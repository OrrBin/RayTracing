package raytracing.geometry;

import raytracing.util.Ray;
import raytracing.util.Vector3;

public abstract class Shape {
	
	protected int material;
	
	abstract Vector3 intersection(Ray ray);
	abstract Vector3 normal(Vector3 point, Ray ray);
	
	public int getMaterial() {
		return material;
	}
	public void setMaterial(int material) {
		this.material = material;
	}
}
