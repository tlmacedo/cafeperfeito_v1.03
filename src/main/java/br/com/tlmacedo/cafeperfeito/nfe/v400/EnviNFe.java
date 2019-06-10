package br.com.tlmacedo.cafeperfeito.nfe.v400;

import br.inf.portalfiscal.xsd.nfe.nfe.TEnviNFe;
import br.inf.portalfiscal.xsd.nfe.nfe.TNFe;

/**
 * Created by IntelliJ IDEA.
 * User: thiagomacedo
 * Date: 2019-06-04
 * Time: 21:19
 */

public class EnviNFe {

    TEnviNFe tEnviNFe;

    public EnviNFe(TNFe tnFe) {

        settEnviNFe(new TEnviNFe());

        gettEnviNFe().setVersao("4.00");
        gettEnviNFe().setIdLote("0000001");
        gettEnviNFe().getNFe().add(tnFe);


    }

    public TEnviNFe gettEnviNFe() {
        return tEnviNFe;
    }

    public void settEnviNFe(TEnviNFe tEnviNFe) {
        this.tEnviNFe = tEnviNFe;
    }
}
