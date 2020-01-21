package raytracing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import raytracing.geometry.Shape;
import raytracing.util.Constants;
import raytracing.util.IntersectionData;
import raytracing.util.Ray;
import raytracing.util.Vector3;

public class Scene {

	public int imageWidth, imageHeight;

	private double aspectRatio;

	private Settings settings;
	private Camera camera;
	private List<Light> lights;
	private List<Shape> shapes;
	private List<Material> materials;

	public Scene(int width, int height) {
		this.imageHeight = height;
		this.imageWidth = width;
		this.aspectRatio = ((double) this.imageWidth) / this.imageHeight;

		this.materials = new ArrayList<>();
		this.shapes = new ArrayList<>();
		this.lights = new ArrayList<>();
		this.settings = new Settings();
	}

	public Ray[] getSuperSamplingRays(int top, int left)
	{
		int N = Math.max(1, settings.getSuperSamplingLevel());
		Ray[] rays = new Ray[N*N];
		
		Random rnd = new Random();
		
		double mult = 1.0;
		double subRectWidth = mult / N;
		for(int i = 0; i < N; i++) {
			double yMin = (subRectWidth * i)+left;
			for(int j = 0; j < N; j++) {
				double xMin = (subRectWidth * j)+top;
				
				double xOffset = xMin + rnd.nextDouble() * subRectWidth;
				double yOffset = yMin + rnd.nextDouble() * subRectWidth;
				rays[i*N + j] = getRay(xOffset, yOffset);
			}
		}
		return rays;
	}
	
	public Ray getRay(double i, double j) {
		Vector3 forward = this.camera.getForward();
		Vector3 right = this.camera.getRight();
		Vector3 top = this.camera.getTop();
		double d = this.camera.getScreenDistance();
		double screenWidth = this.camera.getScreenWidth();
		double screenHeight = screenWidth / aspectRatio;

		Vector3 p0 = this.camera.getPosition();

		Vector3 p1 = p0.cpy().add(forward.cpy().multiply(d)).add(right.cpy().multiply(-screenWidth / 2))
				.add(top.cpy().multiply(screenHeight / 2));


		double leftOff = j - this.imageWidth / 2.0;
		double topOff = i - this.imageHeight / 2.0;

		double leftPixel = (leftOff / this.imageWidth + 0.5) * screenWidth;
		double topPixel = (topOff / this.imageHeight + 0.5) * screenHeight;

		Vector3 rightScreenVec = right.cpy().multiply(leftPixel);
		Vector3 topScreenVec = top.cpy().multiply(-topPixel);

		Vector3 p = p1.cpy().add(rightScreenVec).add(topScreenVec);

		Vector3 direction = p0.connectingVector(p).normalize();

		return new Ray(p0, direction);
	}

	/**
	 * calculates diffuse and sepcular colors
	 */
	Vector3 diffuseAndSpecColor(IntersectionData inter, Ray ray, Material mat) {
		Vector3 diffLight = new Vector3(0, 0, 0);
		Vector3 specLight = new Vector3(0, 0, 0);

		Vector3 v = ray.getDirection().cpy().multiply(-1).normalize();
		Vector3 normal = inter.getShape().normal(inter.getIntersectionPoint(), ray);

		// Calculate each of the scenes light diffuse and specular color and sum up
		for (Light light : this.lights) {
			Vector3 lightDirection = light.getPosition().connectingVector(inter.getIntersectionPoint()).normalize();
			Vector3 reveresedLightDirection = lightDirection.cpy().multiply(-1);

			if (normal.dotProduct(reveresedLightDirection) < 0)
				continue;

			Ray[] rays = light.getRays(inter.getIntersectionPoint(), this.settings.getNumOfShadowRays());
			double shadow = 0;
			for (Ray subRay : rays) {
				List<Shape> intersections = blockingShapes(subRay, inter.getShape());
				double intersectionsSum = 0;
				for (Shape shape : intersections) {
					intersectionsSum += 1 - this.materials.get(shape.getMaterial()).getTransparency();
				}
				shadow += Math.pow(1 - light.getShadowIntensity(), intersectionsSum);
			}

			double factor = normal.dotProduct(reveresedLightDirection) * shadow / rays.length;
			Vector3 added = light.getColor().cpy().multiply(factor);
			diffLight.add(added);

			Vector3 reflectedLight = lightDirection.cpy()
					.add(normal.cpy().multiply(-2 * lightDirection.dotProduct(normal))).normalize();

			double angleWithLightReflection = reflectedLight.dotProduct(v);

			if (angleWithLightReflection > 0) {
				specLight.add(light.getColor().cpy()
						.multiply(Math.pow(angleWithLightReflection, mat.getPhongSpecularityCoefficient()) * shadow
								* light.getSpecularIntensity() / rays.length));
			}

		}

		diffLight.multiply(mat.getDiffuseColor());
		specLight.multiply(mat.getSpecularColor());

		return diffLight.add(specLight);
	}

