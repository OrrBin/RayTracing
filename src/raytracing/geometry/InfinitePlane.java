package raytracing.geometry;

import raytracing.util.Ray;
import raytracing.util.Vector3;

public class InfinitePlane extends Shape {

	private Vector3 normal;
	private double offset;

	public InfinitePlane(Vector3 normal, double offset) {
		super();
		this.normal = normal;
		this.offset = offset;
	}

	@Override
	public Vector3 intersection(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector3 normal(Vector3 point, Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector3 getNormal() {
		return normal;
	}

	public void setNormal(Vector3 normal) {
		this.normal = normal;
	}

	public double getOffset() {
		return offset;
	}

	public void setOffset(double offset) {
		this.offset = offset;
	}

}
