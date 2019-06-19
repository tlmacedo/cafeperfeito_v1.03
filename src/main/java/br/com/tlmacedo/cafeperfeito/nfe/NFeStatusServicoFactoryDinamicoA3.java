package br.com.tlmacedo.cafeperfeito.nfe;

import br.com.tlmacedo.cafeperfeito.nfe.v400.ServiceLoadCertificates;
import br.com.tlmacedo.cafeperfeito.service.ServiceVariaveisSistema;
import br.com.tlmacedo.cafeperfeito.service.ServiceXmlUtil;
import br.inf.portalfiscal.wsdl.nfe.hom.nfeStatusServico4.NfeStatusServico4Stub;
import br.inf.portalfiscal.xsd.nfe.consStatServ.TConsStatServ;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.rmi.RemoteException;

public class NFeStatusServicoFactoryDinamicoA3 {

    public NFeStatusServicoFactoryDinamicoA3() {
        new ServiceVariaveisSistema().getVariaveisSistemaBasica();
        //System.setProperty("senhaDoCertificado", "4879");
        try {
            /**
             * Informações do Certificado Digital A3.
             */
            ServiceLoadCertificates certificates = new ServiceLoadCertificates();
            certificates.loadToken();
            certificates.loadSocketDinamico();


            /**
             * Xml de Consulta.
             */
            TConsStatServ consStatServ = new TConsStatServ();
            consStatServ.setTpAmb("2");
            consStatServ.setCUF("13");
            consStatServ.setVersao("4.00");
            consStatServ.setXServ("STATUS");
            String xml = null;
            try {
                xml = ServiceXmlUtil.objectToXml(consStatServ);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            //String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><consStatServ versao=\"4.00\" xmlns=\"http://www.portalfiscal.inf.br/nfe\"><tpAmb>2</tpAmb><cUF>35</cUF><xServ>STATUS</xServ></consStatServ>";//objectToXml(consStatServ);

            OMElement ome = null;
            try {
                ome = AXIOMUtil.stringToOM(xml);
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
            NfeStatusServico4Stub.NfeDadosMsg dadosMsg = new NfeStatusServico4Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);

            System.out.println(xml);

            NfeStatusServico4Stub stub = null;
            try {
                stub = new NfeStatusServico4Stub();
            } catch (AxisFault axisFault) {
                axisFault.printStackTrace();
            }
            NfeStatusServico4Stub.NfeResultMsg result = null;
            try {
                result = stub.nfeStatusServicoNF(dadosMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            System.out.println(result.getExtraElement().toString());

            info(result.getExtraElement().toString());
        } catch (Exception e) {
            e.printStackTrace();
            error(e.toString());
        }
    }

    public static void main(String[] args) {
        new NFeStatusServicoFactoryDinamicoA3();
    }

    /**
     * Log Info.
     *
     * @param log
     */
    private static void info(String log) {
        System.out.println("INFO: " + log);
    }

    /**
     * Log Error.
     *
     * @param log
     */
    private static void error(String log) {
        System.out.println("ERROR: " + log);
    }

//    public static <T> String objectToXml(Object obj) throws JAXBException {
//
//        JAXBContext context = null;
//        JAXBElement<?> element = null;
//
//        switch (obj.getClass().getSimpleName()) {
//
//            case "TConsStatServ":
//                context = JAXBContext.newInstance(TConsStatServ.class);
//                element = new br.inf.portalfiscal.xsd.nfe.consStatServ.ObjectFactory().createConsStatServ((TConsStatServ) obj);
//                break;
//
////            case ENVIO_NFE:
////                context = JAXBContext.newInstance(TEnviNFe.class);
////                element = new br.inf.portalfiscal.nfe.schema_4.enviNFe.ObjectFactory().createEnviNFe((TEnviNFe) obj);
////                break;
////
////            case RETORNO_ENVIO:
////                context = JAXBContext.newInstance(TRetEnviNFe.class);
////                element = XsdUtil.enviNfe.createTRetEnviNFe((TRetEnviNFe) obj);
////                break;
////
////            case SITUACAO_NFE:
////                context = JAXBContext.newInstance(TConsSitNFe.class);
////                element = new br.inf.portalfiscal.nfe.schema_4.consSitNFe.ObjectFactory().createConsSitNFe((TConsSitNFe) obj);
////                break;
////
////            case DIST_DFE:
////                context = JAXBContext.newInstance(DistDFeInt.class);
////                element = new br.inf.portalfiscal.nfe.schema.distdfeint.ObjectFactory().createDistDFeInt((DistDFeInt) obj);
////                break;
////
////            case TCONSRECINFE:
////                context = JAXBContext.newInstance(TConsReciNFe.class);
////                element = new br.inf.portalfiscal.nfe.schema_4.consReciNFe.ObjectFactory().createConsReciNFe((TConsReciNFe) obj);
////                break;
////
////            case TConsCad:
////                context = JAXBContext.newInstance(TConsCad.class);
////                element = new br.inf.portalfiscal.nfe.schema.consCad.ObjectFactory().createConsCad((TConsCad) obj);
////                break;
////
////            case INUTILIZACAO:
////                context = JAXBContext.newInstance(TInutNFe.class);
////                element = new br.inf.portalfiscal.nfe.schema_4.inutNFe.ObjectFactory().createInutNFe((TInutNFe) obj);
////                break;
////
////            case SITUACAO_NFE_RET:
////                context = JAXBContext.newInstance(TRetConsSitNFe.class);
////                element = new br.inf.portalfiscal.nfe.schema_4.retConsSitNFe.ObjectFactory().createRetConsSitNFe((TRetConsSitNFe) obj);
////                break;
////
////            case TPROCEVENTO:
////                switch (obj.getClass().getName()) {
////                    case TPROCCANCELAR:
////                        context = JAXBContext.newInstance(br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TProcEvento.class);
////                        element = new br.inf.portalfiscal.nfe.schema.envEventoCancNFe.ObjectFactory().createTProcEvento((br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TProcEvento) obj);
////                        break;
////                    case TPROCCCE:
////                        context = JAXBContext.newInstance(br.inf.portalfiscal.nfe.schema.envcce.TProcEvento.class);
////                        element = new br.inf.portalfiscal.nfe.schema.envcce.ObjectFactory().createTProcEvento((br.inf.portalfiscal.nfe.schema.envcce.TProcEvento) obj);
////                        break;
////                    case TPROCEPEC:
////                        context = JAXBContext.newInstance(br.inf.portalfiscal.nfe.schema.envEpec.TProcEvento.class);
////                        element = XsdUtil.epec.createTProcEvento((br.inf.portalfiscal.nfe.schema.envEpec.TProcEvento) obj);
////                        break;
////                }
////
////                break;
////
////            case NFEPROC:
////                context = JAXBContext.newInstance(TNfeProc.class);
////                element = XsdUtil.enviNfe.createTNfeProc((TNfeProc) obj);
////                break;
////
////            case TPROCINUT:
////                context = JAXBContext.newInstance(TProcInutNFe.class);
////                element = XsdUtil.inutNfe.createTProcInutNFe((TProcInutNFe) obj);
////                break;
////
////            case EVENTO:
////                switch (obj.getClass().getName()) {
////                    case CANCELAR:
////                        context = JAXBContext.newInstance(TEnvEvento.class);
////                        element = new br.inf.portalfiscal.nfe.schema.envEventoCancNFe.ObjectFactory().createEnvEvento((TEnvEvento) obj);
////                        break;
////                    case CCE:
////                        context = JAXBContext.newInstance(br.inf.portalfiscal.nfe.schema.envcce.TEnvEvento.class);
////                        element = new br.inf.portalfiscal.nfe.schema.envcce.ObjectFactory().createEnvEvento((br.inf.portalfiscal.nfe.schema.envcce.TEnvEvento) obj);
////                        break;
////                    case EPEC:
////                        context = JAXBContext.newInstance(br.inf.portalfiscal.nfe.schema.envEpec.TEnvEvento.class);
////                        element = new br.inf.portalfiscal.nfe.schema.envEpec.ObjectFactory().createEnvEvento((br.inf.portalfiscal.nfe.schema.envEpec.TEnvEvento) obj);
////                        break;
////                    case MANIFESTAR:
////                        context = JAXBContext.newInstance(br.inf.portalfiscal.nfe.schema.envConfRecebto.TEnvEvento.class);
////                        element = new br.inf.portalfiscal.nfe.schema.envConfRecebto.ObjectFactory().createEnvEvento((br.inf.portalfiscal.nfe.schema.envConfRecebto.TEnvEvento) obj);
////                        break;
////                }
////                break;
////
////            case TProtNFe:
////                switch (obj.getClass().getName()) {
////                    case TProtEnvi:
////                        context = JAXBContext.newInstance(TProtNFe.class);
////                        element = XsdUtil.enviNfe.createTProtNFe((br.inf.portalfiscal.nfe.schema_4.enviNFe.TProtNFe) obj);
////                        break;
////                    case TProtCons:
////                        context = JAXBContext.newInstance(br.inf.portalfiscal.nfe.schema_4.retConsSitNFe.TProtNFe.class);
////                        element = XsdUtil.retConsSitNfe.createTProtNFe((br.inf.portalfiscal.nfe.schema_4.retConsSitNFe.TProtNFe) obj);
////                        break;
////                    case TProtReci:
////                        context = JAXBContext.newInstance(br.inf.portalfiscal.nfe.schema_4.retConsReciNFe.TProtNFe.class);
////                        element = XsdUtil.retConsReciNfe.createTProtNFe((br.inf.portalfiscal.nfe.schema_4.retConsReciNFe.TProtNFe) obj);
////                        break;
////                }
////                break;
////
////            default:
////                throw new NfeException("Objeto não mapeado no XmlUtil:" + obj.getClass().getSimpleName());
//        }
//        Marshaller marshaller = context.createMarshaller();
//
//        marshaller.setProperty("jaxb.encoding", "Unicode");
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
//        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
//
//        StringWriter sw = new StringWriter();
//
////        if (obj.getClass().getSimpleName().equals(ENVIO_NFE) || obj.getClass().getSimpleName().equals(NFEPROC)) {
////            CDATAContentHandler cdataHandler = new CDATAContentHandler(sw, "utf-8");
////            marshaller.marshal(element, cdataHandler);
////        } else {
//        marshaller.marshal(element, sw);
////        }
//        StringBuilder xml = new StringBuilder();
//        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(sw.toString());
//
////        if ((obj.getClass().getSimpleName().equals(TPROCEVENTO))) {
////            return replacesNfe(xml.toString().replaceAll("procEvento", "procEventoNFe"));
////        } else {
//        return replacesNfe(xml.toString());
////        }
//
//    }

    private static String replacesNfe(String xml) {

        xml = xml.replaceAll("ns2:", "");
        xml = xml.replaceAll("<!\\[CDATA\\[<!\\[CDATA\\[", "<!\\[CDATA\\[");
        xml = xml.replaceAll("\\]\\]>\\]\\]>", "\\]\\]>");
        xml = xml.replaceAll("ns3:", "");
        xml = xml.replaceAll("&lt;", "<");
        xml = xml.replaceAll("&amp;", "&");
        xml = xml.replaceAll("&gt;", ">");
        xml = xml.replaceAll("<Signature>", "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">");
        xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
        xml = xml.replaceAll(" xmlns=\"\" xmlns:ns3=\"http://www.portalfiscal.inf.br/nfe\"", "");

        return xml;

    }
}