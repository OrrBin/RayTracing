package raytracing;

import java.util.Random;

import raytracing.util.Ray;
import raytracing.util.Vector3;

public class Light {
	private Vector3 position;
	private Vector3 color;
	private double specularIntensity;
	private double shadowIntensity;
	private double lightRadius;

	public Light() {
	}

	public Light(Vector3 position, Vector3 color, double specularIntensity, double shadowIntensity,
			double lightRadius) {
		this.position = position;
		this.color = color;
		this.specularIntensity = specularIntensity;
		this.shadowIntensity = shadowIntensity;
		this.lightRadius = lightRadius;
	}

	/**
	 * Calculate multiple rays for this light source to create shadows. We create
	 * numOfShadowRays rays, from around the light
	 */
	public Ray[] getRays(Vector3 intersectionPoint, int numOfShadowRays) {
		Ray[] rays = new Ray[numOfShadowRays * numOfShadowRays];

		Vector3 normal = this.position.connectingVector(intersectionPoint).normalize();
		Vector3 up = new Vector3(0, 1, 0);
		Vector3 right = up.crossProduct(normal).normalize();
		Vector3 top = normal.crossProduct(right);

		Random rnd = new Random();

		double rectWidth = this.lightRadius / numOfShadowRays;
		for (int i = 0; i < numOfShadowRays; i++) {
			double yMin = rectWidth * i - this.lightRadius / 2;
			for (int j = 0; j < numOfShadowRays; j++) {
				double xMin = rectWidth * j - this.lightRadius / 2;
				double xOffset = xMin + rnd.nextDouble() * rectWidth;
				double yOffset = yMin + rnd.nextDouble() * rectWidth;

				Vector3 light = this.position.cpy().add(top.cpy().multiply(yOffset)).add(right.cpy().multiply(xOffset));
				rays[i * numOfShadowRays + j] = new Ray(light, light.connectingVector(intersectionPoint).normalize());
			}
		}

		return rays;
	}

	public Vector3 getPosition() {
		return position.cpy();
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Vector3 getColor() {
		return color.cpy();
	}

	public void setColor(Vector3 color) {
		this.color = color;
	}

	public double getSpecularIntensity() {
		return specularIntensity;
	}

	public void setSpecularIntensity(double specularIntensity) {
		this.specularIntensity = specularIntensity;
	}

	public double getShadowIntensity() {
		return shadowIntensity;
	}

	public void setShadowIntensity(double shadowIntensity) {
		this.shadowIntensity = shadowIntensity;
	}

	public double getLightRadius() {
		return lightRadius;
	}

	public void setLightRadius(double lightRadius) {
		this.lightRadius = lightRadius;
	}

}
