package raytracing;

import raytracing.util.Vector3;

public class Material {
	private Vector3 diffuseColor;
	private Vector3 specularColor;
	private Vector3 reflectionColor;
	private double phongSpecularityCoefficient;
	private double transparency;

	public Material() {
	}

	public Material(Vector3 diffuseColor, Vector3 specularColor, Vector3 reflectionColor,
			double phongSpecularityCoefficient, double transparency) {
		this.diffuseColor = diffuseColor;
		this.specularColor = specularColor;
		this.reflectionColor = reflectionColor;
		this.phongSpecularityCoefficient = phongSpecularityCoefficient;
		this.transparency = transparency;
	}

	public Vector3 getDiffuseColor() {
		return diffuseColor.cpy();
	}

	public void setDiffuseColor(Vector3 diffuseColor) {
		this.diffuseColor = diffuseColor;
	}

	public Vector3 getSpecularColor() {
		return specularColor.cpy();
	}

	public void setSpecularColor(Vector3 specularColor) {
		this.specularColor = specularColor;
	}

	public Vector3 getReflectionColor() {
		return reflectionColor.cpy();
	}

	public void setReflectionColor(Vector3 reflectionColor) {
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
