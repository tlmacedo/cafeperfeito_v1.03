package br.com.tlmacedo.cafeperfeito.nfe.v400;

import br.com.tlmacedo.cafeperfeito.service.ServiceAlertMensagem;
import javafx.util.Pair;

import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: thiagomacedo
 * Date: 2019-06-03
 * Time: 15:44
 */

public class ServiceLoadCertificates {

    private static KeyStore keyStoreA3;
    private ServiceAlertMensagem alertMensagem;
    private PrivateKey privateKey;
    private KeyInfo keyInfo;
    private String senhaDoCertificado = null;

    public ServiceLoadCertificates() {
        setSenhaDoCertificado(System.getProperty("senhaDoCertificado"));
    }

    public Pair<KeyInfo, PrivateKey> getCertInfos(XMLSignatureFactory signatureFactory) {
        if (keyStoreA3 == null) {
//        setSenhaDoCertificado("4879");
//        if (getSenhaDoCertificado() == null) {
////            Platform.runLater(() -> {
//                setAlertMensagem(new ServiceAlertMensagem());
//                getAlertMensagem().setCabecalho("Token certificado");
//                getAlertMensagem().setPromptText("Senha do certificado");
//
//                setSenhaDoCertificado(getAlertMensagem().getRetornoAlert_PasswordField().get());
////            });
//            if (getSenhaDoCertificado() == null)
//                return null;
//        }

            Provider p = Security.getProvider("SunPKCS11");
            p = p.configure("/Volumes/150GB-Development/cafeperfeito/cafeperfeito/src/main/resources/certificado/tokenSafeNet5100.cfg");
            //p = p.configure(TCONFIG.getNfe().getCertificadoCfg());
            Security.addProvider(p);

            System.out.printf("senha: [%s]\n", getSenhaDoCertificado());
            System.out.printf("senha.toCharArray(): [%s]\n", getSenhaDoCertificado().toCharArray());

            char[] pin = getSenhaDoCertificado().toCharArray();
            try {
                keyStoreA3 = KeyStore.getInstance("pkcs11", p);
                keyStoreA3.load(null, pin);

                KeyStore.PrivateKeyEntry pkEntry = null;
                Enumeration<String> aliasesEnum = keyStoreA3.aliases();
                while (aliasesEnum.hasMoreElements()) {
                    String alias = (String) aliasesEnum.nextElement();
                    if (keyStoreA3.isKeyEntry(alias)) {
                        pkEntry = (KeyStore.PrivateKeyEntry) keyStoreA3.getEntry(alias,
                                new KeyStore.PasswordProtection(getSenhaDoCertificado().toCharArray()));
                        setPrivateKey(pkEntry.getPrivateKey());
                        break;
                    }
                }
                X509Certificate cert = (X509Certificate) pkEntry.getCertificate();

                KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
                List<X509Certificate> x509Content = new ArrayList<X509Certificate>();

                x509Content.add(cert);
                X509Data x509Data = keyInfoFactory.newX509Data(x509Content);
                setKeyInfo(keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data)));


            } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableEntryException e) {
                e.printStackTrace();
            }
        }
        return new Pair<KeyInfo, PrivateKey>(keyInfo, privateKey);
        //return keyStoreA3;
    }


    public ServiceAlertMensagem getAlertMensagem() {
        return alertMensagem;
    }

    public void setAlertMensagem(ServiceAlertMensagem alertMensagem) {
        this.alertMensagem = alertMensagem;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public KeyInfo getCertInfos() {
        return keyInfo;
    }

    public void setKeyInfo(KeyInfo keyInfo) {
        this.keyInfo = keyInfo;
    }

    public String getSenhaDoCertificado() {
        return senhaDoCertificado;
    }

    public void setSenhaDoCertificado(String senhaDoCertificado) {
        this.senhaDoCertificado = senhaDoCertificado;
    }
}
