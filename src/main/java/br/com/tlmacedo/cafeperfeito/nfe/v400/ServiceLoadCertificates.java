package br.com.tlmacedo.cafeperfeito.nfe.v400;

import br.com.tlmacedo.cafeperfeito.service.ServiceAlertMensagem;
import br.com.tlmacedo.cafeperfeito.service.ServiceSocketFactoryDinamico;
import org.apache.commons.httpclient.protocol.Protocol;

import javax.security.auth.callback.PasswordCallback;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import java.security.*;
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

    private ServiceAlertMensagem alertMensagem;
    private PrivateKey privateKey;
    private X509Certificate x509Certificate;
    private KeyInfo keyInfo;
    private String senhaDoCertificado = null;

    public ServiceLoadCertificates() {
        setSenhaDoCertificado(System.getProperty("senhaDoCertificado"));
    }

    public void loadToken() {
        try {
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
            String configName = "/Volumes/150GB-Development/cafeperfeito/cafeperfeito_v1.03/src/main/resources/certificado/tokenSafeNet5100.cfg";

            Provider p = Security.getProvider("SunPKCS11");
            p = p.configure(configName);
            Security.addProvider(p);

            char[] pin = getSenhaDoCertificado().toCharArray();

            KeyStore.CallbackHandlerProtection chp =
                    new KeyStore.CallbackHandlerProtection(callbacks -> {
                        for (int i = 0; i < callbacks.length; i++) {
                            PasswordCallback pc = (PasswordCallback) callbacks[i];
                            pc.setPassword(getSenhaDoCertificado().toCharArray());
                        }
                    });
            KeyStore.Builder builder =
                    KeyStore.Builder.newInstance("PKCS11", p, chp);

            KeyStore ks = builder.getKeyStore();

            KeyStore.PrivateKeyEntry pkEntry = null;
            String alias = null;
            Enumeration<String> aliasesEnum = ks.aliases();
            while (aliasesEnum.hasMoreElements()) {
                alias = (String) aliasesEnum.nextElement();
                if (ks.isKeyEntry(alias)) {
                    pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias,
                            new KeyStore.PasswordProtection(getSenhaDoCertificado().toCharArray()));
                    setPrivateKey(pkEntry.getPrivateKey());
                    break;
                }
            }

            setX509Certificate((X509Certificate) pkEntry.getCertificate());

        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableEntryException ex) {
            ex.printStackTrace();
        }
    }

    public void setKeyInfo(XMLSignatureFactory signatureFactory) {
        if (getX509Certificate() == null) {
            loadToken();
            System.out.printf("loadToken");
        }

        KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
        List<X509Certificate> x509Content = new ArrayList<X509Certificate>();

        x509Content.add(getX509Certificate());
        X509Data x509Data = keyInfoFactory.newX509Data(x509Content);

        this.keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));
    }

    public void abreSocketDinamico() {
        ServiceSocketFactoryDinamico socketFactoryDinamico = new ServiceSocketFactoryDinamico(getX509Certificate(), getPrivateKey());
        socketFactoryDinamico.setFileCacerts("/Volumes/150GB-Development/cafeperfeito/cafeperfeito_v1.03/src/main/resources/certificado/NFeCacerts");

        Protocol protocol = new Protocol("https", socketFactoryDinamico, 443);
        Protocol.registerProtocol("https", protocol);
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

    public KeyInfo getKeyInfo() {
        return keyInfo;
    }

    public String getSenhaDoCertificado() {
        return senhaDoCertificado;
    }

    public void setSenhaDoCertificado(String senhaDoCertificado) {
        this.senhaDoCertificado = senhaDoCertificado;
    }

    public X509Certificate getX509Certificate() {
        return x509Certificate;
    }

    public void setX509Certificate(X509Certificate x509Certificate) {
        this.x509Certificate = x509Certificate;
    }
}
