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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import raytracing.geometry.FuncCosPlusSin;
import raytracing.geometry.Plane;
import raytracing.geometry.Sphere;
import raytracing.geometry.Triangle;
import raytracing.util.Vector3;

/**
 * Main class for ray tracing exercise.
 */
public class RayTracer {

	public int imageWidth;
	public int imageHeight;
	public Scene scene;

	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as
	 * input.
	 * 
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

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

			tracer.scene = new Scene(tracer.imageWidth, tracer.imageHeight);

			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

		} catch (IOException | RayTracerException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it
	 * generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException {
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);

		int max_mat = 0;
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
				Vector3 pos = new Vector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
						Double.parseDouble(params[2]));
				Vector3 lap = new Vector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
						Double.parseDouble(params[5]));
				Vector3 up = new Vector3(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
						Double.parseDouble(params[8]));
				double screen_dist = Double.parseDouble(params[9]), screen_width = Double.parseDouble(params[10]);
				scene.setCamera(new Camera(pos, lap, up, screen_dist, screen_width));
				System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
			} else if (code.equals("set")) {
				scene.getSettings().setBackgroundColor(new Vector3(Double.parseDouble(params[0]),
						Double.parseDouble(params[1]), Double.parseDouble(params[2])));
				scene.getSettings().setNumOfShadowRays(Integer.parseInt(params[3]));
				scene.getSettings().setMaxRecursionLevel(Integer.parseInt(params[4]));
				scene.getSettings().setSuperSamplingLevel(Integer.parseInt(params[5]));
				System.out.println(String.format("Parsed general settings (line %d)", lineNum));
			} else if (code.equals("mtl")) {
				Vector3 diffuse = new Vector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
						Double.parseDouble(params[2]));
				Vector3 specular = new Vector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
						Double.parseDouble(params[5]));
				Vector3 reflection = new Vector3(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
						Double.parseDouble(params[8]));
				Double phong = Double.parseDouble(params[9]);
				Double trans = Double.parseDouble(params[10]);
				scene.getMaterials().add(new Material(diffuse, specular, reflection, phong, trans));
				System.out.println(String.format("Parsed material (line %d)", lineNum));
			} else if (code.equals("sph")) {
				Vector3 center = new Vector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
						Double.parseDouble(params[2]));
				double radius = Double.parseDouble(params[3]);
				int mat_idx = Integer.parseInt(params[4]) - 1;
				max_mat = Math.max(max_mat, mat_idx);
				scene.getShapes().add(new Sphere(center, radius, mat_idx));
				System.out.println(String.format("Parsed sphere (line %d)", lineNum));
			} else if (code.equals("pln")) {
				Vector3 normal = new Vector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
						Double.parseDouble(params[2]));
				double offset = Double.parseDouble(params[3]);
				int mat_idx = Integer.parseInt(params[4]) - 1;
				max_mat = Math.max(max_mat, mat_idx);
				scene.getShapes().add(new Plane(normal, offset, mat_idx));
				System.out.println(String.format("Parsed plane (line %d)", lineNum));
			} else if (code.equals("trg")) {
				Vector3 p1 = new Vector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
						Double.parseDouble(params[2]));
				Vector3 p2 = new Vector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
						Double.parseDouble(params[5]));
				Vector3 p3 = new Vector3(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
						Double.parseDouble(params[8]));
				int mat_idx = Integer.parseInt(params[9]) - 1;
				max_mat = Math.max(max_mat, mat_idx);
				scene.getShapes().add(new Triangle(p1, p2, p3, mat_idx));
				System.out.println(String.format("Parsed triangle (line %d)", lineNum));
			} else if (code.equals("cps")) {
				double zOffset = Integer.parseInt(params[0]);
				int mat_idx = Integer.parseInt(params[1]) - 1;
				max_mat = Math.max(max_mat, mat_idx);
				scene.getShapes().add(new FuncCosPlusSin(zOffset, mat_idx));
				System.out.println(String.format("Parsed cossin (line %d)", lineNum));
			} else if (code.equals("lgt")) {
				Vector3 center = new Vector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
						Double.parseDouble(params[2]));
				Vector3 color = new Vector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
						Double.parseDouble(params[5]));
				double specularIntensity = Double.parseDouble(params[6]);
				double shadowIntensity = Double.parseDouble(params[7]);
				double lightRadius = Double.parseDouble(params[8]);
				scene.getLights().add(new Light(center, color, specularIntensity, shadowIntensity, lightRadius));
				System.out.println(String.format("Parsed light (line %d)", lineNum));
			} else {
				System.out.println(String.format("Undefined object: %s (line %d)", code, lineNum));
			}
		}

		// It is recommended that you check here that the scene is valid,
		// for example camera settings and all necessary materials were defined.

		r.close();

		if (max_mat >= scene.getMaterials().size()) {
			System.err.println("An undefined material is being used");
			System.exit(1);
		}
		System.out.println("Finished parsing scene file " + sceneFileName);

	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 * 
	 * @throws InterruptedException
	 */
	public void renderScene(String outputFileName) throws InterruptedException {
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];

		ExecutorService executor = Executors.newFixedThreadPool(6);
		List<Callable<Boolean>> tasks = new ArrayList<>();

		for (int top = 0; top < scene.imageHeight; top++) {
			for (int left = 0; left < scene.imageWidth; left++) {
				tasks.add(new PixelTask(scene, rgbData, top, left));
			}
		}
		executor.invokeAll(tasks);
		executor.shutdown();
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

		private static final long serialVersionUID = -835524526290457389L;

		public RayTracerException(String msg) {
			super(msg);
		}
	}

}
