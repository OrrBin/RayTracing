package raytracing.geometry;

import raytracing.util.Ray;
import raytracing.util.Vector3;

public class Sphere implements Shape {

	private Vector3 center;
	private double radius;
	
	public Sphere() {
	}
	
	public Sphere(Vector3 center, double radius) {
		this.center = center;
		this.radius = radius;
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

	public Vector3 getCenter() {
		return center;
	}

	public void setCenter(Vector3 center) {
		this.center = center;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	

}
