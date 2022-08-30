package raytracing.geometry;

import raytracing.actors.Ray;
import raytracing.math.Vector3;

public abstract class Shape {

	protected int material;

	public abstract Vector3 intersection(Ray ray);

	public abstract Vector3 normal(Vector3 point, Ray ray);

	public Shape() {
	}

	public Shape(int material) {
		this.material = material;
	}

	public int getMaterial() {
		return material;
	}

	public void setMaterial(int material) {
		this.material = material;
	}
}
