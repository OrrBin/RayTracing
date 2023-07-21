package raytracing.parsing.serialization.json;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import raytracing.actors.Camera;
import raytracing.actors.Light;
import raytracing.actors.Material;
import raytracing.actors.Scene;
import raytracing.animation.StageManager;
import raytracing.geometry.FuncCosPlusSin;
import raytracing.geometry.Plane;
import raytracing.geometry.Polygon3D;
import raytracing.geometry.Sphere;
import raytracing.geometry.SquarePyramid;
import raytracing.geometry.Torus;
import raytracing.geometry.Triangle;
import raytracing.geometry.TriangularPyramid;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;
import raytracing.parsing.SceneParser;
import raytracing.projection.Projection;
import raytracing.util.ImageUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Main class for ray tracing exercise.
 */

@Slf4j
@AllArgsConstructor
public class SceneParserCustomFormat implements SceneParser {

	private final Vector3Factory vector3Factory;
	private final StageManager stageManager;
	private final ImageUtils imageUtils;
	private final Map<String, Projection> projectionMap;

	/**
	 * Parses the scene file and creates the scene. Change this function so it
	 * generates the required objects.
	 */
	@Override
	public Scene parseScene(final File sceneFile, final int imageWidth, final int imageHeight) throws IOException {

		final String sceneId = sceneFile.getName().substring(0, sceneFile.getName().indexOf("."));
		final Scene scene = new Scene(sceneId, imageWidth, imageHeight, stageManager, imageUtils, vector3Factory);

		log.info("Started parsing scene file: {}", sceneFile.getAbsolutePath());

		final FileReader fr = new FileReader(sceneFile);
		final BufferedReader bufferedReader = new BufferedReader(fr);
		int max_mat = 0;
		int lineNum = 0;
		String configLine;

		while ((configLine = bufferedReader.readLine()) != null) {
			configLine = configLine.trim();
			++lineNum;

			if (configLine.isEmpty() || (configLine.charAt(0) == '#')) { // This line in the scene file is a comment
				continue;
			}
			String code = configLine.substring(0, 3).toLowerCase();
			// Split according to white space characters:
			String[] params = configLine.substring(3).trim().toLowerCase().split("\\s+");

			switch (code) {
				case "cam":
					Vector3 pos = vector3Factory.getVector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					Vector3 lap = vector3Factory.getVector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]));
					Vector3 up = vector3Factory.getVector3(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
							Double.parseDouble(params[8]));
					double screen_dist = Double.parseDouble(params[9]), screen_width = Double.parseDouble(params[10]);
					scene.setCamera(new Camera(pos, lap, up, screen_dist, screen_width));
					System.out.printf("Parsed camera parameters (line %d)%n", lineNum);
					break;
				case "set":
					scene.getSettings().setBackgroundColor(vector3Factory.getVector3(Double.parseDouble(params[0]),
							Double.parseDouble(params[1]), Double.parseDouble(params[2])));
					scene.getSettings().setNumOfShadowRays(Integer.parseInt(params[3]));
					scene.getSettings().setMaxRecursionLevel(Integer.parseInt(params[4]));
					scene.getSettings().setSuperSamplingLevel(Integer.parseInt(params[5]));
					scene.getSettings().setNumberOfFrames(Integer.parseInt(params[6]));
					scene.getSettings().setFramesPerSecond(Integer.parseInt(params[7]));
					if(params.length >= 9) {
						scene.getSettings().setBackgroundImage(params[8]);
					}
					System.out.printf("Parsed general settings (line %d)%n", lineNum);
					break;
				case "mtl":
					Vector3 diffuse = vector3Factory.getVector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					Vector3 specular = vector3Factory.getVector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]));
					Vector3 reflection = vector3Factory.getVector3(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
							Double.parseDouble(params[8]));
					double phong = Double.parseDouble(params[9]);
					double trans = Double.parseDouble(params[10]);
					scene.getMaterials().add(new Material(diffuse, specular, reflection, phong, trans));
					System.out.printf("Parsed material (line %d)%n", lineNum);
					break;
				case "sph": {
					Vector3 center = vector3Factory.getVector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					double radius = Double.parseDouble(params[3]);
					int mat_idx = Integer.parseInt(params[4]) - 1;
					String imagePath = null;
					String projectionName = null;
					if (params.length == 7) {
						imagePath = params[5];
						projectionName = params[6];
					}
					max_mat = Math.max(max_mat, mat_idx);
					scene.addShape(new Sphere(center, radius, mat_idx, imagePath, projectionMap.get(projectionName)));
					System.out.printf("Parsed sphere (line %d)%n", lineNum);
					break;
				}
				case "pln": {
					Vector3 normal = vector3Factory.getVector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					double offset = Double.parseDouble(params[3]);
					int mat_idx = Integer.parseInt(params[4]) - 1;
					max_mat = Math.max(max_mat, mat_idx);
					scene.addShape(new Plane(normal, offset, mat_idx));
					System.out.printf("Parsed plane (line %d)%n", lineNum);
					break;
				}
				case "py3": {
					Vector3 p1 = vector3Factory.getVector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					Vector3 p2 = vector3Factory.getVector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]));
					Vector3 p3 = vector3Factory.getVector3(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
							Double.parseDouble(params[8]));
					Vector3 p4 = vector3Factory.getVector3(Double.parseDouble(params[9]), Double.parseDouble(params[10]),
							Double.parseDouble(params[11]));
					int mat_idx = Integer.parseInt(params[12]) - 1;
					max_mat = Math.max(max_mat, mat_idx);
					scene.addSuperShape(new TriangularPyramid(p1, p2, p3, p4, mat_idx, vector3Factory));
					System.out.printf("Parsed triangular pyramid (line %d)%n", lineNum);
					break;
				}
				case "py4": {
					Vector3 p1 = vector3Factory.getVector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					Vector3 p2 = vector3Factory.getVector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]));
					Vector3 p3 = vector3Factory.getVector3(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
							Double.parseDouble(params[8]));
					Vector3 p4 = vector3Factory.getVector3(Double.parseDouble(params[9]), Double.parseDouble(params[10]),
							Double.parseDouble(params[11]));
					Vector3 p5 = vector3Factory.getVector3(Double.parseDouble(params[12]), Double.parseDouble(params[13]),
							Double.parseDouble(params[14]));
					int mat_idx = Integer.parseInt(params[15]) - 1;
					max_mat = Math.max(max_mat, mat_idx);
					scene.addSuperShape(new SquarePyramid(p1, p2, p3, p4, p5, mat_idx, vector3Factory));
					System.out.printf("Parsed square pyramid (line %d)%n", lineNum);
					break;
				}
				case "pol": {
					if (params.length < 4 || params.length % 3 != 1) {
						throw new RuntimeException("Wrong number of parameters for polygon");
					}

					List<Vector3> vertices = new LinkedList<>();
					for (int i = 0; i < params.length / 3; i++) {
						vertices.add(vector3Factory.getVector3(
								Double.parseDouble(params[i * 3]), Double.parseDouble(params[i * 3 + 1]), Double.parseDouble(params[i * 3 + 2])));
					}
					int mat_idx = Integer.parseInt(params[params.length - 1]) - 1;
					max_mat = Math.max(max_mat, mat_idx);
					scene.addSuperShape(new Polygon3D(vertices, mat_idx, vector3Factory));
					System.out.printf("Parsed Polygon (line %d)%n", lineNum);
					break;
				}
				case "trg": {
					Vector3 p1 = vector3Factory.getVector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					Vector3 p2 = vector3Factory.getVector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]));
					Vector3 p3 = vector3Factory.getVector3(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
							Double.parseDouble(params[8]));
					int mat_idx = Integer.parseInt(params[9]) - 1;
					max_mat = Math.max(max_mat, mat_idx);
					scene.addShape(new Triangle(p1, p2, p3, mat_idx, vector3Factory));
					System.out.printf("Parsed triangle (line %d)%n", lineNum);
					break;
				}
				case "trs": {
					double sweptRadius = Double.parseDouble(params[0]);
					double tubeRadius = Double.parseDouble(params[1]);
					Vector3 rotation = vector3Factory.getVector3(Double.parseDouble(params[2]), Double.parseDouble(params[3]),
							Double.parseDouble(params[4]));
					Vector3 translation = vector3Factory.getVector3(Double.parseDouble(params[5]), Double.parseDouble(params[6]),
							Double.parseDouble(params[7]));
					int mat_idx = Integer.parseInt(params[8]) - 1;
					max_mat = Math.max(max_mat, mat_idx);
					scene.addShape(new Torus(sweptRadius, tubeRadius, rotation, translation, mat_idx, vector3Factory));
					System.out.printf("Parsed triangle (line %d)%n", lineNum);
					break;
				}
				case "cps": {
					double zOffset = Integer.parseInt(params[0]);
					int mat_idx = Integer.parseInt(params[1]) - 1;
					max_mat = Math.max(max_mat, mat_idx);
					scene.addShape(new FuncCosPlusSin(zOffset, mat_idx, vector3Factory));
					System.out.printf("Parsed cossin (line %d)%n", lineNum);
					break;
				}
				case "lgt": {
					Vector3 center = vector3Factory.getVector3(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
							Double.parseDouble(params[2]));
					Vector3 color = vector3Factory.getVector3(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
							Double.parseDouble(params[5]));
					double specularIntensity = Double.parseDouble(params[6]);
					double shadowIntensity = Double.parseDouble(params[7]);
					double lightRadius = Double.parseDouble(params[8]);
					final Light light = new Light(
							center, color, specularIntensity, shadowIntensity, lightRadius);
					light.setUp(scene.getCamera().getUpVector());
					light.setVector3Factory(vector3Factory);
					scene.getLights().add(light);
					System.out.printf("Parsed light (line %d)%n", lineNum);
					break;
				}
				default:
					System.out.printf("Undefined object: %s (line %d)%n", code, lineNum);
					break;
			}
		}

		// It is recommended that you check here that the scene is valid,
		// for example camera settings and all necessary materials were defined.

		bufferedReader.close();

		if (max_mat >= scene.getMaterials().size()) {
			throw new IllegalArgumentException("An undefined material is being used");
		}
		log.info("Finished parsing scene file: {}", sceneFile.getAbsolutePath());

		return scene;
	}
}