	/**
	 * calculates transparent colors
	 */
	Vector3 transparencyColor(IntersectionData inter, Ray ray, Material mat, int nextRecDepth) {
		Vector3 transColor = new Vector3(0, 0, 0);
		if (mat.getTransparency() > 0) {
			Vector3 delta = ray.getDirection().cpy().multiply(0.0001);
			Ray trasRay = new Ray(inter.getIntersectionPoint().cpy().add(delta), ray.getDirection());
			transColor = calculateColor(trasRay, nextRecDepth, inter.getShape());
		}

		return transColor;
	}

	/**
	 * calculates reflection colors
	 */
	Vector3 reflectionColor(IntersectionData inter, Ray ray, Material mat, int nextRecDepth) {
		Vector3 normal = inter.getShape().normal(inter.getIntersectionPoint(), ray);
		Vector3 dir = ray.getDirection().normalize();
		double dot = dir.dotProduct(normal);

		Vector3 reflectionVector = dir.cpy().add(normal.cpy().multiply(-2 * dot)).normalize();
		Ray reflectionRay = new Ray(inter.getIntersectionPoint(), reflectionVector);

		return calculateColor(reflectionRay, nextRecDepth, inter.getShape()).multiply(mat.getReflectionColor());
	}

	/**
	 * Calculates closest shape that intersects with given ray. returns null if no
	 * shape intersects the ray.
	 * 
	 * @param ray
	 * @return
	 */
	public IntersectionData calcIntersection(Ray ray, Shape excludeShape) {

		double tmpDistance;
		double minDistance = Double.POSITIVE_INFINITY;
		IntersectionData intersection = null;

		for (Shape shape : this.shapes) {
			if (excludeShape != null && shape.equals(excludeShape))
				continue;
			Vector3 intersectionPoint = shape.intersection(ray);
			if (intersectionPoint == null)
				continue;

			if ((tmpDistance = ray.getOriginPoint().distance(intersectionPoint)) < minDistance) {
				minDistance = tmpDistance;
				intersection = new IntersectionData(shape, intersectionPoint);
			}
		}

		return intersection;
	}

	/**
	 * Returns list of shapes that block this ray before hitting the target shape
	 * 
	 * @param ray
	 * @param targetShape
	 * @return
	 */
	public List<Shape> blockingShapes(Ray ray, Shape targetShape) {
		Map<Shape, Double> shapesMap = new HashMap<>();

		double maxDistance = Double.MAX_VALUE;
		for (Shape shape : this.shapes) {

			Vector3 interPoint = shape.intersection(ray);

			if (interPoint != null) {
				IntersectionData inter = new IntersectionData(shape, interPoint);
				double distance = ray.getOriginPoint().connectingVector(inter.getIntersectionPoint()).norm();

				if (targetShape == shape) {
					maxDistance = distance;
					continue;
				}
				shapesMap.put(shape, distance);
			}
		}

		List<Shape> result = new ArrayList<>();
		for (Shape shape : shapesMap.keySet()) {
			if (shapesMap.get(shape) < maxDistance)
				result.add(shape);
		}

		return result;

	}

	public Vector3 calculateColor(Ray ray) {
		return calculateColor(ray, 1, null);
	}

	public Vector3 calculateColor(Ray ray, int recursionDepth, Shape excludeShape) {
		// We limit recursion depth
		if (recursionDepth > this.settings.getMaxRecursionLevel()) {
			return new Vector3(0, 0, 0);
		}

		recursionDepth += 1;

		IntersectionData inter = calcIntersection(ray, excludeShape);

		// If Ray does not intersect any body, return the background color
		if (inter == null)
			return this.settings.getBackgroundColor();

		Material mat = this.materials.get(inter.getShape().getMaterial());

		//////////////////////////////////////////////////////////////////
		// Diffuse and sepecular color calculation
		Vector3 diffuseAndSpecColor = diffuseAndSpecColor(inter, ray, mat);

		//////////////////////////////////////////////////////////////////
		// Transparency Color calculation
		Vector3 transColor = transparencyColor(inter, ray, mat, recursionDepth);

		//////////////////////////////////////////////////////////////////
		// Reflection Color calculation
		Vector3 reflectionColor = reflectionColor(inter, ray, mat, recursionDepth);

		Vector3 result = new Vector3(0, 0, 0);
		result.add(transColor.cpy().multiply(mat.getTransparency()));
		result.add(diffuseAndSpecColor.cpy().multiply(1 - mat.getTransparency()));
		result.add(reflectionColor);

		return result.boundFromAbove(1, 1, 1);
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public List<Light> getLights() {
		return lights;
	}

	public void setLights(List<Light> lights) {
		this.lights = lights;
	}

	public List<Shape> getShapes() {
		return shapes;
	}

	public void setShapes(List<Shape> shapes) {
		this.shapes = shapes;
	}

	public List<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(List<Material> materials) {
		this.materials = materials;
	}

}
