package br.com.tlmacedo.cafeperfeito.nfe;

import org.w3c.dom.Node;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: thiagomacedo
 * Date: 2019-06-19
 * Time: 16:15
 */

public class ServiceOutputXML {

    public static String outputXML(Node node) {
        String xml = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(node), new StreamResult(os));
            xml = os.toString();
            if ((xml != null) && (!"".equals(xml))) {
                xml = xml.replaceAll("\\r\\n", "");
                xml = xml.replaceAll(" standalone=\"no\"", "");
                xml = xml.replaceAll("<\\?xml version=\"1.0\" encoding=\"UTF-8\"\\?>", "");
            }
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return xml;
    }
}
