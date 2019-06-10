import br.com.tlmacedo.cafeperfeito.model.dao.TelefoneOperadoraDAO;
import br.com.tlmacedo.cafeperfeito.model.vo.TelefoneOperadora;
import br.com.tlmacedo.cafeperfeito.nfe.v400.ServiceAssinarXml;
import br.com.tlmacedo.cafeperfeito.service.ServiceBuscaWebService;
import br.com.tlmacedo.cafeperfeito.service.ServiceVariaveisSistema;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class Testes {
    private static Document documentFactory(String xml) throws SAXException,
            IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document document = factory.newDocumentBuilder().parse(
                new ByteArrayInputStream(xml.getBytes()));
        return document;
    }

    private static String lerXML(String fileXML) throws IOException {
        String linha = "";
        StringBuilder xml = new StringBuilder();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileXML)));
        while ((linha = in.readLine()) != null) {
            xml.append(linha);
        }
        in.close();

        return xml.toString();
    }

    public static void main(String... args) throws Exception {
        new ServiceVariaveisSistema().getVariaveisSistemaBasica();

        String fileEnviNFe = "/Volumes/150GB-Development/cafeperfeito/cafeperfeito/src/main/resources/xml/nfe/out/NFe13190608009246000136550010000008671201906058.xml";
        String xmlEnviNFe = lerXML(fileEnviNFe);
        new ServiceAssinarXml(xmlEnviNFe);
        Document doc = documentFactory(xmlEnviNFe);

//            for (int i = 0; i < document.getDocumentElement().getElementsByTagName("NFe").getLength(); i++) {
//
////            assinarNFe(signatureFactory, transformList, privateKey, keyInfo, document, 1);
//            }

        try {
//            File inputFile = new File("/Volumes/150GB-Development/cafeperfeito/cafeperfeito/src/main/resources/xml/_nfe1.xml");
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("NFe");
            System.out.println("----------------------------");
            System.out.printf("qtd: [%d]\n", doc.getElementsByTagName("NFe").getLength());

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                for (int i = 0; i < nNode.getChildNodes().getLength(); i++) {
                    System.out.printf("child(%d): [%s]\n", i, nNode.getChildNodes().item(i).getNodeName());
                }

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.printf("eElement: [%s]\n", eElement.getTagName());
//                    System.out.println("Student roll no : "
//                            + eElement.getAttribute("rollno"));
//                    System.out.println("First Name : "
//                            + eElement
//                            .getElementsByTagName("firstname")
//                            .item(0)
//                            .getTextContent());
//                    System.out.println("Last Name : "
//                            + eElement
//                            .getElementsByTagName("lastname")
//                            .item(0)
//                            .getTextContent());
//                    System.out.println("Nick Name : "
//                            + eElement
//                            .getElementsByTagName("nickname")
//                            .item(0)
//                            .getTextContent());
//                    System.out.println("Marks : "
//                            + eElement
//                            .getElementsByTagName("marks")
//                            .item(0)
//                            .getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//        String fileEnviNFe = "/Volumes/150GB-Development/cafeperfeito/cafeperfeito/src/main/resources/xml/_nfe1.xml";
//        String xmlEnviNFe = lerXML(fileEnviNFe);

//        //System.out.printf("return: [%s]\n", LocalDateTime.now().format(DTF_NFE_TO_LOCAL_DATE));
//        System.out.printf("%s\n", ZonedDateTime.of(LocalDateTime.now(), MY_ZONE_TIME).toString());
//        System.out.printf("%s\n", ZonedDateTime.now(MY_ZONE_TIME).format(DTF_NFE_TO_LOCAL_DATE));
//

//        String cUF = String.valueOf(TCONFIG.getNfe().getCUF());
//        System.out.printf("cUF: [%s]\n", cUF);
//        String aaMM = String.format("%02d%02d", LocalDateTime.now().getYear() % 100, LocalDateTime.now().getMonthValue());
//        System.out.printf("aaMM: [%s]\n", aaMM);
//        String cnpj = TCONFIG.getNfe().getCnpj();
//
//        int dV = ServiceValidarDado.nfeDv("5206043300991100250655012000000780026730161");
//        System.out.printf("Dv: [%d]\n", dV);
//
//        System.out.printf("Dv: [%d]\n", ServiceValidarDado.nfeDv("3519030569424800011355001000018992119860132"));
//
//        System.out.printf("Dv: [%d]\n", ServiceValidarDado.nfeDv("3519010698183300024855001000000134114788237"));


//        try {
////            String json =
////                    "EntradaProduto" +
////                            "{ " +
////                            "id=0, " +
////                            "situacao=0, " +
////                            "loja=T. L. MACEDO (CAFE PERFEITO), " +
////                            "entradaNfe=EntradaNfe" +
////                            "{" +
////                            "id=0, " +
////                            "chave=35190306981833000248550010000001501580649055, " +
////                            "numero=150, " +
////                            "serie=1, " +
////                            "dataEmissao=2019-03-01T14:08:05.712600, " +
////                            "dataEntrada=2019-03-14T14:08:05.712627, " +
////                            "modelo=1, " +
////                            "emissor=TORREFACAO NISHIDA SAN LTDA (***)" +
////                            "}, " +
////                            "entradaCte=EntradaCte" +
////                            "{" +
////                            "id=0, " +
////                            "chave=35190326010257000207570000000114451000129266, " +
////                            "numero=11445, " +
////                            "serie=0, " +
////                            "vlrCte=268.18, " +
////                            "qtdVolume=10, " +
////                            "pesoBruto=52.00, " +
////                            "vlrFreteBruto=225.00, " +
////                            "vlrTaxas=32.45, " +
////                            "vlrColeta=0.00, " +
////                            "vlrImpostoFrete=10.73, " +
////                            "dataEmissao=2019-03-01T14:08:05.712723, " +
////                            "tomadorServico=3, " +
////                            "modelo=3, " +
////                            "situacaoTributaria=(00) - Tributação normal ICMS, emissor=ROTA AIR BRASIL TRANSPORTE LOGISTICA E ARMAZENAGEM LTDA (ROTA AIR)" +
////                            "}" +
////                            "}";
//            String json =
//                    "EntradaProduto" +
//                            "{" +
//                            "id=LongProperty [value: 0]," +
//                            "situacao=IntegerProperty [value: 0]," +
//                            "loja=T. L. MACEDO (CAFE PERFEITO)," +
//                            "entradaNfe=EntradaNfe" +
//                            "{" +
//                            "id=LongProperty [value: 0]," +
//                            "chave=StringProperty [value: 35190306981833000248550010000001501580649055]," +
//                            "numero=StringProperty [value: 150]," +
//                            "serie=StringProperty [value: 1]," +
//                            "dataEmissao=2019-03-01T14:08:05.712600, " +
//                            "dataEntrada=2019-03-14T14:08:05.712627, " +
//                            "modelo=IntegerProperty [value: 1], " +
//                            "emissor=TORREFACAO NISHIDA SAN LTDA (***)" +
//                            "}," +
//                            "entradaCte=EntradaCte" +
//                            "{" +
//                            "id=LongProperty [value: 0], " +
//                            "chave=StringProperty [value: 35190326010257000207570000000114451000129266]," +
//                            "numero=StringProperty [value: 11445], " +
//                            "serie=StringProperty [value: 0]," +
//                            "vlrCte=268.18, " +
//                            "qtdVolume=IntegerProperty [value: 10], " +
//                            "pesoBruto=52.00, " +
//                            "vlrFreteBruto=225.00, " +
//                            "vlrTaxas=32.45, " +
//                            "vlrColeta=0.00, " +
//                            "vlrImpostoFrete=10.73, " +
//                            "dataEmissao=2019-03-01T14:08:05.712723, " +
//                            "tomadorServico=IntegerProperty [value: 3]," +
//                            "modelo=IntegerProperty [value: 3], " +
//                            "situacaoTributaria=[00] - Tributação normal ICMS, emissor=ROTA AIR BRASIL TRANSPORTE LOGISTICA E ARMAZENAGEM LTDA (ROTA AIR)" +
//                            "}" +
//                            "}";
//            ServiceJSonUtil.printJsonFromObject(Arrays.asList(json), null);
////            ObjectMapper mapper = new ObjectMapper();
////            System.out.println(json);
////            final EntradaProduto entProd = mapper.readValue(json, EntradaProduto.class);
////            //Read json
////            System.out.println(entProd);
////
////            ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
////            byte[] barWritten = objectWriter.writeValueAsBytes(entProd);
////            //Written json
////            System.out.println(new String(barWritten));  //Just to show - you should write something like ByteArrayInputStream bis = new ByteArrayInputStream(barWritten);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


//        String input = "Bancos mÃºltiplos, com carteira comercial";
//        String output = "";
//        try {
//            /* From ISO-8859-1 to UTF-8 */
//            output = new String(input.getBytes("ISO-8859-1"), "UTF-8");
//            /* From UTF-8 to ISO-8859-1 */
//            //output = new String(input.getBytes("UTF-8"), "ISO-8859-1");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        System.out.printf("output: [%s]\n", output);
//
//
//        String text = "Bancos mÃºltiplos, com carteira comercial";
//        System.out.printf("text: [%s]\n", text);
//        byte[] byteText = text.getBytes(Charset.forName("UTF-8"));
//        System.out.printf("byteText: [%s]\n", byteText);
////To get original string from byte.
//        String originalString = new String(byteText, "UTF-8");
//        System.out.printf("originalString: [%s]\n", originalString);


//        EmpresaDAO empresaDAO = new EmpresaDAO();
//        List<Empresa> empresaList = empresaDAO.getAll(Empresa.class, null);
//        System.out.printf("iniciando... total de empresas: [%d]\n", empresaList.size());
//        int cont = 1;
//        for (Empresa empresa : empresaList) {
//            System.out.printf("alterando (%d-%d) [%s]\n", cont, empresaList.size(), empresa.getRazao());
//            Integer ddd = 92;
//            if ((ddd = empresa.getEnderecoList().get(0).getMunicipio().getDdd()) != null) ;
//
//            for (Telefone telEmpresa : empresa.getTelefoneList()) {
//                System.out.printf("alterando\t[%s]\n", telEmpresa.getDescricao());
//                telEmpresa.setDescricao(ddd + telEmpresa.getDescricao().substring(2));
//                System.out.printf("alterou\t[%s]\n", telEmpresa.getDescricao());
//            }
//            for (Contato contatos : empresa.getContatoList()) {
//                System.out.printf("alterando\t[%s]\n", contatos.getDescricao());
//                for (Telefone telContato : contatos.getTelefoneList()) {
//                    System.out.printf("alterando\t[%s]\n", telContato.getDescricao());
//                    telContato.setDescricao(ddd + telContato.getDescricao().substring(2));
//                    System.out.printf("alterou\t[%s]\n", telContato.getDescricao());
//                }
//            }
//            empresaDAO.persiste(empresa);
//            System.out.printf("alterou (%d-%d)\n", cont++, empresaList.size());
//        }
//        System.out.printf("terminou...\n");


//        TelefoneDAO telefoneDAO = new TelefoneDAO();
//        List<Telefone> telefoneList = telefoneDAO.getAll(Telefone.class, null);
//        System.out.printf("iniciando... total de telefones: [%d]\n", telefoneList.size());
//        for (int i = 74; i < telefoneList.size(); i++) {
//            System.out.printf("alterando (%d-%d) [%s (%s)]\n", i, telefoneList.size(),
//                    telefoneList.get(i).getDescricao(), telefoneList.get(i).getTelefoneOperadora());
//            telefoneList.get(i).setTelefoneOperadora(getOperadoraTelefone(telefoneList.get(i).getDescricao()));
//            telefoneDAO.persiste(telefoneList.get(i));
//            System.out.printf("alterou (%d-%d) [%s (%s)]\n\n\n", i, telefoneList.size(),
//                    telefoneList.get(i).getDescricao(), telefoneList.get(i).getTelefoneOperadora());
//        }

//        for (Telefone tel : telefoneList) {
//            System.out.printf("alterando (%d-%d) [%s (%s)]\n", cont, telefoneList.size(), tel.getDescricao(), tel.getTelefoneOperadora());
//            tel.setTelefoneOperadora(getOperadoraTelefone(tel.getDescricao()));
//            telefoneDAO.persiste(tel);
//            System.out.printf("alterou (%d-%d) [%s (%s)]\n", cont++, telefoneList.size(), tel.getDescricao(), tel.getTelefoneOperadora());
//        }
//        System.out.printf("terminou...\n");


//        String dtp = "28/11/2018";
//        System.out.println(String.format("dtp: [%s]", dtp));
//
//        LocalDate data = LocalDate.parse(dtp, Convert_Date_Key.DTF_DATA);
//        System.out.println(String.format("data: [%s]", data));
//
//        LocalDateTime dateTime = data.atTime(0, 0, 0);
//        System.out.println(String.format("dateTime: [%s]", dateTime));

//        String senhaSimples = "cafeperfeito";
//        System.out.println("senhaSimples: [" + senhaSimples + "]");
//
//        String originalString = "Tlm$487901";
//        System.out.println("Original String to encrypt - " + originalString);
//        String encryptedString = ServiceCryptografia.encrypt(originalString);
//        System.out.println("Encrypted String - " + encryptedString);
//        String decryptedString = ServiceCryptografia.decrypt(encryptedString);
//        System.out.println("After decryption - " + decryptedString);

        //String key = "DB99A2A8EB6904F492E9DF0595ED683C";
        //String password = "Admin";

//        String salt ="cafeperfeito.com";// ServiceCryptografia.gerarSenhaSalt();
//        Scanner scan = new Scanner(System.in);
////        System.out.println("Please Enter Key:");
//        //key = key;//scanner.next();
//        System.out.println("Please Enter Plain Text Password:");
//        String senha = scan.next();
//        String encrypt = ServiceCryptografia.encrypt(senha,salt);
//        System.out.println("SenhaEncrypt: " + encrypt);
//        String decrypt = ServiceCryptografia.decrypt(encrypt,salt);
//        System.out.println("SenhaDecrypt: " + decrypt);
//
//
//
//        String key;
//        KeyGenerator keyGen = KeyGenerator.getInstance(ServiceCryptografia.AES);
//        keyGen.init(128);
//        SecretKey sk = keyGen.generateKey();
//        key = ServiceCryptografia.byteArrayToHexString(sk.getEncoded());
//        System.out.println("key:" + key);
//        key = ServiceCryptografia.gerarSenhaSalt();
//        System.out.println("key:" + key);
//        Scanner scanner = new Scanner(System.in);
////        System.out.println("Please Enter Key:");
//        //key = key;//scanner.next();
//        System.out.println("Please Enter Plain Text Password:");
//        String password = scanner.next();
//
//        byte[] bytekey = hexStringToByteArray(key);
//        SecretKeySpec sks = new SecretKeySpec(bytekey, ServiceCryptografia.AES);
//        Cipher cipher = Cipher.getInstance(ServiceCryptografia.AES);
//        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
//        byte[] encrypted = cipher.doFinal(password.getBytes());
//        String encryptedpwd = ServiceCryptografia.byteArrayToHexString(encrypted);
//        System.out.println("****************  Encrypted Password  ****************");
//        System.out.println(encryptedpwd);
//        System.out.println("****************  Encrypted Password  ****************");
//
//
//        String tempkey = key;
//        password = encryptedpwd;
//
//        bytekey = hexStringToByteArray(tempkey);
//        sks = new SecretKeySpec(bytekey, ServiceCryptografia.AES);
//        cipher = Cipher.getInstance(ServiceCryptografia.AES);
//        cipher.init(Cipher.DECRYPT_MODE, sks);
//        byte[] decrypted = cipher.doFinal(hexStringToByteArray(password));
//        String OriginalPassword = new String(decrypted);
//        System.out.println("****************  Original Password  ****************");
//        System.out.println(OriginalPassword);
//        System.out.println("****************  Original Password  ****************");
//
//
//        String ean = "7898903647025", path = System.getProperty("user.home") + "/Pictures/" + ean + "_EAN.png";

//        String text = "www.cafeperfeito.com.br";
//        // 二维码图片导出路径
//        //File file = new File("E:/二维码.jpg");
//        File file = new File(path);
//
//        // 二维码参数的构造对象，很多参数赋予了默认值，可自行通过set方法更改
//        ZxingEntity entity = new ZxingEntity();
//        entity.setBarcodeFormat(BarcodeFormat.QR_CODE);
//        entity.setText(text);
//        entity.setOutputFile(file);
//        entity.setWidth(300);
//        entity.setHeight(300);
//
//        // 以文件格式读取并导出，该方式适合本地调用
//        ZxingEncoder encoder = new ZxingEncoder();
//        encoder.encodeForFile(entity);
//
//        // 以文件格式扫描并解析
//        ZxingDecoder decoder = new ZxingDecoder();
//        Result result = decoder.decodeByFile(file, entity.getEncoding());
//
//        System.out.println("扫描结果 - [Text] : " + result.getText() + " [Timestamp] : " + result.getTimestamp() + " [BarcodeFormat] : " + result.getBarcodeFormat() + " [NumBits] : " + result.getNumBits());


//        // 条形码内容
//        String text = ean;//"6943620593115";
//        // 条形码图片导出路径
//        File file = new File(path);
//
//        // 条形码参数的构造对象，很多参数赋予了默认值，可自行通过set方法更改
//        ZxingEntity entity = new ZxingEntity();
//        entity.setBarcodeFormat(BarcodeFormat.EAN_13);
//        entity.setText(text);
//        entity.setMargin(5);
//        entity.setOutputFile(file);
//        entity.setWidth(115);
//        entity.setHeight(40);
//
//        // 以文件格式读取并导出，该方式适合本地调用
//        ZxingEncoder encoder = new ZxingEncoder();
//        encoder.encodeForFile(entity);
//
//        // 以文件格式扫描并解析
//        ZxingDecoder decoder = new ZxingDecoder();
//        Result result = decoder.decodeByFile(file, entity.getEncoding());
//
//        System.out.println("扫描结果 - [Text] : " + result.getText() + " [Timestamp] : " + result.getTimestamp() + " [BarcodeFormat] : " + result.getBarcodeFormat() + " [NumBits] : " + result.getNumBits());


//        String text = "123456789101";
//
//        int width = 300;
//        int height = 100;
//        String imgFormat = "png";
//        try {
//            BitMatrix bitMatrix = new EAN13Writer().encode(ean, BarcodeFormat.EAN_13, width, height);
//            MatrixToImageWriter.writeToStream(bitMatrix, imgFormat, new FileOutputStream(new File(path)));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Success!");


//        new ServiceEan13(ean).createBarcodePNG();


//        try{
//            Barcode barcode = BarcodeFactory.createEAN13(ean);
//            BufferedImage image = new BufferedImage(220, 130, BufferedImage.TYPE_BYTE_GRAY);
//            Graphics2D g = (Graphics2D) image.getGraphics();
//            g.setBackground(Color.BLUE);
//            barcode.draw(g, 10, 56);
//            File f = new File(path);
//            // Let the barcode image handler do the hard work
//            BarcodeImageHandler.saveJPEG(barcode, f);
//        }catch(Exception ex){
//            ex.getMessage();
//        }


//        Barcode barcode = null;
//        try {
//            barcode = BarcodeFactory.createEAN13(ean.substring(0, 12));
//            ImageIO.write(BarcodeImageHandler.getImage(barcode), "PNG", new File(path));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }


//        System.out.println("cnpj: [" + new ServiceFormatarDado().gerarMascara("len::;cnpj::;") + "]");
//        System.out.println("cpf: [" + new ServiceFormatarDado().gerarMascara("cpf") + "]");
//        System.out.println("barcode: [" + new ServiceFormatarDado().gerarMascara("barcode") + "]");
//        System.out.println("4numero0(123456): [" + new ServiceFormatarDado().gerarMascara("4numero0") + "]");
//        System.out.println("peso3(123456): [" + new ServiceFormatarDado().gerarMascara("peso3") + "]");
//        System.out.println("moeda2(123456): [" + new ServiceFormatarDado().gerarMascara("moeda2") + "]");
//        System.out.println("moeda3(123456): [" + new ServiceFormatarDado().gerarMascara("moeda3") + "]");
//        System.out.println("cep(69067360): [" + new ServiceFormatarDado().gerarMascara("cep") + "]");
//        System.out.println("ncm(09012100): [" + new ServiceFormatarDado().gerarMascara("nfencm") + "]");
//        System.out.println("cest(1234567): [" + new ServiceFormatarDado().gerarMascara("nfecest") + "]");
//        System.out.println("nfechave: [" + new ServiceFormatarDado().gerarMascara("nfechave") + "]");
//        System.out.println("nfenumero: [" + new ServiceFormatarDado().gerarMascara("nfenumero") + "]");
//        System.out.println("nfedocorigem: [" + new ServiceFormatarDado().gerarMascara("nfedocorigem") + "]");
//        System.out.println("telefone8: [" + new ServiceFormatarDado().gerarMascara("telefone8") + "]");
//        System.out.println("telefone9: [" + new ServiceFormatarDado().gerarMascara("telefone9") + "]");
//        System.out.println("ie(ac): [" + new ServiceFormatarDado().gerarMascara("ieac") + "]");
//        System.out.println("ie(al): [" + new ServiceFormatarDado().gerarMascara("ieal") + "]");
//        System.out.println("ie(am): [" + new ServiceFormatarDado().gerarMascara("ieam") + "]");
//        System.out.println("ie(ap): [" + new ServiceFormatarDado().gerarMascara("ieap") + "]");
//        System.out.println("ie(ba): [" + new ServiceFormatarDado().gerarMascara("ieba") + "]");
//        System.out.println("ie(ce): [" + new ServiceFormatarDado().gerarMascara("iece") + "]");
//        System.out.println("ie(df): [" + new ServiceFormatarDado().gerarMascara("iedf") + "]");
//        System.out.println("ie(es): [" + new ServiceFormatarDado().gerarMascara("iees") + "]");
//        System.out.println("ie(go): [" + new ServiceFormatarDado().gerarMascara("iego") + "]");
//        System.out.println("ie(ma): [" + new ServiceFormatarDado().gerarMascara("iema") + "]");
//        System.out.println("ie(mg): [" + new ServiceFormatarDado().gerarMascara("iemg") + "]");
//        System.out.println("ie(ms): [" + new ServiceFormatarDado().gerarMascara("iems") + "]");
//        System.out.println("ie(mt): [" + new ServiceFormatarDado().gerarMascara("iemt") + "]");
//        System.out.println("ie(pa): [" + new ServiceFormatarDado().gerarMascara("iepa") + "]");
//        System.out.println("ie(pb): [" + new ServiceFormatarDado().gerarMascara("iepb") + "]");
//        System.out.println("ie(pe): [" + new ServiceFormatarDado().gerarMascara("iepe") + "]");
//        System.out.println("ie(pi): [" + new ServiceFormatarDado().gerarMascara("iepi") + "]");
//        System.out.println("ie(pr): [" + new ServiceFormatarDado().gerarMascara("iepr") + "]");
//        System.out.println("ie(rj): [" + new ServiceFormatarDado().gerarMascara("ierj") + "]");
//        System.out.println("ie(rn): [" + new ServiceFormatarDado().gerarMascara("iern") + "]");
//        System.out.println("ie(ro): [" + new ServiceFormatarDado().gerarMascara("iero") + "]");
//        System.out.println("ie(rr): [" + new ServiceFormatarDado().gerarMascara("ierr") + "]");
//        System.out.println("ie(rs): [" + new ServiceFormatarDado().gerarMascara("iers") + "]");
//        System.out.println("ie(sc): [" + new ServiceFormatarDado().gerarMascara("iesc") + "]");
//        System.out.println("ie(se): [" + new ServiceFormatarDado().gerarMascara("iese") + "]");
//        System.out.println("ie(sp): [" + new ServiceFormatarDado().gerarMascara("iesp") + "]");
//        System.out.println("ie(to): [" + new ServiceFormatarDado().gerarMascara("ieto") + "]");
//
//        System.out.println("cnpj(08009246000136): [" + new ServiceFormatarDado().getValorFormatado("08009246000136", "cnpj") + "]");
//        System.out.println("cpf(52309550230): [" + new ServiceFormatarDado().getValorFormatado("52309550230", "cpf") + "]");
//        System.out.println("barcode(7896423421255): [" + new ServiceFormatarDado().getValorFormatado("7896423421255", "barcode") + "]");
//        System.out.println("numero0(123456): [" + new ServiceFormatarDado().getValorFormatado("123456","6numero0") + "]");
//        System.out.println("peso3(123456): [" + new ServiceFormatarDado().getValorFormatado("123456","peso3") + "]");
//        System.out.println("moeda2(123456): [" + new ServiceFormatarDado().getValorFormatado("12345678","moeda2") + "]");
//        System.out.println("moeda3(123456): [" + new ServiceFormatarDado().getValorFormatado("12345678","moeda3") + "]");
//        System.out.println("cep(69067360): [" + new ServiceFormatarDado().getValorFormatado("69067360","cep") + "]");
//        System.out.println("ncm(09012100): [" + new ServiceFormatarDado().getValorFormatado("09012100","nfencm") + "]");
//        System.out.println("cest(1234567): [" + new ServiceFormatarDado().getValorFormatado("1234567","nfecest") + "]");
//        System.out.println("nfechave(35180406981833000248550010000000471027609712): [" + new ServiceFormatarDado().getValorFormatado("35180406981833000248550010000000471027609712", "nfechave") + "]");
//        System.out.println("nfenumero(000000047): [" + new ServiceFormatarDado().getValorFormatado("000000047", "nfenumero") + "]");
//        System.out.println("nfedocorigem(041812441652): [" + new ServiceFormatarDado().getValorFormatado("041812441652", "nfedocorigem") + "]");
//        System.out.println("telefone8(38776148): [" + new ServiceFormatarDado().getValorFormatado("38776148", "telefone8") + "]");
//        System.out.println("telefone9(981686148): [" + new ServiceFormatarDado().getValorFormatado("981686148", "telefone9") + "]");
//
//        System.out.println("ie(AM): [" + new ServiceFormatarDado().gerarMascara("ieam") + "]");
//
//
//        System.out.print("digite um valor para conversão: ");
//
//
//


//        //System.out.println("moeda2(123456): [" + new ServiceFormatarDado().getValorFormatado("1234567890123",15,"moeda", 3) + "]");
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date()));
//        String date = "2017-03-08T12:30:54";
//        LocalDateTime localDateTime1 = LocalDateTime.parse(date);
//        System.out.println("origional date as string: " + date);
//        System.out.println("generated LocalDateTime: " + localDateTime1);
//
//        String dhEmi = "2018-09-03T12:01:12-03:00";//.replace("T", " ");//"YYYY-MM-ddTHH:mm:ssGTM"
//        //System.out.println("localDateTime: [" + SDF.parse(dhEmi) + "]");
//        LocalDateTime localDateTime = LocalDateTime.parse(dhEmi, DTF_NFE_TO_LOCAL_DATE);
//        System.out.println("localDateTime: [" + localDateTime + "]");
//        System.out.println("toLocalDate: [" + localDateTime.toLocalDate() + "]");


