package raytracing.actors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import raytracing.math.Vector3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import raytracing.util.ImageUtils;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Settings {

	private Vector3 backgroundColor;
	private int numOfShadowRays;
	private int maxRecursionLevel;
	private int superSamplingLevel;
	private int numberOfFrames;
	private int framesPerSecond;
	private BufferedImage backgroundImage;
	private final ImageUtils imageUtils;

	public Vector3 getBackgroundColor(final int row, final int col) {
		if(backgroundImage != null) {
			return imageUtils.getPixelColor(backgroundImage, row, col);
		}

		return backgroundColor.cpy();
	}

	public void setBackgroundImage(final String imagePath) throws IOException {
		if (imagePath != null) {
			final File imageFile = new File(imagePath);
			this.backgroundImage = ImageIO.read(imageFile);
		} else {
			this.backgroundImage = null;
		}
	}
}
