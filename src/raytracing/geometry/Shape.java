package raytracing.geometry;

import raytracing.util.Ray;
import raytracing.util.Vector3;

public abstract class Shape {
	
	protected int materialIndex;
	
	abstract Vector3 intersection(Ray ray);
	abstract Vector3 normal(Vector3 point, Ray ray);
	
	public int getMaterialIndex() {
		return materialIndex;
	}
	public void setMaterialIndex(int materialIndex) {
		this.materialIndex = materialIndex;
	}
}
