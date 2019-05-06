package raytracing;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import raytracing.geometry.InfinitePlane;
import raytracing.geometry.Shape;
import raytracing.geometry.Sphere;
import raytracing.geometry.Triangle;
import raytracing.util.Vector3;

/**
 * Main class for ray tracing exercise.
 */
public class RayTracer {

	public int imageWidth;
	public int imageHeight;

	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as
	 * input.
	 */
	public static void main(String[] args) {

		try {

			RayTracer tracer = new RayTracer();

			// Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException(
						"Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			if (args.length > 3) {
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}

			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

			// } catch (IOException e) {
			// System.out.println(e.getMessage());
		} catch (RayTracerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it
	 * generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException {
		FileReader fr = new FileReader(sceneFileName);

		Scene scene = new Scene();
		List<Material> materials = new ArrayList<>();
		List<Shape> shapes = new ArrayList<>();

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);

		while ((line = r.readLine()) != null) {
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#')) { // This line in the scene file is a comment
				continue;
			}
			String code = line.substring(0, 3).toLowerCase();
			// Split according to white space characters:
			String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

			if (code.equals("cam")) {
				Camera camera = new Camera();

				Vector3 position = new Vector3(Double.parseDouble(params[0]), Double.parseDouble(params[2]),
						Double.parseDouble(params[2]));
				camera.setPosition(position);

				Vector3 lookAtPoint = new Vector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
						Double.parseDouble(params[5]));
				camera.setLookAtPoint(lookAtPoint);

				Vector3 up = new Vector3(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
						Double.parseDouble(params[8]));
				camera.setUpVector(up);
				camera.setScreenDistance(Integer.parseInt(params[9]));
				camera.setScreenWidth(Integer.parseInt(params[10]));

				scene.setCamera(camera);

				System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));

			} else if (code.equals("set")) {
				Settings settings = new Settings();

				settings.setBackgroundColor(new Vector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
						Double.parseDouble(params[2])));
				settings.setNumOfShadowRays(Integer.parseInt(params[3]));
				settings.setMaxRecursionLevel(Integer.parseInt(params[4]));
				settings.setSuperSamplingLevel(Integer.parseInt(params[5]));

				scene.setSettings(settings);

				System.out.println(String.format("Parsed general settings (line %d)", lineNum));

			} else if (code.equals("mtl")) {
				Material material = new Material();

				material.setDiffuseColor(new Vector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
						Double.parseDouble(params[2])));
				material.setSpecularColor(new Vector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
						Double.parseDouble(params[5])));
				material.setReflectionColor(new Vector3(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
						Double.parseDouble(params[8])));
				material.setPhongSpecularityCoefficient(Double.parseDouble(params[9]));
				material.setTransparency(Double.parseDouble(params[10]));

				materials.add(material);

				System.out.println(String.format("Parsed material (line %d)", lineNum));

			} else if (code.equals("sph")) {
				Sphere sphere = new Sphere();

				sphere.setCenter(new Vector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
						Double.parseDouble(params[2])));
				sphere.setRadius(Double.parseDouble(params[3]));
				sphere.setMaterialIndex(Integer.parseInt(params[4]));

				shapes.add(sphere);

				System.out.println(String.format("Parsed sphere (line %d)", lineNum));

			} else if (code.equals("pln")) {
				InfinitePlane infinitePlane = new InfinitePlane();

				infinitePlane.setNormal(new Vector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
						Double.parseDouble(params[2])));
				infinitePlane.setOffset(Double.parseDouble(params[3]));
				infinitePlane.setMaterialIndex(Integer.parseInt(params[4]));

				shapes.add(infinitePlane);

				System.out.println(String.format("Parsed plane (line %d)", lineNum));

			} else if (code.equals("trg")) {
				Triangle triangle = new Triangle();

				triangle.setVertex1(new Vector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
						Double.parseDouble(params[2])));
				triangle.setVertex2(new Vector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
						Double.parseDouble(params[5])));
				triangle.setVertex3(new Vector3(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
						Double.parseDouble(params[8])));
				triangle.setMaterialIndex(Integer.parseInt(params[10]));

				shapes.add(triangle);

				System.out.println(String.format("Parsed triangle (line %d)", lineNum));

			} else if (code.equals("lgt")) {
				Light light = new Light();

				light.setPosition(new Vector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
						Double.parseDouble(params[2])));
				light.setColor(new Vector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
						Double.parseDouble(params[5])));

				System.out.println(String.format("Parsed light (line %d)", lineNum));
			} else {
				System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
			}
		}

		// It is recommended that you check here that the scene is valid,
		// for example camera settings and all necessary materials were defined.

		System.out.println("Finished parsing scene file " + sceneFileName);

	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName) {
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];

		// Put your ray tracing code here!
		//
		// Write pixel color values in RGB format to rgbData:
		// Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) * 3]
		// green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
		// blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
		//
		// Each of the red, green and blue components should be a byte, i.e. 0-255

		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

		// The time is measured for your own conveniece, rendering speed will not affect
		// your score
		// unless it is exceptionally slow (more than a couple of minutes)
		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

		// This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}

	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT
	//////////////////////// //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName) {
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB
	 * values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
		int height = buffer.length / width / 3;
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		ColorModel cm = new ComponentColorModel(cs, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		SampleModel sm = cm.createCompatibleSampleModel(width, height);
		DataBufferByte db = new DataBufferByte(buffer, width * height);
		WritableRaster raster = Raster.createWritableRaster(sm, db, null);
		BufferedImage result = new BufferedImage(cm, raster, false, null);

		return result;
	}

	public static class RayTracerException extends Exception {
		public RayTracerException(String msg) {
			super(msg);
		}
	}

}
