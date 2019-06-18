package br.com.tlmacedo.cafeperfeito.service;

import br.inf.portalfiscal.xsd.nfe.enviNFe.TEnviNFe;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static br.com.tlmacedo.cafeperfeito.service.ServiceVariaveisSistema.TCONFIG;

/**
 * Created by IntelliJ IDEA.
 * User: thiagomacedo
 * Date: 2019-06-05
 * Time: 11:54
 */

public class ServiceFileSave {

    public static boolean saveNfeXmlOut(TEnviNFe tEnviNFe) {
        try {
            TNFe tnFe = tEnviNFe.getNFe().get(0);
            System.out.printf(String.format("001[%s]\n",
                    TCONFIG.getPaths().getPathNFeSaveXmlOut().trim()));

            System.out.printf(String.format("002[%s]\n",
                    (tnFe.getInfNFe().getId())));

            System.out.printf(String.format("003[%s]\n",
                    (tnFe.getSignature() != null) ? "-assinada" : ""));


            FileWriter arqXml = new FileWriter(new File(
                    String.format("%s%s%s.xml",
                            TCONFIG.getPaths().getPathNFeSaveXmlOut().trim(),
                            tnFe.getInfNFe().getId(),
                            (tnFe.getSignature() != null) ? "-assinada" : "")
            ));
            arqXml.write(ServiceXmlUtil.objectToXml(tEnviNFe));
            arqXml.close();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