// Arquivo a ser movido
//        File arquivo = new File("/Users/thiagomacedo/Desktop/35180906186733000220570010005423021009457382-cte.xml");
//
//        if (!arquivo.exists()) {
//            System.out.println("Arquivo não encontrado");
//        } else {
//
//            // Diretorio de destino
//            File diretorioDestino = new File(PATH_DIR_XML_NFE_CTE + "cte/in/");
//
//            String fileName = arquivo.getName();
//            Files.move(arquivo.toPath(), Paths.get(PATH_CLASS_XML_NFE_CTE + "cte/in", fileName), StandardCopyOption.REPLACE_EXISTING);
//
////            // Move o arquivo para o novo diretorio
////            boolean sucesso = arquivo.renameTo(new File(diretorioDestino, arquivo.getName()));
////            if (sucesso)
////                System.out.println("Arquivo movido para '" + diretorioDestino.getAbsolutePath() + "'");
////            else
////                System.out.println("Erro ao mover arquivo '" + arquivo.getAbsolutePath() + "' para '"
////                        + diretorioDestino.getAbsolutePath() + "'");
//        }


//        Connection conn = ConnectionFactory.getConnection();
//        CallableStatement stmt = conn.prepareCall("{CALL getTabEmpresa()}");
//        //stmt.setInt(1, 1);
//        ResultSet rs = stmt.executeQuery();
//        while (rs.next())
//            System.out.println(String.format("id: %s  cnpj: %s  empresa: %s (%s)",
//                    String.format("%03d", rs.getInt("id")),
//                    rs.getString("cnpj"),
//                    rs.getString("razao"),
//                    rs.getString("fantasia")));


    }

    private static TelefoneOperadora getOperadoraTelefone(String telefone) {
        String retURL;
        if ((retURL = new ServiceBuscaWebService().getObjectWebService(
                String.format("%s?pass=%s&user=%s&search_number=%s",
                        "http://consultas.portabilidadecelular.com/painel/consulta_numero.php",
                        "Tlm487901",
                        "tlmacedo",
                        telefone.replaceAll("\\D", ""))
        )) == null) retURL = "55321";
        String finalRetURL = retURL;
        return new TelefoneOperadoraDAO().getAll(TelefoneOperadora.class, null, null, null, null)
                .stream().filter(operadora -> operadora.getCodWsPortabilidade().contains(finalRetURL))
                .findFirst()
                .orElse(new TelefoneOperadoraDAO().getById(TelefoneOperadora.class, 355L));
    }

}
