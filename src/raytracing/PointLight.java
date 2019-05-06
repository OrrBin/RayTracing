package raytracing;

import java.awt.Color;

import raytracing.util.Vector3;

public class PointLight {
	private Vector3 position;
	private Color color;
	private double specularIntensity;
	private double shadowIntensity;
	private double lightRadius;

	public PointLight(Vector3 position, Color color, double specularIntensity, double shadowIntensity,
			double lightRadius) {
		super();
		this.position = position;
		this.color = color;
		this.specularIntensity = specularIntensity;
		this.shadowIntensity = shadowIntensity;
		this.lightRadius = lightRadius;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
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
