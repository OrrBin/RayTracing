package raytracing.util;

import lombok.AllArgsConstructor;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;

import java.awt.image.BufferedImage;

@AllArgsConstructor
public class ImageUtils {

    private Vector3Factory vector3Factory;

    public Vector3 getPixelColor(final BufferedImage image, final int x, final int y) {
        int clr;
        try {
            clr = image.getRGB(x, y);
        } catch (final ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException: x, y:" + x + ", " + y);
            return null;
        }
        final int red = (clr & 0x00ff0000) >> 16;
        final int green = (clr & 0x0000ff00) >> 8;
        final int blue = clr & 0x000000ff;

        return vector3Factory.getVector3(red / 256.0, green / 256.0, blue / 256.0);
    }
}
