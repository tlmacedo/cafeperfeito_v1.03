package br.com.tlmacedo.cafeperfeito.nfe.v400;

import br.com.tlmacedo.cafeperfeito.model.dao.EmpresaDAO;
import br.com.tlmacedo.cafeperfeito.model.vo.*;
import br.com.tlmacedo.cafeperfeito.model.vo.enums.ClassificacaoJuridica;
import br.com.tlmacedo.cafeperfeito.model.vo.enums.EnderecoTipo;
import br.com.tlmacedo.cafeperfeito.service.ServiceMascara;
import br.inf.portalfiscal.xsd.nfe.enviNFe.*;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe.*;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe.Cobr.Dup;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe.Cobr.Fat;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe.Det.Imposto;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe.Det.Imposto.COFINS.COFINSNT;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe.Det.Imposto.ICMS.*;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe.Det.Imposto.PIS;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe.Det.Imposto.PIS.PISNT;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe.Det.Prod;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe.Total.ICMSTot;
import br.inf.portalfiscal.xsd.nfe.enviNFe.TNFe.InfNFe.Transp.Transporta;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static br.com.tlmacedo.cafeperfeito.interfaces.Convert_Date_Key.*;
import static br.com.tlmacedo.cafeperfeito.service.ServiceVariaveisSistema.TCONFIG;

/**
 * Created by IntelliJ IDEA.
 * User: thiagomacedo
 * Date: 2019-05-27
 * Time: 16:33
 */

public class Nfe {

    TNFe tnFe;
    TEnviNFe tEnviNFe;

    SaidaProduto saidaProduto;

