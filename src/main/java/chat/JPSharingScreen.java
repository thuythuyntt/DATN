/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author thuy
 */
public class JPSharingScreen extends JPanel {

//    private int[] img;'
    private byte[] img;

    public JPSharingScreen(byte[] capture) {
        img = capture;
    }

    private BufferedImage createRGBImage() {
//        DataBufferByte buffer = new DataBufferByte(bytes, bytes.length);
//        ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8}, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
//        return new BufferedImage(cm, Raster.createInterleavedRaster(buffer, width, height, width * 3, 3, new int[]{0, 1, 2}, null), false, null);
    
        try {
            System.out.println(img.toString());
            ByteArrayInputStream bais = new ByteArrayInputStream(img);
            return ImageIO.read(bais);
        } catch (IOException ex) {
            Logger.getLogger(JPSharingScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(createRGBImage(), 0, 0, this);
    }

//    private BufferedImage capture;
//
//    public JPSharingScreen(BufferedImage capture) {
//        this.capture = capture;
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        g.drawImage(capture, 0, 0, this);
//    }
}
