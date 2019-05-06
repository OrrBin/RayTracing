package raytracing.geometry;

import raytracing.util.Constants;
import raytracing.util.Ray;
import raytracing.util.Vector3;

public class Plane extends Shape {

	private Vector3 normal;
	private double offset;

	public Plane(Vector3 normal, double offset) {
		super();
		this.normal = normal;
		this.offset = offset;
	}

	@Override
	public Vector3 intersection(Ray ray) {
		double angle = ray.getDirection().dotProduct(normal);
		
		if(Math.abs(angle) < Constants.EPSILON)
			return null;
		
		double t = (this.offset - ray.getPosition().dotProduct(normal)) / angle;
		if(t < 0)
			return null;
		
		Vector3 intersectionPoint = ray.getPosition().add(ray.getDirection().multiply(t));	
		
		return intersectionPoint;
	}

	@Override
	public Vector3 normal(Vector3 point, Ray ray) {
		Vector3 n = normal.normalize();
		
		Vector3 v = ray.getDirection().multiply(-1).normalize();
		if(n.dotProduct(v) < 0)
			return n.multiply(-1);
		return n;
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