    public Nfe(SaidaProduto saidaProduto) {
        setSaidaProduto(saidaProduto);

        setTnFe(new TNFe());

        getTnFe().setInfNFe(new InfNFe());
        getNewInfNFe();


        settEnviNFe(getTnFe());

//        try {
//            info("XML NF-e: " + ServiceXmlUtil.printXmlFromString(strValueOf(getTnFe()), true));
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Grupo B. Identificação da Nota Fiscal eletrônica
     */
    private void getNewIde() {
        Ide ide = getTnFe().getInfNFe().getIde();

        /**Código da UF do emitente do Documento Fiscal
         * Código da UF do emitente do Documento Fiscal.
         * Utilizar a Tabela do IBGE de código de unidades da federação (Anexo IX - Tabela de UF, Município e País).
         */
        ide.setCUF(String.valueOf(TCONFIG.getNfe().getCUF()));

        /**Código Numérico que compõe a Chave de Acesso
         * Código numérico que compõe a Chave de Acesso.
         * Número aleatório gerado pelo emitente para cada NF-e para evitar acessos indevidos da NF-e. (v2.0)
         */
        ide.setCNF(String.format("%04d%02d%02d", getSaidaProduto().getNfe().getDataHoraEmissao().getYear(),
                getSaidaProduto().getNfe().getDataHoraEmissao().getMonthValue(),
                getSaidaProduto().getNfe().getDataHoraEmissao().getDayOfMonth()));

        /**Descrição da Natureza da Operação
         * Informar a natureza da operação de que decorrer a saída ou a entrada, tais como:
         * venda, compra, transferência, devolução, importação, consignação,
         * remessa (para fins de demonstração, de industrialização ou outra), conforme previsto na alínea 'i',
         * inciso I, art. 19 do CONVÊNIO S/No, de 15 de dezembro de 1970.
         */
        ide.setNatOp(getSaidaProduto().getNfe().getNaturezaOperacao());

        /**Código do Modelo do Documento Fiscal
         * 55=NF-e emitida em substituição ao modelo 1 ou 1A;
         * 65=NFC-e, utilizada nas operações de venda no varejo (a critério da UF aceitar este modelo de documento).
         */
        ide.setMod(getSaidaProduto().getNfe().getModelo().getDescricao());

        /**Série do Documento Fiscal
         * Série do Documento Fiscal, preencher com zeros na hipótese de a NF-e não possuir série.
         * Série na faixa:
         * - [000-889]: Aplicativo do Contribuinte; Emitente=CNPJ; Assinatura pelo e-CNPJ do contribuinte (procEmi<>1,2);
         * - [890-899]: Emissão no site do Fisco (NFA-e - Avulsa); Emitente= CNPJ / CPF; Assinatura pelo e-CNPJ da SEFAZ (procEmi=1);
         * - [900-909]: Emissão no site do Fisco (NFA-e); Emitente= CNPJ; Assinatura pelo e-CNPJ da SEFAZ (procEmi=1), ou Assinatura pelo e-CNPJ do contribuinte (procEmi=2);
         * - [910-919]: Emissão no site do Fisco (NFA-e); Emitente= CPF; Assinatura pelo e-CNPJ da SEFAZ (procEmi=1), ou Assinatura pelo e-CPF do contribuinte (procEmi=2);
         * - [920-969]: Aplicativo do Contribuinte; Emitente=CPF; Assinatura pelo e-CPF do contribuinte (procEmi<>1,2);
         * (Atualizado NT 2018/001)
         */
        ide.setSerie(String.valueOf(getSaidaProduto().getNfe().getSerie()));

        /**Número do Documento Fiscal
         * Número do Documento Fiscal.
         */
        ide.setNNF(String.valueOf(getSaidaProduto().getNfe().getNumero()));

        /**Data e hora de emissão do Documento Fiscal
         * Data e hora no formato UTC (Universal Coordinated Time):
         * AAAA-MM-DDThh:mm:ssTZD
         */
        ide.setDhEmi(ZonedDateTime.of(getSaidaProduto().getNfe().getDataHoraEmissao(), MY_ZONE_TIME).format(DTF_NFE_TO_LOCAL_DATE));

        /**Data e hora de Saída ou da Entrada da Mercadoria/Produto
         * Data e hora no formato UTC (Universal Coordinated Time):
         * AAAA-MM-DDThh:mm:ssTZD.
         * Observação: Não informar este campo para a NFC-e.
         */
        ide.setDhSaiEnt(ZonedDateTime.of(getSaidaProduto().getNfe().getDataHoraSaida(), MY_ZONE_TIME).format(DTF_NFE_TO_LOCAL_DATE));    //Data e hora de Saída ou da Entrada da Mercadoria/Produto

        /**Tipo de Operação
         * 0=Entrada;
         * 1=Saída
         */
        ide.setTpNF(String.valueOf(TCONFIG.getNfe().getTpNF()));

        /**dentificador de local de destino da operação
         * 1=Operação interna;
         * 2=Operação interestadual;
         * 3=Operação com exterior.
         */
        ide.setIdDest(String.valueOf(getSaidaProduto().getNfe().getDestOperacao().getCod()));

        /**Código do Município de Ocorrência do Fato Gerador
         * Informar o município de ocorrência do fato gerador do ICMS.
         * Utilizar a Tabela do IBGE (Anexo IX - Tabela de UF, Município e País)
         */
        ide.setCMunFG(String.valueOf(TCONFIG.getNfe().getCMunFG()));   //Código do Município de Ocorrência do Fato Gerador

        /**Formato de Impressão do DANFE
         * 0=Sem geração de DANFE;
         * 1=DANFE normal, Retrato;
         * 2=DANFE normal, Paisagem;
         * 3=DANFE Simplificado;
         * 4=DANFE NFC-e;
         * 5=DANFE NFC-e em mensagem eletrônica (o envio de mensagem eletrônica pode ser feita de forma simultânea
         * com a impressão do DANFE; usar o tpImp=5 quando esta for a única forma de disponibilização do DANFE).
         */
        ide.setTpImp(String.valueOf(TCONFIG.getNfe().getTpImp()));

        /**Tipo de Emissão da NF-e
         * 1=Emissão normal (não em contingência);
         * 2=Contingência FS-IA, com impressão do DANFE em Formulário de Segurança - Impressor Autônomo;
         * 3=Contingência SCAN (Sistema de Contingência do Ambiente Nacional); *Desativado * NT 2015/002
         * 4=Contingência EPEC (Evento Prévio da Emissão em Contingência);
         * 5=Contingência FS-DA, com impressão do DANFE em Formulário de Segurança - Documento Auxiliar;
         * 6=Contingência SVC-AN (SEFAZ Virtual de Contingência do AN);
         * 7=Contingência SVC-RS (SEFAZ Virtual de Contingência do RS);
         * 9=Contingência off-line da NFC-e;
         *
         * Observação: Para a NFC-e somente é válida a opção de
         * contingência: 9-Contingência Off-Line e, a critério da
         *   UF, opção 4-Contingência EPEC. (NT 2015/002)
         */
        ide.setTpEmis(String.valueOf(TCONFIG.getNfe().getTpEmis()));

        /**Dígito Verificador da Chave de Acesso da NF-e
         * Informar o DV da Chave de Acesso da NF-e,
         * o DV será calculado com a aplicação do algoritmo módulo 11 (base 2,9) da Chave de Acesso.
         * (vide item 5.4 do MOC – Visão Geral)
         */
        ide.setCDV(getSaidaProduto().getNfe().getChave().substring(43, 44));

        /**Identificação do Ambiente
         * 1=Produção;
         * 2=Homologação
         */
        ide.setTpAmb(String.valueOf(TCONFIG.getNfe().getTpAmb()));

        /**Finalidade de emissão da NF-e
         *1=NF-e normal;
         * 2=NF-e complementar;
         * 3=NF-e de ajuste;
         * 4=Devolução de mercadoria.
         */
        ide.setFinNFe(String.valueOf(TCONFIG.getNfe().getFinNFe()));

        /**Indica operação com Consumidor final
         * 0=Normal;
         * 1=Consumidor final;
         */
        //ide.setIndFinal(getSaidaProduto().getNfe().isConsumidorFinal() ? "1" : "0");
        ide.setIndFinal((!getSaidaProduto().getCliente().isIeIsento() && getSaidaProduto().getCliente().getIe().equals(""))
                ? "1" : "0");

        /**Indicador de presença do comprador no estabelecimento comercial no momento da operação
         * 0=Não se aplica (por exemplo, Nota Fiscal complementar ou de ajuste);
         * 1=Operação presencial;
         * 2=Operação não presencial, pela Internet;
         * 3=Operação não presencial, Teleatendimento;
         * 4=NFC-e em operação com entrega a domicílio;
         * 5=Operação presencial, fora do estabelecimento;   (incluído NT 2016/002)
         * 9=Operação não presencial, outros.
         */
        ide.setIndPres(String.valueOf(getSaidaProduto().getNfe().getPresencaComprador().getCod()));

        /**Processo de emissão da NF-e
         * 0=Emissão de NF-e com aplicativo do contribuinte;
         * 1=Emissão de NF-e avulsa pelo Fisco;
         * 2=Emissão de NF-e avulsa, pelo contribuinte com seu certificado digital, através do site do Fisco;
         * 3=Emissão NF-e pelo contribuinte com aplicativo fornecido pelo Fisco.
         */
        ide.setProcEmi(String.valueOf(TCONFIG.getNfe().getProcEmi()));

        /**Versão do Processo de emissão da NF-e
         * Informar a versão do aplicativo emissor de NF-e.
         */
        ide.setVerProc(TCONFIG.getNfe().getVerProc());  //Versão do Processo de emissão da NF-e

        //return ide;
    }

    /**
     * Grupo C. Identificação do Emitente da Nota Fiscal eletrônica
     */
    private void getNewEmit() {
        Emit emit = getTnFe().getInfNFe().getEmit();

        Empresa loja = new EmpresaDAO().getAll(Empresa.class, null, null, null, null)
                .stream().filter(empresa -> empresa.isLojaSistema()).findFirst().orElse(null);

        /**CNPJ do emitente
         * Informar o CNPJ do emitente.
         */
        /**
         * Razão Social ou Nome do emitente
         */
//        if (TCONFIG.getNfe().getTpAmb() == 2) {
//            emit.setCNPJ("99999999000191");
//            emit.setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");
//        } else {
        emit.setCNPJ(loja.getCnpj());
        emit.setXNome(loja.getRazao());
//        }


        /**
         * Nome fantasia
         */
        emit.setXFant(loja.getFantasia());

        emit.setEnderEmit(getNewEnderEmi(loja.getEnderecoNFe(), loja.getTelefone().getDescricao()));

        /**Inscrição Estadual do Emitente
         * Informar somente os algarismos, sem os caracteres de formatação (ponto, barra, hífen, etc.).
         */
        emit.setIE(loja.getIe());

        /**IE do Substituto Tributário
         * IE do Substituto Tributário da UF de destino da mercadoria, quando houver a retenção do ICMS ST
         * para a UF de destino.
         */
        //emit.setIEST("");


        emit.setCRT("3");

        //return emit;
    }

    /**
     * Grupo C.1 Endereço do emitente
     */
    private TEnderEmi getNewEnderEmi(Endereco end, String fone) {
        TEnderEmi tEnderEmi = new TEnderEmi();

        /**
         * Logradouro
         */
        tEnderEmi.setXLgr(end.getLogradouro());

        /**
         * Número
         */
        tEnderEmi.setNro(end.getNumero());

        /**
         * Complemento
         */
        if (end.getComplemento() != null && !end.getComplemento().equals(""))
            tEnderEmi.setXCpl(end.getComplemento());

        /**
         * Bairro
         */
        tEnderEmi.setXBairro(end.getBairro());

        /**Código do município
         * Utilizar a Tabela do IBGE (Anexo IX- Tabela de UF, Município e País).
         */
        tEnderEmi.setCMun(end.getMunicipio().getIbge_codigo());

        /**
         * Nome do município
         */
        tEnderEmi.setXMun(end.getMunicipio().getDescricao());

        /**
         * Sigla da UF
         */
        tEnderEmi.setUF(TUfEmi.valueOf(end.getMunicipio().getUf().getSigla()));

        /**Código do CEP
         * Informar os zeros não significativos. (NT 2011/004)
         */
        tEnderEmi.setCEP(end.getCep());

        /**Código do País
         * 1058=Brasil
         */
        tEnderEmi.setCPais("1058");

        /**Nome do País
         * Brasil ou BRASIL
         */
        tEnderEmi.setXPais("BRASIL");

        /**Telefone
         * Preencher com o Código DDD + número do telefone. Nas operações com exterior é permitido informar o
         * código do país + código da localidade + número do telefone (v2.0)
         */
        tEnderEmi.setFone(ServiceMascara.getTelefone(fone));

        return tEnderEmi;
    }

    /**
     * Grupo E.1 Endereço do Destinatário da NF-e
     */
    private TEndereco getNewEndereco(Endereco end, String fone) {
        TEndereco tEndereco = new TEndereco();

        /**
         * Logradouro
         */
        tEndereco.setXLgr(end.getLogradouro());

        /**
         * Número
         */
        tEndereco.setNro(end.getNumero());

        /**
         * Complemento
         */
        tEndereco.setXCpl(end.getComplemento());

        /**
         * Bairro
         */
        tEndereco.setXBairro(end.getBairro());

        /**Código do município
         * Utilizar a Tabela do IBGE (Anexo IX - Tabela de UF, Município e País).
         */
        tEndereco.setCMun(end.getMunicipio().getIbge_codigo());

        /**Nome do Município
         * Informar ‘EXTERIOR ‘para operações com o exterior.
         */
        tEndereco.setXMun(end.getMunicipio().getDescricao());

        /**Sigla da UF
         * Informar ‘EX’ para operações com o exterior.
         */
        tEndereco.setUF(TUf.valueOf(end.getMunicipio().getUf().getSigla()));

        /**Código do CEP
         * Informar os zeros não significativos.
         */
        tEndereco.setCEP(end.getCep());

        /**Código do País
         * Utilizar a Tabela do BACEN (Anexo VII - Tabela de UF, Município e País).
         * 1058=Brasil
         */
        tEndereco.setCPais("1058");

        /**Nome do País
         * Brasil ou BRASIL
         */
        tEndereco.setXPais("BRASIL");

        /**Telefone
         * Preencher com o Código DDD + número do telefone.
         * Nas operações com exterior é permitido informar o código do país + código da localidade + número do telefone (v2.0)
         */
        tEndereco.setFone(ServiceMascara.getTelefone(fone));


        return tEndereco;
    }

    /**
     * Grupo A. Dados da Nota Fiscal eletrônica
     */
    private void getNewInfNFe() {
        InfNFe infNFe = getTnFe().getInfNFe();

        /**Identificador da TAG a ser assinada
         * Informar a Chave de Acesso precedida do literal ‘NFe’,
         */
        infNFe.setId(String.format("NFe%s", getSaidaProduto().getNfe().getChave()));

        /**Versão do leiaute
         * Versão do leiaute (v4.00)
         */
        infNFe.setVersao(TCONFIG.getNfe().getVersao());


        /**Informações de identificação da
         */
        infNFe.setIde(new Ide());
        getNewIde();
        infNFe.setEmit(new Emit());
        getNewEmit();
        infNFe.setDest(new Dest());
        getNewDest();

//        if (getSaidaProduto().getCliente().getEnderecoNFeEntrega() != null) {
//            infNFe.setEntrega(getNewEntrega(getSaidaProduto().getCliente().getEnderecoNFeEntrega()));
//        }

        List<Det> dets = new ArrayList<>();
        infNFe.getDet().addAll(dets);
        getNewListDet();

        infNFe.setTotal(new Total());
        getNewTotalNFe();

        infNFe.setTransp(new Transp());
        getNewTransp();

        infNFe.setCobr(new Cobr());
        getNewCobr();

        infNFe.setPag(new Pag());
        getNewPag();

        infNFe.setInfAdic(getNewInfAdic());

        //return infNFe;
    }

    /**
     * Grupo E. Identificação do Destinatário da Nota Fiscal eletrônica
     */
    private void getNewDest() {
        Dest dest = getTnFe().getInfNFe().getDest();

        Empresa destinatario = getSaidaProduto().getCliente();

        /**
         * nformar o CNPJ ou o CPF do destinatário, preenchendo os zeros não significativos.
         * No caso de operação com o exterior, ou para comprador estrangeiro informar a tag
         * "idEstrangeiro”.
         */
        if (destinatario.getClassifJuridica().equals(ClassificacaoJuridica.PESSOAJURIDICA))
            dest.setCNPJ(destinatario.getCnpj());
        else
            dest.setCPF(destinatario.getCnpj());

        /**
         * Informar esta tag no caso de operação com o exterior, ou para comprador estrangeiro.
         * Informar o número do passaporte ou outro documento legal para identificar pessoa estrangeira (campo aceita valor nulo).
         * Observação: Campo aceita algarismos, letras (maiúsculas e minúsculas) e os caracteres do conjunto que segue: [:.+-/()]
         */
        //dest.setIdEstrangeiro("");

        /**Razão Social ou nome do destinatário
         * Tag obrigatória para a NF-e (modelo 55) e opcional para a NFC-e.
         */

        String xNome = String.format("%s (%s)",
                destinatario.getRazao(),
                destinatario.getFantasia());
        if (TCONFIG.getNfe().getTpAmb() == 1)
            dest.setXNome(xNome.length() > 60
                    ? xNome.substring(0, 60)
                    : xNome);
        else
            dest.setXNome("NF-E EMITIDA EM AMBIENTE DE HOMOLOGACAO - SEM VALOR FISCAL");

        /**
         * Grupo obrigatório para a NF-e (modelo 55).
         */
        dest.setEnderDest(getNewEndereco(destinatario.getEnderecoNFe(), destinatario.getTelefone().getDescricao()));

//        if (destinatario.getEnderecoNFeEntrega() != null)
//            getTnFe().getInfNFe().setEntrega(getNewEntrega(destinatario.getEnderecoNFeEntrega()));

        /**Indicador da IE do Destinatário
         * 1=Contribuinte ICMS (informar a IE do destinatário);
         * 2=Contribuinte isento de Inscrição no cadastro de Contribuintes
         * 9=Não Contribuinte, que pode ou não possuir Inscrição Estadual no Cadastro de Contribuintes do ICMS.
         * Nota 1: No caso de NFC-e informar indIEDest=9 e não informar a tag IE do destinatário;
         * Nota 2: No caso de operação com o Exterior informar indIEDest=9 e não informar a tag IE do destinatário;
         * Nota 3: No caso de Contribuinte Isento de Inscrição (indIEDest=2), não informar a tag IE do destinatário.
         */
        dest.setIndIEDest(!getSaidaProduto().getCliente().getIe().equals("")
                ? "1"
                : (getSaidaProduto().getCliente().isIeIsento())
                ? "2" : "9");

        /**Inscrição Estadual do Destinatário
         * Campo opcional. Informar somente os algarismos, sem os caracteres de formatação (ponto, barra, hífen, etc.).
         */
        if (dest.getIndIEDest().equals("1"))
            dest.setIE(destinatario.getIe());
        if (dest.getIndIEDest().equals("9"))
            getTnFe().getInfNFe().getIde().setIndFinal("1");

        /**Inscrição na SUFRAMA
         * Obrigatório, nas operações que se beneficiam de incentivos fiscais existentes nas áreas sob controle da SUFRAMA.
         * A omissão desta informação impede o processamento da operação pelo Sistema de Mercadoria Nacional da SUFRAMA e a
         * liberação da Declaração de Ingresso, prejudicando a comprovação do ingresso / internamento da mercadoria nestas áreas. (v2.0)
         */
        //dest.setISUF("");

        /**Inscrição Municipal do Tomador do Serviço
         * Campo opcional, pode ser informado na NF-e conjugada, com itens de produtos sujeitos ao ICMS e itens de serviços sujeitos ao ISSQN.
         */
        //dest.setIM("");

        /**email
         * Campo pode ser utilizado para informar o e-mail de recepção da NF-e indicada pelo destinatário (v2.0)
         */
        dest.setEmail(destinatario.getEmailPrincipal());

        //return dest;
    }

    /**
     * Grupo G. Identificação do Local de Entrega
     */
    private TLocal getNewEntrega(Endereco endEnt) {
        TLocal tLocal = new TLocal();

        Empresa emp = getSaidaProduto().getCliente();

        /**Cnpj
         * Informar CNPJ ou CPF. Preencher os zeros não significativos. (v2.0)
         */
        tLocal.setCNPJ(emp.getCnpj());

        /**
         * Razão Social
         */
        tLocal.setXNome(emp.getRazao());

        /**
         * Logradouro
         */
        tLocal.setXNome(endEnt.getLogradouro());

        /**
         * Número
         */
        tLocal.setNro(endEnt.getNumero());

        /**
         * Complemento
         */
        tLocal.setXCpl(endEnt.getComplemento());

        /**
         * Bairro
         */
        tLocal.setXBairro(endEnt.getBairro());

        /**Código do município
         * Utilizar a Tabela do IBGE (Anexo IX - Tabela de UF, Município e País).
         */
        tLocal.setCMun(endEnt.getMunicipio().getIbge_codigo());

        /**Nome do Município
         * Informar ‘EXTERIOR ‘para operações com o exterior.
         */
        tLocal.setXMun(endEnt.getMunicipio().getDescricao());

        /**Sigla da UF
         * Informar ‘EX’ para operações com o exterior.
         */
        tLocal.setUF(TUf.valueOf(endEnt.getMunicipio().getUf().getSigla()));

        /**Código do CEP
         * Informar os zeros não significativos.
         */
        tLocal.setCEP(endEnt.getCep());

        /**Código do País
         * Utilizar a Tabela do BACEN (Anexo VII - Tabela de UF, Município e País).
         * 1058=Brasil
         */
        tLocal.setCPais("1058");

        /**Nome do País
         * Brasil ou BRASIL
         */
        tLocal.setXPais("BRASIL");

        /**Telefone
         * Preencher com o Código DDD + número do telefone.
         * Nas operações com exterior é permitido informar o código do país + código da localidade + número do telefone (v2.0)
         */
        tLocal.setFone(ServiceMascara.getTelefone(emp.getTelefone().getDescricao()));

        /**Endereço de e-mail do Recebedor
         */
        tLocal.setEmail(emp.getEmailPrincipal());

        return tLocal;
    }

    /**
     * Grupo H. Detalhamento de Produtos e Serviços da NF-e
     */
    private void getNewListDet() {
        List<Det> detList = getTnFe().getInfNFe().getDet();
        int item = 1;
        for (SaidaProdutoProduto saidaProdutoProduto : getSaidaProduto().getSaidaProdutoProdutoList()) {
            Det det = new Det();
            det.setNItem(String.valueOf(item++));

            det.setProd(getNewProd(saidaProdutoProduto));

            det.setImposto(getNewImposto(saidaProdutoProduto));

            detList.add(det);
        }
        //return detList;
    }

    /**
     * Grupo I. Produtos e Serviços da NF-e
     */
    private Prod getNewProd(SaidaProdutoProduto saidaProdutoProduto) {
        Prod prod = new Prod();
        Produto produto = saidaProdutoProduto.getProduto();

        /**Código do produto ou serviço
         * Preencher com CFOP, caso se trate de itens não relacionados com mercadorias/produtos e que o
         * contribuinte não possua codificação própria.Formato: ”CFOP9999”
         */
        prod.setCProd(produto.getCodigo());

        /**
         * Preencher com o código GTIN-8, GTIN-12, GTIN-13 ou GTIN-14 (antigos códigos EAN, UPC e DUN-14).
         * Para produtos que não possuem código de barras com GTIN, deve ser informado o literal “SEM GTIN”;
         */
        prod.setCEAN(produto.getProdutoCodigoBarraList().get(0) != null
                ? produto.getProdutoCodigoBarraList().get(0).getCodigoBarra()
                : "SEM GTIN");

        /**Descrição do produto ou serviço
         */
        prod.setXProd(produto.getDescricao());

        /**Código NCM com 8 dígitos
         * Obrigatória informação do NCM completo (8 dígitos). Nota: Em caso de item de serviço ou item que não
         * tenham produto (ex. transferência de crédito, crédito do ativo imobilizado, etc.), informar o valor 00 (dois zeros). (NT 2014/004)
         */
        prod.setNCM(produto.getNcm());

        /**Codificação NVE - Nomenclatura de Valor Aduaneiro e Estatística.
         * Codificação opcional que detalha alguns NCM.
         * Formato: duas letras maiúsculas e 4 algarismos.
         * Se a mercadoria se enquadrar em mais de uma codificação, informar até 8 codificações principais.
         * Vide: Anexo XII.03 - Identificador NVE.
         */
        //prod.getNVE().add("")

        /**Código CEST
         * Campo CEST (Código Especificador da Substituição Tributária), que estabelece a sistemática de uniformização e
         * identificação das mercadorias e bens passíveis de sujeição aos regimes de substituição tributária e de
         * antecipação de recolhimento do ICMS.
         */
        prod.setCEST(produto.getCest());

        /**CNPJ do Fabricante da Mercadoria
         * CNPJ do Fabricante da Mercadoria, obrigatório para produto em escala NÃO relevante.
         * (Incluído na NT 2016/002)
         */
        //prod.setCNPJFab("");

        /**Código de Benefício Fiscal na UF aplicado ao item
         * Código de Benefício Fiscal utilizado pela UF, aplicado ao item.
         * Obs.: Deve ser utilizado o mesmo código adotado na EFD e outras declarações, nas UF que o exigem.
         * (Incluído na NT 2016/002)
         */
        //prod.setCBenef("");

        /**EX_TIPI
         * Preencher de acordo com o código EX da TIPI. Em caso de serviço, não incluir a TAG
         */
        //prod.setEXTIPI("");

        /**Código Fiscal de Operações e Prestações
         * Utilizar Tabela de CFOP.
         * 1.000 – Entrada e/ou Aquisições de Serviços do Estado
         * 2.000 – Entrada e/ou Aquisições de Serviços de Outros Estados.
         * 3.000 – Entrada e/ou Aquisições de Serviços do Exterior.
         * 5.000 – Saídas ou Prestações de Serviços para o Estado.
         * 6.000 – Saídas ou Prestações de Serviços para Outros Estados
         * 7.000 – Saídas ou Prestações de Serviços para o Exterior.
         */
        switch (getSaidaProduto().getNfe().getDestOperacao()) {
            case INTERNA:
                prod.setCFOP("5102");
                break;
            case INTERESTADUAL:
                prod.setCFOP("6103");
                break;
            case EXTERIOR:
                prod.setCFOP("7102");
                break;
        }

        /**Unidade Comercial
         * Informar a unidade de comercialização do produto.
         */
        prod.setUCom(produto.getUnidadeComercial().getDescricao());

        /**Quantidade Comercial
         * Informar a quantidade de comercialização do produto (v2.0).
         */
        prod.setQCom(saidaProdutoProduto.getQtd() + ".0000");

        /**Valor Unitário de Comercialização
         * Informar o valor unitário de comercialização do produto, campo meramente informativo, o
         * contribuinte pode utilizar a precisão desejada (0-10 decimais).
         * Para efeitos de cálculo, o valor unitário será obtido pela divisão do valor do produto pela quantidade comercial. (v2.0)
         */
        prod.setVUnCom(saidaProdutoProduto.getVlrConsumidor().setScale(10).toString());

        /**Valor Total Bruto dos Produtos ou Serviços.
         * O valor do ICMS faz parte do Valor Total Bruto
         */
        prod.setVProd(saidaProdutoProduto.getVlrBruto().setScale(2).toString());

        /**GTIN (Global Trade Item Number) da unidade tributável, antigo código EAN ou código de barras
         * Preencher com o código GTIN-8, GTIN-12, GTIN-13 ou GTIN-14 (antigos códigos EAN, UPC e DUN-14)
         * da unidade tributável do produto.
         * O GTIN da unidade tributável deve corresponder àquele da menor unidade comercializável identificada por código GTIN.
         * Para produtos que não possuem código de barras com GTIN, deve ser informado o literal
         * "SEM GTIN”;
         */
        prod.setCEANTrib(produto.getProdutoCodigoBarraList().get(0) != null
                ? produto.getProdutoCodigoBarraList().get(0).getCodigoBarra()
                : "SEM GTIN");

        /**Unidade Tributável
         * Informar a unidade tributável do produto.
         */
        prod.setUTrib(produto.getUnidadeComercial().getDescricao());

        /**Quantidade Tributável
         * Informar a quantidade Tributável do produto (v2.0).
         */
        prod.setQTrib(saidaProdutoProduto.getQtd() + ".0000");

        /**Valor Unitário de tributação
         * Informar o valor unitário Tributável do produto, campo meramente informativo, o
         * contribuinte pode utilizar a precisão desejada (0-10 decimais).
         * Para efeitos de cálculo, o valor unitário será obtido pela divisão do valor do produto pela quantidade comercial. (v2.0)
         */
        prod.setVUnTrib(saidaProdutoProduto.getVlrConsumidor().setScale(10).toString());

        /**Valor Total do Frete
         */
        //prod.setVFrete("");

        /**Valor Total do Seguro
         */
        //prod.setVSeg("");

        /**Valor do Desconto
         */
        if (saidaProdutoProduto.getVlrDescontoLiquido().compareTo(BigDecimal.ZERO) > 0)
            prod.setVDesc(saidaProdutoProduto.getVlrDescontoLiquido().setScale(2).toString());

        /**Outras despesas acessórias
         */
        //prod.setVOutro("");

        /**Indica se valor do Item (vProd) entra no valor total da NF-e (vProd)
         * 0=Valor do item (vProd) não compõe o valor total da NF-e
         * 1=Valor do item (vProd) compõe o valor total da NF-e (vProd) (v2.0)
         */
        prod.setIndTot("1");

        return prod;
    }

    /**
     * Grupo M. Tributos incidentes no Produto ou Serviço
     */
    private Imposto getNewImposto(SaidaProdutoProduto saidaProdutoProduto) {
        Imposto imposto = new Imposto();

        ICMS icms = getNewIcms(saidaProdutoProduto);
        PIS pis = getNewPis(saidaProdutoProduto);
        COFINS cofins = getNewCofins(saidaProdutoProduto);
        imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoICMS(icms));
        imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoPIS(pis));
        imposto.getContent().add(new ObjectFactory().createTNFeInfNFeDetImpostoCOFINS(cofins));

