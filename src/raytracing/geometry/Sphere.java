package raytracing.geometry;

import raytracing.util.Constants;
import raytracing.util.Ray;
import raytracing.util.Vector3;

public class Sphere extends Shape {

	private Vector3 center;
	private double radius;
	
	public Sphere() {
	}
	
	public Sphere(Vector3 center, double radius, int material) {
		super(material);
		this.center = center;
		this.radius = radius;
	}

	@Override
	public Vector3 intersection(Ray ray) {
		Vector3 centerToOrigin = center.connectingVector(ray.getPosition());
		
		double b = 2.0 * ray.getDirection().dotProduct(centerToOrigin);
		double c = Math.pow(centerToOrigin.norm(), 2) - Math.pow(radius, 2);
		
		double discriminant = Math.pow(b,  2) - 4 * c;
		if(discriminant < 0)
			return null;
		
		if(discriminant - Constants.EPSILON <= 0) {
			double t = (-b)/(2);
			
			if(t < 0)
				return null;
			
			Vector3 intersectionPoint = ray.getPosition().add(ray.getDirection().multiply(t));
			return intersectionPoint;
	}
		double t1 = (-b + Math.sqrt(discriminant))/(2);
		double t2 = (-b - Math.sqrt(discriminant))/(2);
		
		if(t1 < 0 && t2 < 0)
			return null;
		
		Vector3 intersectionPoint1 = ray.getPosition().add(ray.getDirection().multiply(t1));
		Vector3 intersectionPoint2 = ray.getPosition().add(ray.getDirection().multiply(t2));
		
		Vector3 intersectionPoint = null;
		
		if(t2 < 0)
			intersectionPoint = intersectionPoint1;
		else if(t1 < 0)
			intersectionPoint = intersectionPoint2;
		else {
			double length1 = ray.getPosition().connectingVector(intersectionPoint1).norm();
			double length2 = ray.getPosition().connectingVector(intersectionPoint2).norm();
			
			if(length1 < length2)
				intersectionPoint = intersectionPoint1;
			else
				intersectionPoint = intersectionPoint2;
		}
		
		return intersectionPoint;
	}
		
	@Override
	public Vector3 normal(Vector3 point, Ray ray) {
		Vector3 n = center.connectingVector(point).normalize();
		
		Vector3 v = ray.getDirection().multiply(-1).normalize();
		if(n.dotProduct(v) < 0)
			return n.multiply(-1);
		return n;
	}

	public Vector3 getCenter() {
		return center.cpy();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((center == null) ? 0 : center.hashCode());
		long temp;
		temp = Double.doubleToLongBits(radius);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sphere other = (Sphere) obj;
		if (center == null) {
			if (other.center != null)
				return false;
		} else if (!center.equals(other.center))
			return false;
		if (Double.doubleToLongBits(radius) != Double.doubleToLongBits(other.radius))
			return false;
		return true;
	}
	
	
	

}
