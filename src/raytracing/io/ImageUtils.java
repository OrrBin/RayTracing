package raytracing.io;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    /*
     * Saves RGB data as an image in png format to the specified location.
     */
    public static void saveImage(final int width, final byte[] rgbData, final File file) {
        try {

            BufferedImage image = bytes2RGB(width, rgbData);
            ImageIO.write(image, "jpeg", file);

        } catch (IOException e) {
            System.out.println("ERROR SAVING FILE: " + e.getMessage());
        }

    }

    /*
     * Producing a BufferedImage that can be saved as png from a byte array of RGB
     * values.
     */
    public static BufferedImage bytes2RGB(final int width, final byte[] buffer) {
        int height = buffer.length / width / 3;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);

        return new BufferedImage(cm, raster, false, null);
    }

    public static class RayTracerException extends Exception {

        private static final long serialVersionUID = -835524526290457389L;

        public RayTracerException(String msg) {
            super(msg);
        }
    }
}
