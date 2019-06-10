package br.com.tlmacedo.cafeperfeito.service;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ServiceImage {

    public static Image getImagemFromUrl(String strUrl) {
        Image image = null;
        try {
            URL url = new URL(strUrl);
            image = SwingFXUtils.toFXImage(ImageIO.read(url), null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return image;
    }

    public static InputStream getInputStreamFromImage(Image image) {
        if (image == null)
            return null;
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", outputStream);
            byte[] res = outputStream.toByteArray();
            return new ByteArrayInputStream(res);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Image getImageFromInputStream(InputStream inputStream) {
        if (inputStream == null)
            return null;
        try {
            return SwingFXUtils.toFXImage(ImageIO.read(inputStream), null);
        } catch (Exception ex) {
            if (!(ex instanceof NullPointerException))
                ex.printStackTrace();
        }
        return null;
    }

    public static Image getImageCodBarrasEAN13(String busca) {
        Barcode barcode = null;
        try {
            barcode = BarcodeFactory.createEAN13(busca.substring(0, 12));
//            barcode.setBarHeight(2);
//            barcode.setBarWidth(200);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            return SwingFXUtils.toFXImage(BarcodeImageHandler.getImage(barcode), null);
        } catch (OutputException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Image getImageResized(final Image image, int width, int height) {
        BufferedImage inputImage = SwingFXUtils.fromFXImage(image, null);
        BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, width, height, null);
        g2d.dispose();
        return SwingFXUtils.toFXImage(outputImage, null);
    }


}