        return imposto;
    }

    /**
     * Grupo N. ICMS Normal e ST
     */
    private ICMS getNewIcms(SaidaProdutoProduto saidaProdutoProduto) {
        ICMS icms = new ICMS();

        switch ((int) saidaProdutoProduto.getProduto().getFiscalIcms().getId()) {
            case 0:
                icms.setICMS00(getNewIcms00());
                break;
            case 10:
                icms.setICMS10(getNewIcms10());
                break;
            case 20:
                icms.setICMS20(getNewIcms20());
                break;
            case 30:
                icms.setICMS30(getNewIcms30());
                break;
            case 40:
            case 41:
            case 50:
                icms.setICMS40(getNewIcms40(String.valueOf(saidaProdutoProduto.getProduto().getFiscalIcms().getId())));
                break;
            case 51:
                icms.setICMS51(getNewIcms51());
                break;
            case 60:
                icms.setICMS60(getNewIcms60());
                break;
            case 70:
                icms.setICMS70(getNewIcms70());
                break;
            case 90:
                icms.setICMS90(getNewIcms90());
                break;
        }

        return icms;
    }

    /**
     * Grupo N. Grupo Tributação do ICMS= 00
     */
    private ICMS00 getNewIcms00() {
        ICMS00 icms00 = new ICMS00();

        /**Origem da mercadoria
         * 0 - Nacional, exceto as indicadas nos códigos 3, 4, 5 e 8;
         * 1 - Estrangeira - Importação direta, exceto a indicada no código 6;
         * 2 - Estrangeira - Adquirida no mercado interno, exceto a indicada no código 7;
         * 3 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 40% e inferior ou igual a 70%;
         * 4 - Nacional, cuja produção tenha sido feita em conformidade com os processos produtivos básicos de que tratam as legislações citadas nos Ajustes;
         * 5 - Nacional, mercadoria ou bem com Conteúdo de Importação inferior ou igual a 40%;
         * 6 - Estrangeira - Importação direta, sem similar nacional, constante em lista da CAMEX e gás natural;
         * 7 - Estrangeira - Adquirida no mercado interno, sem similar nacional, constante lista CAMEX e gás natural.
         * 8 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 70%;
         */
        icms00.setOrig("0");

        /**Tributação do ICMS = 00
         * 00=Tributada integralmente.
         */
        icms00.setCST("00");

        /**Modalidade de determinação da BC do ICMS
         * 0=Margem Valor Agregado (%);
         * 1=Pauta (Valor);
         * 2=Preço Tabelado Máx. (valor);
         * 3=Valor da operação.
         */
        icms00.setModBC("");

        /**Valor da BC do ICMS
         */
        icms00.setVBC("");

        /**Alíquota do imposto
         * Alíquota do ICMS sem o FCP. Quando for o caso, informar a alíquota do FCP no campo pFCP
         */
        icms00.setPICMS("");

        /**Valor do ICMS
         */
        icms00.setVICMS("");

        return icms00;
    }

    /**
     * Grupo N. Grupo Tributação do ICMS= 10
     */
    private ICMS10 getNewIcms10() {
        ICMS10 icms10 = new ICMS10();

        /**Origem da mercadoria
         * 0 - Nacional, exceto as indicadas nos códigos 3, 4, 5 e 8;
         * 1 - Estrangeira - Importação direta, exceto a indicada no código 6;
         * 2 - Estrangeira - Adquirida no mercado interno, exceto a indicada no código 7;
         * 3 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 40% e inferior ou igual a 70%;
         * 4 - Nacional, cuja produção tenha sido feita em conformidade com os processos produtivos básicos de que tratam as legislações citadas nos Ajustes;
         * 5 - Nacional, mercadoria ou bem com Conteúdo de Importação inferior ou igual a 40%;
         * 6 - Estrangeira - Importação direta, sem similar nacional, constante em lista da CAMEX e gás natural;
         * 7 - Estrangeira - Adquirida no mercado interno, sem similar nacional, constante lista CAMEX e gás natural.
         * 8 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 70%;
         */
        icms10.setOrig("0");

        /**Tributação do ICMS = 10
         * Tributada e com cobrança do ICMS por substituição tributária
         */
        icms10.setCST("10");

        /**Modalidade de determinação da BC do ICMS
         * 0=Margem Valor Agregado (%);
         * 1=Pauta (Valor);
         * 2=Preço Tabelado Máx. (valor);
         * 3=Valor da operação.
         */
        icms10.setModBC("");

        /**Valor da BC do ICMS
         */
        icms10.setVBC("");

        /**Alíquota do imposto
         * Alíquota do ICMS sem o FCP. Quando for o caso, informar a alíquota do FCP no campo pFCP
         */
        icms10.setPICMS("");

        /**Valor do ICMS
         */
        icms10.setVICMS("");

        /**Valor da Base de Cálculo do FCP
         * Informar o valor da Base de Cálculo do FCP
         */
        icms10.setVBCFCP("");

        /**Percentual do Fundo de Combate à Pobreza (FCP)
         * Percentual relativo ao Fundo de Combate à Pobreza (FCP).
         */
        icms10.setPFCP("");

        /**Valor do Fundo de Combate à Pobreza (FCP)
         * Valor do ICMS relativo ao Fundo de Combate à Pobreza (FCP).
         */
        icms10.setVFCP("");

        /**Modalidade de determinação da BC do ICMS ST
         * 0=Preço tabelado ou máximo sugerido;
         * 1=Lista Negativa (valor);
         * 2=Lista Positiva (valor);
         * 3=Lista Neutra (valor);
         * 4=Margem Valor Agregado (%);
         * 5=Pauta (valor);
         */
        icms10.setModBCST("");

        /**Percentual da margem de valor Adicionado do ICMS ST
         */
        icms10.setPMVAST("");

        /**Percentual da Redução de BC do ICMS ST
         */
        icms10.setPRedBCST("");

        /**Valor da BC do ICMS ST
         */
        icms10.setVBCST("");

        /**Alíquota do imposto do ICMS ST
         * Alíquota do ICMS ST sem o FCP. Quando for o caso, informar a alíquota do FCP no campo pFCP
         */
        icms10.setPICMSST("");

        /**Valor do ICMS ST
         * Valor do ICMS ST retido
         */
        icms10.setVICMSST("");

        return icms10;
    }

    /**
     * Grupo N. Grupo Tributação do ICMS= 20
     */
    private ICMS20 getNewIcms20() {
        ICMS20 icms20 = new ICMS20();

        /**Origem da mercadoria
         * 0 - Nacional, exceto as indicadas nos códigos 3, 4, 5 e 8;
         * 1 - Estrangeira - Importação direta, exceto a indicada no código 6;
         * 2 - Estrangeira - Adquirida no mercado interno, exceto a indicada no código 7;
         * 3 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 40% e inferior ou igual a 70%;
         * 4 - Nacional, cuja produção tenha sido feita em conformidade com os processos produtivos básicos de que tratam as legislações citadas nos Ajustes;
         * 5 - Nacional, mercadoria ou bem com Conteúdo de Importação inferior ou igual a 40%;
         * 6 - Estrangeira - Importação direta, sem similar nacional, constante em lista da CAMEX e gás natural;
         * 7 - Estrangeira - Adquirida no mercado interno, sem similar nacional, constante lista CAMEX e gás natural.
         * 8 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 70%;
         */
        icms20.setOrig("0");

        /**Tributação do ICMS = 20
         * Tributação com redução de base de cálculo
         */
        icms20.setCST("20");

        /**Modalidade de determinação da BC do ICMS
         * 0=Margem Valor Agregado (%);
         * 1=Pauta (Valor);
         * 2=Preço Tabelado Máx. (valor);
         * 3=Valor da operação.
         */
        icms20.setModBC("");

        /**Percentual da Redução de BC
         */
        icms20.setPRedBC("");

        /**Valor da BC do ICMS
         */
        icms20.setVBC("");

        /**Alíquota do imposto
         * Alíquota do ICMS sem o FCP. Quando for o caso, informar a alíquota do FCP no campo pFCP
         */
        icms20.setPICMS("");

        /**Valor do ICMS
         */
        icms20.setVICMS("");

        /**Valor da Base de Cálculo do FCP
         * Informar o valor da Base de Cálculo do FCP
         */
        icms20.setVBCFCP("");

        /**Percentual do Fundo de Combate à Pobreza (FCP)
         * Percentual relativo ao Fundo de Combate à Pobreza (FCP).
         */
        icms20.setPFCP("");

        /**Valor do Fundo de Combate à Pobreza (FCP)
         * Valor do ICMS relativo ao Fundo de Combate à Pobreza (FCP).
         */
        icms20.setVFCP("");

        /**Valor do ICMS desonerado
         * Informar apenas nos motivos de desoneração documentados abaixo.
         */
        icms20.setVICMSDeson("");

        /**Motivo da desoneração do ICMS
         * Campo será preenchido quando o campo anterior estiver preenchido.
         * Informar o motivo da desoneração: 3=Uso na agropecuária;
         * 9=Outros;
         * 12=Órgão de fomento e desenvolvimento agropecuário.
         */
        icms20.setMotDesICMS("");

        return icms20;
    }

    /**
     * Grupo N. Grupo Tributação do ICMS= 30
     */
    private ICMS30 getNewIcms30() {
        ICMS30 icms30 = new ICMS30();

        /**Origem da mercadoria
         * 0 - Nacional, exceto as indicadas nos códigos 3, 4, 5 e 8;
         * 1 - Estrangeira - Importação direta, exceto a indicada no código 6;
         * 2 - Estrangeira - Adquirida no mercado interno, exceto a indicada no código 7;
         * 3 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 40% e inferior ou igual a 70%;
         * 4 - Nacional, cuja produção tenha sido feita em conformidade com os processos produtivos básicos de que tratam as legislações citadas nos Ajustes;
         * 5 - Nacional, mercadoria ou bem com Conteúdo de Importação inferior ou igual a 40%;
         * 6 - Estrangeira - Importação direta, sem similar nacional, constante em lista da CAMEX e gás natural;
         * 7 - Estrangeira - Adquirida no mercado interno, sem similar nacional, constante lista CAMEX e gás natural.
         * 8 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 70%;
         */
        icms30.setOrig("0");

        /**Tributação do ICMS = 30
         * 30=Isenta ou não tributada e com cobrança do ICMS por substituição tributária
         */
        icms30.setCST("30");

        /**Modalidade de determinação da BC do ICMS ST
         * 0=Preço tabelado ou máximo sugerido;
         * 1=Lista Negativa (valor);
         * 2=Lista Positiva (valor);
         * 3=Lista Neutra (valor);
         * 4=Margem Valor Agregado (%);
         * 5=Pauta (valor);
         */
        icms30.setModBCST("");

        /**Percentual da margem de valor Adicionado do ICMS ST
         */
        icms30.setPMVAST("");

        /**Percentual da Redução de BC do ICMS ST
         */
        icms30.setPRedBCST("");

        /**Valor da BC do ICMS ST
         */
        icms30.setVBCST("");

        /**Alíquota do imposto do ICMS ST
         * Alíquota do ICMS ST sem o FCP. Quando for o caso, informar a alíquota do FCP no campo pFCP
         */
        icms30.setPICMSST("");

        /**Valor do ICMS ST
         * Valor do ICMS ST retido
         */
        icms30.setVICMSST("");

        /**Valor da Base de Cálculo do FCP
         * Informar o valor da Base de Cálculo do FCP retido por Substituição Tributária
         */
        icms30.setVBCFCPST("");

        /**Percentual do FCP retido por Substituição Tributária
         * Percentual relativo ao Fundo de Combate à Pobreza (FCP) retido por substituição tributária.
         */
        icms30.setPFCPST("");

        /**Valor do FCP retido por Substituição Tributária
         * Valor do ICMS relativo ao Fundo de Combate à Pobreza (FCP) retido por substituição tributária.
         */
        icms30.setVFCPST("");

        /**Valor do ICMS desonerado
         *Informar apenas nos motivos de desoneração documentados abaixo.
         */
        icms30.setVICMSDeson("");

        /**Motivo da desoneração do ICMS
         * Campo será preenchido quando o campo anterior estiver preenchido. Informar o motivo da desoneração: 6=Utilitários e Motocicletas da Amazônia Ocidental e Áreas de Livre Comércio (Resolução 714/88 e 790/94 – CONTRAN e suas alterações);
         * 7=SUFRAMA;
         * 9=Outros;
         */
        icms30.setMotDesICMS("");

        return icms30;
    }

    /**
     * Grupo N. Grupo Tributação do ICMS= 40, 41, 50
     */
    private ICMS40 getNewIcms40(String cst) {
        ICMS40 icms40 = new ICMS40();

        /**Origem da mercadoria
         * 0 - Nacional, exceto as indicadas nos códigos 3, 4, 5 e 8;
         * 1 - Estrangeira - Importação direta, exceto a indicada no código 6;
         * 2 - Estrangeira - Adquirida no mercado interno, exceto a indicada no código 7;
         * 3 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 40% e inferior ou igual a 70%;
         * 4 - Nacional, cuja produção tenha sido feita em conformidade com os processos produtivos básicos de que tratam as legislações citadas nos Ajustes;
         * 5 - Nacional, mercadoria ou bem com Conteúdo de Importação inferior ou igual a 40%;
         * 6 - Estrangeira - Importação direta, sem similar nacional, constante em lista da CAMEX e gás natural;
         * 7 - Estrangeira - Adquirida no mercado interno, sem similar nacional, constante lista CAMEX e gás natural.
         * 8 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 70%;
         */
        icms40.setOrig("0");

        /**Tributação do ICMS = 40, 41 ou 50
         * 40=Isenta; 41=Não tributada; 50=Suspensão.
         */
        icms40.setCST(cst);


        /**Valor do ICMS
         * Informar nas operações:
         * a) com produtos beneficiados com a desoneração condicional do ICMS.
         * b) destinadas à SUFRAMA, informando-se o valor que seria devido se não houvesse isenção.
         * c) de venda a órgão da administração pública direta e suas fundações e autarquias com isenção do ICMS. (NT 2011/004
         * d) demais casos solicitados pelo Fisco.
         */
        icms40.setVICMSDeson("");

        /**Motivo da desoneração do ICMS
         * Campo será preenchido quando o campo anterior estiver preenchido. Informar o motivo da desoneração:
         * 1=Táxi;
         * 3=Produtor Agropecuário;
         * 4=Frotista/Locadora;
         * 5=Diplomático/Consular;
         * 6=Utilitários e Motocicletas da Amazônia Ocidental e Áreas de Livre Comércio (Resolução 714/88 e 790/94 – CONTRAN e suas alterações);
         * 7=SUFRAMA;
         * 8=Venda a Órgão Público;
         * 9=Outros. (NT 2011/004);
         * 10=Deficiente Condutor (Convênio ICMS 38/12);
         * 11=Deficiente Não Condutor (Convênio ICMS 38/12).
         * 16=Olimpíadas Rio 2016 (NT 2015.002); 90=Solicitado pelo Fisco
         * Revogada a partir da versão a possibilidade de usar o motivo
         * 2=Deficiente Físico
         */
        icms40.setMotDesICMS("");

        return icms40;
    }

    /**
     * Grupo N. Grupo Tributação do ICMS= 51
     */
    private ICMS51 getNewIcms51() {
        ICMS51 icms51 = new ICMS51();

        /**Origem da mercadoria
         * 0 - Nacional, exceto as indicadas nos códigos 3, 4, 5 e 8;
         * 1 - Estrangeira - Importação direta, exceto a indicada no código 6;
         * 2 - Estrangeira - Adquirida no mercado interno, exceto a indicada no código 7;
         * 3 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 40% e inferior ou igual a 70%;
         * 4 - Nacional, cuja produção tenha sido feita em conformidade com os processos produtivos básicos de que tratam as legislações citadas nos Ajustes;
         * 5 - Nacional, mercadoria ou bem com Conteúdo de Importação inferior ou igual a 40%;
         * 6 - Estrangeira - Importação direta, sem similar nacional, constante em lista da CAMEX e gás natural;
         * 7 - Estrangeira - Adquirida no mercado interno, sem similar nacional, constante lista CAMEX e gás natural.
         * 8 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 70%;
         */
        icms51.setOrig("0");

        /**Tributação do ICMS = 51
         * 51=Diferimento
         */
        icms51.setCST("51");

        /**Modalidade de determinação da BC do ICMS
         * 0=Margem Valor Agregado (%);
         * 1=Pauta (Valor);
         * 2=Preço Tabelado Máx. (valor);
         * 3=Valor da operação.
         */
        icms51.setModBC("");

        /**Percentual da Redução de BC
         */
        icms51.setPRedBC("");

        /**Valor da BC do ICMS
         */
        icms51.setVBC("");

        /**Alíquota do imposto
         * Alíquota do ICMS sem o FCP. Quando for o caso, informar a alíquota do FCP no campo pFCP (Atualizado
         */
        icms51.setPICMS("");

        /**Valor do ICMS da Operação
         * Valor como se não tivesse o diferimento
         */
        icms51.setVICMSOp("");

        /**Percentual do diferimento
         * No caso de diferimento total, informar o percentual de diferimento "100".
         */
        icms51.setPDif("");

        /**Valor do ICMS diferido
         */
        icms51.setVICMSDif("");

        /**Valor do ICMS
         * Informar o valor realmente devido.
         */
        icms51.setVICMS("");

        /**Valor da Base de Cálculo do FCP
         * Informar o valor da Base de Cálculo do FCP
         */
        icms51.setVBCFCP("");

        /**Percentual do ICMS relativo ao Fundo de Combate à Pobreza (FCP)
         * Percentual relativo ao Fundo de Combate à Pobreza (FCP).
         */
        icms51.setPFCP("");

        /**Valor do ICMS relativo ao Fundo de Combate à Pobreza (FCP)
         * Valor do ICMS relativo ao Fundo de Combate à Pobreza (FCP)
         */
        icms51.setVFCP("");

        return icms51;
    }

    /**
     * Grupo N. Grupo Tributação do ICMS= 60
     */
    private ICMS60 getNewIcms60() {
        ICMS60 icms60 = new ICMS60();

        /**Origem da mercadoria
         * 0 - Nacional, exceto as indicadas nos códigos 3, 4, 5 e 8;
         * 1 - Estrangeira - Importação direta, exceto a indicada no código 6;
         * 2 - Estrangeira - Adquirida no mercado interno, exceto a indicada no código 7;
         * 3 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 40% e inferior ou igual a 70%;
         * 4 - Nacional, cuja produção tenha sido feita em conformidade com os processos produtivos básicos de que tratam as legislações citadas nos Ajustes;
         * 5 - Nacional, mercadoria ou bem com Conteúdo de Importação inferior ou igual a 40%;
         * 6 - Estrangeira - Importação direta, sem similar nacional, constante em lista da CAMEX e gás natural;
         * 7 - Estrangeira - Adquirida no mercado interno, sem similar nacional, constante lista CAMEX e gás natural.
         * 8 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 70%;
         */
        icms60.setOrig("0");

        /**Tributação do ICMS = 60
         * 60=ICMS cobrado anteriormente por substituição tributária
         */
        icms60.setCST("60");

        return icms60;
    }

    /**
     * Grupo N. Grupo Tributação do ICMS= 70
     */
    private ICMS70 getNewIcms70() {
        ICMS70 icms70 = new ICMS70();

        /**Origem da mercadoria
         * 0 - Nacional, exceto as indicadas nos códigos 3, 4, 5 e 8;
         * 1 - Estrangeira - Importação direta, exceto a indicada no código 6;
         * 2 - Estrangeira - Adquirida no mercado interno, exceto a indicada no código 7;
         * 3 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 40% e inferior ou igual a 70%;
         * 4 - Nacional, cuja produção tenha sido feita em conformidade com os processos produtivos básicos de que tratam as legislações citadas nos Ajustes;
         * 5 - Nacional, mercadoria ou bem com Conteúdo de Importação inferior ou igual a 40%;
         * 6 - Estrangeira - Importação direta, sem similar nacional, constante em lista da CAMEX e gás natural;
         * 7 - Estrangeira - Adquirida no mercado interno, sem similar nacional, constante lista CAMEX e gás natural.
         * 8 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 70%;
         */
        icms70.setOrig("0");

        /**Tributação do ICMS = 70
         * 70=Com redução de base de cálculo e cobrança do ICMS por substituição tributária
         */
        icms70.setCST("70");

        /**Modalidade de determinação da BC do ICMS
         * 0=Margem Valor Agregado (%);
         * 1=Pauta (Valor);
         * 2=Preço Tabelado Máx. (valor);
         * 3=Valor da operação.
         */
        icms70.setModBC("");

        /**Percentual da Redução de BC
         */
        icms70.setPRedBC("");

        /**Valor da BC do ICMS
         */
        icms70.setVBC("");

        /**Alíquota do imposto
         * Alíquota do ICMS sem o FCP. Quando for o caso, informar a alíquota do FCP no campo pFCP.
         */
        icms70.setPICMS("");

        /**Valor do ICMS
         */
        icms70.setVICMS("");

        return icms70;
    }

    /**
     * Grupo N. Grupo Tributação do ICMS= 90
     */
    private ICMS90 getNewIcms90() {
        ICMS90 icms90 = new ICMS90();

        /**Origem da mercadoria
         * 0 - Nacional, exceto as indicadas nos códigos 3, 4, 5 e 8;
         * 1 - Estrangeira - Importação direta, exceto a indicada no código 6;
         * 2 - Estrangeira - Adquirida no mercado interno, exceto a indicada no código 7;
         * 3 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 40% e inferior ou igual a 70%;
         * 4 - Nacional, cuja produção tenha sido feita em conformidade com os processos produtivos básicos de que tratam as legislações citadas nos Ajustes;
         * 5 - Nacional, mercadoria ou bem com Conteúdo de Importação inferior ou igual a 40%;
         * 6 - Estrangeira - Importação direta, sem similar nacional, constante em lista da CAMEX e gás natural;
         * 7 - Estrangeira - Adquirida no mercado interno, sem similar nacional, constante lista CAMEX e gás natural.
         * 8 - Nacional, mercadoria ou bem com Conteúdo de Importação superior a 70%;
         */
        icms90.setOrig("0");

        /**Tributação do ICMS = 90
         * 90=Outros
         */
        icms90.setCST("90");

        return icms90;
    }

    /**
     * Grupo Q. PIS
     */
    private PIS getNewPis(SaidaProdutoProduto saidaProdutoProduto) {
        PIS pis = new PIS();

        switch ((int) saidaProdutoProduto.getProduto().getFiscalPis().getId()) {
            case 6:
                pis.setPISNT(getNewPisNT((int) saidaProdutoProduto.getProduto().getFiscalPis().getId()));
                break;
        }
        return pis;
    }

    private PISNT getNewPisNT(int cst) {
        PISNT pisnt = new PISNT();

        /**Código de Situação Tributária do PIS
         * 01=Operação Tributável (base de cálculo = valor da operação alíquota normal (cumulativo/não cumulativo));
         * 02=Operação Tributável (base de cálculo = valor da operação (alíquota diferenciada));
         * 03=Operação Tributável (base de cálculo = quantidade vendida x alíquota por unidade de produto);
         * 04=Operação Tributável (tributação monofásica (alíquota zero));
         * 05=Operação Tributável (Substituição Tributária);
         * 06=Operação Tributável (alíquota zero);
         * 07=Operação Isenta da Contribuição;
         * 08=Operação Sem Incidência da Contribuição;
         * 09=Operação com Suspensão da Contribuição;
         */
        pisnt.setCST(String.format("%02d", cst));

        return pisnt;
    }

    /**
     * Grupo S. COFINS
     */
    private COFINS getNewCofins(SaidaProdutoProduto saidaProdutoProduto) {
        COFINS cofins = new COFINS();

        switch ((int) saidaProdutoProduto.getProduto().getFiscalCofins().getId()) {
            case 6:
                cofins.setCOFINSNT(getNewCofinsNT((int) saidaProdutoProduto.getProduto().getFiscalCofins().getId()));
                break;
        }

        return cofins;
    }

    private COFINSNT getNewCofinsNT(int cst) {
        COFINSNT cofinsnt = new COFINSNT();

        /**Código de Situação Tributária do PIS
         * 01=Operação Tributável (base de cálculo = valor da operação alíquota normal (cumulativo/não cumulativo));
         * 02=Operação Tributável (base de cálculo = valor da operação (alíquota diferenciada));
         * 03=Operação Tributável (base de cálculo = quantidade vendida x alíquota por unidade de produto);
         * 04=Operação Tributável (tributação monofásica (alíquota zero));
         * 05=Operação Tributável (Substituição Tributária);
         * 06=Operação Tributável (alíquota zero);
         * 07=Operação Isenta da Contribuição;
         * 08=Operação Sem Incidência da Contribuição;
         * 09=Operação com Suspensão da Contribuição;
         */
        cofinsnt.setCST(String.format("%02d", cst));

        return cofinsnt;
    }

    /**
     * Grupo W. Total da NF-e
     */
    private void getNewTotalNFe() {
        Total tot = getTnFe().getInfNFe().getTotal();

        tot.setICMSTot(getNewICMSTotal());

        //return tot;
    }

    /**
     * Grupo Totais referentes ao ICMS
     */
    private ICMSTot getNewICMSTotal() {
        ICMSTot icmsTot = new ICMSTot();

        /**Base de Cálculo do ICMS
         */
        icmsTot.setVBC(BigDecimal.ZERO.setScale(2).toString());

        /**Valor Total do ICMS
         */
        icmsTot.setVICMS(BigDecimal.ZERO.setScale(2).toString());

        /**Valor Total do ICMS desonerado
         */
        icmsTot.setVICMSDeson(BigDecimal.ZERO.setScale(2).toString());

        /**Valor total do ICMS relativo Fundo de Combate à Pobreza (FCP) da UF de destino
         * Valor total do ICMS relativo ao Fundo de Combate à Pobreza (FCP) para a UF de destino.
         *  (Incluído na NT 2015/003)
         */
        icmsTot.setVFCPUFDest(BigDecimal.ZERO.setScale(2).toString());

        /**Valor total do ICMS Interestadual para a UF de destino
         * Valor total do ICMS Interestadual para a UF de destino, já considerando o valor do ICMS relativo ao Fundo de Combate à Pobreza naquela UF.
         * (Incluído na NT 2015/003)
         */
        icmsTot.setVICMSUFDest(BigDecimal.ZERO.setScale(2).toString());

        /**Valor total do ICMS Interestadual para a UF do remetente
         * Valor total do ICMS Interestadual para a UF do remetente. Nota: A partir de 2019, este valor será zero.
         * (Incluído na NT 2015/003)
         */
        icmsTot.setVICMSUFRemet(BigDecimal.ZERO.setScale(2).toString());

        /**Valor Total do FCP (Fundo de Combate à Pobreza)
         * Corresponde ao total da soma dos campos id: N17c (Incluído na NT 2016/002)
         */
        icmsTot.setVFCP(BigDecimal.ZERO.setScale(2).toString());

        /**Base de Cálculo do ICMS ST
         */
        icmsTot.setVBCST(BigDecimal.ZERO.setScale(2).toString());

        /**Valor Total do ICMS ST
         */
        icmsTot.setVST(BigDecimal.ZERO.setScale(2).toString());

        /**Valor Total do FCP (Fundo de Combate à
         Pobreza) retido por substituição tributária
         * Corresponde ao total da soma dos campos id:N23d
         * (Incluído na NT 2016/002)
         */
        icmsTot.setVFCPST(BigDecimal.ZERO.setScale(2).toString());

        /**Valor Total do FCP retido anteriormente por Substituição Tributária
         * Corresponde ao total da soma dos campos id:N27d
         * (Incluído na NT 2016/002)
         */
        icmsTot.setVFCPSTRet(BigDecimal.ZERO.setScale(2).toString());

        /**Valor Total dos produtos e serviços
         */
        icmsTot.setVProd(getSaidaProduto().getSaidaProdutoProdutoList()
                .stream().map(SaidaProdutoProduto::getVlrBruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2).toString()
        );

        /**Valor Total do Frete
         */
        icmsTot.setVFrete(BigDecimal.ZERO.setScale(2).toString());

        /**Valor Total do Seguro
         */
        icmsTot.setVSeg(BigDecimal.ZERO.setScale(2).toString());

        /**Valor Total do Desconto
         */
        icmsTot.setVDesc(getSaidaProduto().getSaidaProdutoProdutoList()
                .stream().map(SaidaProdutoProduto::getVlrDescontoLiquido)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2).toString());

        /**Valor Total do II
         */
        icmsTot.setVII(BigDecimal.ZERO.setScale(2).toString());

        /**Valor Total do IPI
         */
        icmsTot.setVIPI(BigDecimal.ZERO.setScale(2).toString());

        /**Valor Total do IPI devolvido
         * Deve ser informado quando preenchido o Grupo Tributos Devolvidos na emissão de nota finNFe=4
         * (devolução) nas operações com não contribuintes do IPI. Corresponde ao total da soma dos campos id:UA04.
         * (Incluído na NT 2016/002)
         */
        icmsTot.setVIPIDevol(BigDecimal.ZERO.setScale(2).toString());

        /**Valor do PIS
         */
        icmsTot.setVPIS(BigDecimal.ZERO.setScale(2).toString());

        /**Valor da COFINS
         */
        icmsTot.setVCOFINS(BigDecimal.ZERO.setScale(2).toString());

        /**Outras Despesas acessórias
         */
        icmsTot.setVOutro(BigDecimal.ZERO.setScale(2).toString());

        /**Valor Total da NF-e
         * Vide validação para este campo na regra de validação "W16-xx".
         */
        icmsTot.setVNF(getSaidaProduto().getSaidaProdutoProdutoList()
                .stream().map(SaidaProdutoProduto::getVlrLiquido)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2).toString());

        /**Valor aproximado total de tributos federais, estaduais e municipais.
         * (NT 2013/003)
         */
        icmsTot.setVTotTrib(BigDecimal.ZERO.setScale(2).toString());

        return icmsTot;
    }

    /**
     * Grupo X. Informações do Transporte da NF-e
     */
    private void getNewTransp() {
        Transp transp = getTnFe().getInfNFe().getTransp();

        /**Modalidade do frete
         * 0=Contratação do Frete por conta do Remetente (CIF);
         * 1=Contratação do Frete por conta do Destinatário (FOB);
         * 2=Contratação do Frete por conta de Terceiros;
         * 3=Transporte Próprio por conta do Remetente;
         * 4=Transporte Próprio por conta do Destinatário;
         * 9=Sem Ocorrência de Transporte.
         */
        switch (getSaidaProduto().getNfe().getDestOperacao()) {
            case INTERNA:
                transp.setModFrete("3");
                break;
            case INTERESTADUAL:
            case EXTERIOR:
                transp.setModFrete("1");
                break;
        }

        if (getSaidaProduto().getNfe().getTransportador() != null) {
            transp.setTransporta(getNewTransporta());
        }

        //return transp;
    }

    private Transporta getNewTransporta() {
        Transporta transporta = new Transporta();

        /**CNPJ do Transportador
         */
        transporta.setCNPJ(getSaidaProduto().getNfe().getTransportador().getCnpj());

        /**Razão Social ou nome
         */
        transporta.setXNome(getSaidaProduto().getNfe().getTransportador().getRazao());

        /**Inscrição Estadual do Transportador
         */
        if (getSaidaProduto().getNfe().getTransportador().isIeIsento())
            transporta.setIE("ISENTO");
        else
            transporta.setIE(getSaidaProduto().getNfe().getTransportador().getIe());

        /**Endereço Completo
         */
        transporta.setXEnder(getSaidaProduto().getNfe().getTransportador().getEnderecoPrincipal());

        /**Nome do município
         */
        transporta.setXMun(getSaidaProduto().getNfe().getTransportador().getEnderecoList()
                .stream().filter(endereco -> endereco.getTipo().equals(EnderecoTipo.PRINCIPAL))
                .findFirst().orElse(null).getMunicipio().getDescricao());

        /**Sigla da UF
         */
        transporta.setUF(TUf.valueOf(getSaidaProduto().getNfe().getTransportador().getEnderecoList()
                .stream().filter(endereco -> endereco.getTipo().equals(EnderecoTipo.PRINCIPAL))
                .findFirst().orElse(null).getMunicipio().getUf().getSigla()));

        return transporta;
    }

    /**
     * Grupo Y. Dados da Cobrança
     */
    private void getNewCobr() {
        Cobr cobr = getTnFe().getInfNFe().getCobr();

        cobr.setFat(getNewFat());

        if (getSaidaProduto().getContasAReceber() != null) {
            cobr.getDup().add(getNewDup());
        } else {
            Dup dup = new Dup();

            /**Número da Parcela
             */
            dup.setNDup("001");

            /**Data de vencimento
             */
            dup.setDVenc(getSaidaProduto().getNfe().getDataHoraSaida().format(DTF_MYSQL_DATA));

            /**Valor da Parcela
             */
            dup.setVDup(getSaidaProduto().getSaidaProdutoProdutoList()
                    .stream().map(SaidaProdutoProduto::getVlrLiquido)
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2).toString());
            cobr.getDup().add(dup);
        }
    }

    private Fat getNewFat() {
        Fat fat = new Fat();

        /**Número da Fatura
         */
        fat.setNFat(getSaidaProduto().getNfe().getCobrancaNumero());

        /**Valor Original da Fatura
         */
        fat.setVOrig(getSaidaProduto().getSaidaProdutoProdutoList().stream()
                .map(SaidaProdutoProduto::getVlrBruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2).toString());

        /**Valor do desconto
         */
        fat.setVDesc(getSaidaProduto().getSaidaProdutoProdutoList().stream()
                .map(SaidaProdutoProduto::getVlrDescontoLiquido)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2).toString());

        /**Valor Líquido da Fatura
         */
        fat.setVLiq(getSaidaProduto().getSaidaProdutoProdutoList().stream()
                .map(SaidaProdutoProduto::getVlrLiquido)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2).toString());

        return fat;
    }

    private Dup getNewDup() {
        Dup dup = new Dup();

        /**Número da Parcela
         */
        dup.setNDup(String.format("001 - [%s]", getSaidaProduto().getNfe().getCobrancaNumero()));

        /**Data de vencimento
         */
        dup.setDVenc(getSaidaProduto().getContasAReceber().getDtVencimento().format(DTF_MYSQL_DATA));

        /**Valor da Parcela
         */
        dup.setVDup(getSaidaProduto().getSaidaProdutoProdutoList()
                .stream().map(SaidaProdutoProduto::getVlrLiquido)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2).toString());

        return dup;
    }

    /**
     * Grupo YA. Informações de Pagamento
     */
    private void getNewPag() {
        Pag pag = getTnFe().getInfNFe().getPag();

        /**Grupo Detalhamento do G YA01 1-100 Pagamento
         */
        pag.getDetPag().add(getNewDetPag());

        //return pag;
    }

    private Pag.DetPag getNewDetPag() {
        Pag.DetPag detPag = new Pag.DetPag();

        /**Indicador da Forma de Pagamento
         * 0= Pagamento à Vista
         * 1= Pagamento à Prazo
         *  (Incluído na NT 2016/002
         */
        detPag.setIndPag("1");

        /**Meio de pagamento
         * 01=Dinheiro
         * 02=Cheque
         * 03=Cartão de Crédito
         * 04=Cartão de Débito
         * 05=Crédito Loja
         * 10=Vale Alimentação
         * 11=Vale Refeição
         * 12=Vale Presente
         * 13=Vale Combustível
         * 15=Boleto Bancário
         * 90= Sem pagamento
         * 99=Outros
         */
        detPag.setTPag("99");

        /**Valor do Pagamento
         */
        detPag.setVPag(getSaidaProduto().getSaidaProdutoProdutoList().stream()
                .map(SaidaProdutoProduto::getVlrLiquido)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2).toString());

        return detPag;
    }


    /**
     * Grupo Z. Informações Adicionais da NF-e
     */
    private InfAdic getNewInfAdic() {
        if (getSaidaProduto().getNfe().getInformacaoAdicional() != null &&
                !getSaidaProduto().getNfe().getInformacaoAdicional().equals("")) {
            InfAdic infAdic = new InfAdic();
            infAdic.setInfCpl(getSaidaProduto().getNfe().getInformacaoAdicional());
            return infAdic;
        }
        return null;
    }

    private static String strValueOf(TEnviNFe tEnviNFe) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(TNFe.class);
        Marshaller marshaller = context.createMarshaller();
        JAXBElement<TEnviNFe> element = new ObjectFactory().createEnviNFe(tEnviNFe);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        marshaller.marshal(element, sw);

        String xml = sw.toString();
        xml = xml.replaceAll("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\" ", "");
        xml = xml.replaceAll("<NFe>", "<NFe xmlns=\"http://www.portalfiscal.inf.br/nfe\">");

        return xml;
    }

    private static void error(String error) {
        System.out.println("| ERROR: " + error);
    }

    private static void info(String info) {
        System.out.println("| INFO: " + info.toString());
    }

    public TNFe getTnFe() {
        return tnFe;
    }

    public void setTnFe(TNFe tnFe) {
        this.tnFe = tnFe;
    }

    public SaidaProduto getSaidaProduto() {
        return saidaProduto;
    }

    public void setSaidaProduto(SaidaProduto saidaProduto) {
        this.saidaProduto = saidaProduto;
    }

    public TEnviNFe gettEnviNFe() {
        return tEnviNFe;
    }

    public void settEnviNFe(TNFe tnNFe) {
        this.tEnviNFe = new EnviNFe(tnNFe).gettEnviNFe();
    }
}
