package raytracing;

import raytracing.util.Vector3;

public class Material {
	private Vector3 diffuseVector3;
	private Vector3 specularVector3;
	private Vector3 reflectionVector3;
	private double phongSpecularityCoefficient;
	private double transparency;

	public Material() {
	}
	
	public Material(Vector3 diffuseVector3, Vector3 specularVector3, Vector3 reflectionVector3, double phongSpecularityCoefficient,
			double transparency) {
		super();
		this.diffuseVector3 = diffuseVector3;
		this.specularVector3 = specularVector3;
		this.reflectionVector3 = reflectionVector3;
		this.phongSpecularityCoefficient = phongSpecularityCoefficient;
		this.transparency = transparency;
	}

	public Vector3 getDiffuseColor() {
		return diffuseVector3.cpy();
	}

	public void setDiffuseColor(Vector3 diffuseVector3) {
		this.diffuseVector3 = diffuseVector3;
	}

	public Vector3 getSpecularColor() {
		return specularVector3.cpy();
	}

	public void setSpecularColor(Vector3 specularVector3) {
		this.specularVector3 = specularVector3;
	}

	public Vector3 getReflectionColor() {
		return reflectionVector3.cpy();
	}

	public void setReflectionColor(Vector3 reflectionVector3) {
		this.reflectionVector3 = reflectionVector3;
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
