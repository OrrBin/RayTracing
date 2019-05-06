package raytracing;

import java.awt.Color;

public class Material {
	private Color diffuseColor;
	private Color specularColor;
	private Color reflectionColor;
	private double phongSpecularityCoefficient;
	private double transparency;

	public Material(Color diffuseColor, Color specularColor, Color reflectionColor, double phongSpecularityCoefficient,
			double transparency) {
		super();
		this.diffuseColor = diffuseColor;
		this.specularColor = specularColor;
		this.reflectionColor = reflectionColor;
		this.phongSpecularityCoefficient = phongSpecularityCoefficient;
		this.transparency = transparency;
	}

	public Color getDiffuseColor() {
		return diffuseColor;
	}

	public void setDiffuseColor(Color diffuseColor) {
		this.diffuseColor = diffuseColor;
	}

	public Color getSpecularColor() {
		return specularColor;
	}

	public void setSpecularColor(Color specularColor) {
		this.specularColor = specularColor;
	}

	public Color getReflectionColor() {
		return reflectionColor;
	}

	public void setReflectionColor(Color reflectionColor) {
		this.reflectionColor = reflectionColor;
	}

	public double getPhongSpecularityCoefficient() {
		return phongSpecularityCoefficient;
	}

	public void setPhongSpecularityCoefficient(double phongSpecularityCoefficient) {
		this.phongSpecularityCoefficient = phongSpecularityCoefficient;
	}

	public double getTransparency() {
		return transparency;
	}

	public void setTransparency(double transparency) {
		this.transparency = transparency;
	}

}
