package br.com.tlmacedo.cafeperfeito.nfe.v400;

import br.com.tlmacedo.cafeperfeito.service.ServiceXmlUtil;
import br.inf.portalfiscal.xsd.nfe.nfe.TNFe;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: thiagomacedo
 * Date: 2019-06-03
 * Time: 15:43
 */

public class ServiceAssinarXml {
    private static final String NFE = "NFe";

    private Document document;
    private XMLSignatureFactory signatureFactory;
    private ArrayList<Transform> transformList;
    private ServiceLoadCertificates certificates;

    public ServiceAssinarXml(String xml, ServiceLoadCertificates certificates) {
        setCertificates(certificates);
        assinar(xml);
    }


    public ServiceAssinarXml(TNFe tnFe) {
        try {
            assinar(ServiceXmlUtil.objectToXml(tnFe));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void assinar(String xml) {
        setDocument(documentFactory(xml));
        setSignatureFactory(XMLSignatureFactory.getInstance("DOM"));
        setTransformList(signatureFactory(getSignatureFactory()));

        getCertificates().setKeyInfo(getSignatureFactory());

        for (int i = 0; i < getDocument().getElementsByTagName(NFE).getLength(); i++) {
            assinarNFe(i);
        }
    }

    private Document documentFactory(String xml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document document = null;
        try {
            document = factory.newDocumentBuilder().parse(
                    new ByteArrayInputStream(xml.getBytes()));
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            ex.printStackTrace();
        }
        return document;
    }

    private ArrayList<Transform> signatureFactory(XMLSignatureFactory signatureFactory) {
        ArrayList<Transform> transformList = new ArrayList<Transform>();
        TransformParameterSpec tps = null;
        try {
            Transform envelopedTransform = signatureFactory.newTransform(
                    Transform.ENVELOPED, tps);
            Transform c14NTransform = signatureFactory.newTransform(
                    "http://www.w3.org/TR/2001/REC-xml-c14n-20010315", tps);
            transformList.add(envelopedTransform);
            transformList.add(c14NTransform);
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return transformList;
    }

    private void assinarNFe(int indexNFe) {
        NodeList elements = getDocument().getElementsByTagName("infNFe");
        Element el = (Element) elements.item(indexNFe);
        el.setIdAttribute("Id", true);
        String id = el.getAttribute("Id");
        try {
            Reference reference = getSignatureFactory().newReference("#" + id,
                    getSignatureFactory().newDigestMethod(DigestMethod.SHA1, null), getTransformList(), null, null);

            SignedInfo signedInfo = getSignatureFactory().newSignedInfo(getSignatureFactory().newCanonicalizationMethod(
                    CanonicalizationMethod.INCLUSIVE,
                    (C14NMethodParameterSpec) null),
                    getSignatureFactory().newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                    Collections.singletonList(reference));

            XMLSignature signature = getSignatureFactory().newXMLSignature(signedInfo, getCertificates().getKeyInfo());

            DOMSignContext domSignContext = new DOMSignContext(getCertificates().getPrivateKey(),
                    getDocument().getElementsByTagName(NFE).item(indexNFe));

            domSignContext.setBaseURI("ok");

            signature.sign(domSignContext);

        } catch (InvalidAlgorithmParameterException | MarshalException | XMLSignatureException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }

    public String outputXML() {
        String xml = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(getDocument()), new StreamResult(os));
            xml = os.toString();
            if ((xml != null) && (!"".equals(xml))) {
                xml = xml.replaceAll("\\r\\n", "");
                xml = xml.replaceAll(" standalone=\"no\"", "");
            }
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        //System.out.printf("\n\n\nxml: *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\n[%s]\n", xml);
        return xml;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public XMLSignatureFactory getSignatureFactory() {
        return signatureFactory;
    }

    public void setSignatureFactory(XMLSignatureFactory signatureFactory) {
        this.signatureFactory = signatureFactory;
    }

    public ArrayList<Transform> getTransformList() {
        return transformList;
    }

    public void setTransformList(ArrayList<Transform> transformList) {
        this.transformList = transformList;
    }

    public ServiceLoadCertificates getCertificates() {
        return certificates;
    }

    public void setCertificates(ServiceLoadCertificates certificates) {
        this.certificates = certificates;
    }
}