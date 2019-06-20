package br.com.tlmacedo.cafeperfeito.nfe;

import br.com.tlmacedo.cafeperfeito.model.vo.SaidaProdutoNfe;
import br.com.tlmacedo.cafeperfeito.service.ServiceValidarDado;

import static br.com.tlmacedo.cafeperfeito.service.ServiceVariaveisSistema.TCONFIG;

/**
 * Created by IntelliJ IDEA.
 * User: thiagomacedo
 * Date: 2019-05-28
 * Time: 18:18
 */

public class ServiceGerarChaveNfe {

    public static String Gerar(SaidaProdutoNfe nfe) {
        String cUF = String.valueOf(TCONFIG.getInfLoja().getCUF());
        String aAMM = String.format("%02d%02d", nfe.getSaidaProduto().getDataCadastro().getYear() % 100,
                nfe.getSaidaProduto().getDataCadastro().getMonthValue());
        String cnpj = TCONFIG.getInfLoja().getCnpj();
        String mod = String.format("%02d", nfe.getModelo().getCod());
        String serie = String.format("%03d", nfe.getSerie());
        String nNF = String.format("%09d", nfe.getNumero());
        String tpEmis = String.valueOf(TCONFIG.getNfe().getTpEmis());
        String cNF = String.format("%04d%02d%02d", nfe.getDataHoraEmissao().getYear(),
                nfe.getDataHoraEmissao().getMonthValue(),
                nfe.getDataHoraEmissao().getDayOfMonth());
        String chave = String.format("%s%s%s%s%s%s%s%s",
                cUF,
                aAMM,
                cnpj,
                mod,
                serie,
                nNF,
                tpEmis,
                cNF);
        return String.format("%s%d",
                chave,
                ServiceValidarDado.nfeDv(chave));
    }
}

