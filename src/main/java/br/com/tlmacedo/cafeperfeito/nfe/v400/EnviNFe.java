package br.com.tlmacedo.cafeperfeito.nfe.v400;

import br.com.tlmacedo.cafeperfeito.service.ServiceXmlUtil;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TEnviNFe;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe;

import javax.xml.bind.JAXBException;

import static br.com.tlmacedo.cafeperfeito.service.ServiceVariaveisSistema.TCONFIG;

/**
 * Created by IntelliJ IDEA.
 * User: thiagomacedo
 * Date: 2019-06-04
 * Time: 21:19
 */

public class EnviNFe {

    TEnviNFe tEnviNFe;
    TNFe tnFe;

    public EnviNFe(TNFe tnFe) {
        setTnFe(tnFe);

        newEnviNFe();
    }

    public EnviNFe(String tnFe) {
        try {
            setTnFe(ServiceXmlUtil.xmlToObject(tnFe, TNFe.class));
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        newEnviNFe();
    }

    private void newEnviNFe() {
        settEnviNFe(new TEnviNFe());
        gettEnviNFe().setVersao(TCONFIG.getNfe().getVersao());
        gettEnviNFe().setIdLote(String.format("%015d", 1));
        gettEnviNFe().setIndSinc(TCONFIG.getNfe().getIndSinc());
        gettEnviNFe().getNFe().add(getTnFe());
    }

    public TEnviNFe gettEnviNFe() {
        return tEnviNFe;
    }

    public void settEnviNFe(TEnviNFe tEnviNFe) {
        this.tEnviNFe = tEnviNFe;
    }

    public TNFe getTnFe() {
        return tnFe;
    }

    public void setTnFe(TNFe tnFe) {
        this.tnFe = tnFe;
    }
}
