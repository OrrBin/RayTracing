package raytracing.geometry;

import raytracing.actors.Ray;
import raytracing.math.Vector3;
import raytracing.util.Constants;

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
		Vector3 centerToOrigin = center.connectingVector(ray.originPoint);

		double b = 2.0 * ray.direction.dotProduct(centerToOrigin);
		double c = Math.pow(centerToOrigin.norm(), 2) - Math.pow(radius, 2);

		double discriminant = Math.pow(b, 2) - 4 * c;
		if (discriminant < 0)
			return null;

		if (discriminant - Constants.EPSILON <= 0) {
			double t = (-b) / (2);

			if (t < 0)
				return null;

			return ray.getOriginPointCpy().add(ray.direction.multiply(t));
		}
		double t1 = (-b + Math.sqrt(discriminant)) / (2);
		double t2 = (-b - Math.sqrt(discriminant)) / (2);

		if (t1 < 0 && t2 < 0)
			return null;

		Vector3 intersectionPoint1 = ray.getOriginPointCpy().add(ray.direction.multiply(t1));
		Vector3 intersectionPoint2 = ray.getOriginPointCpy().add(ray.direction.multiply(t2));

		Vector3 intersectionPoint;

		if (t2 < 0)
			intersectionPoint = intersectionPoint1;
		else if (t1 < 0)
			intersectionPoint = intersectionPoint2;
		else {
			double length1 = ray.originPoint.connectingVector(intersectionPoint1).norm();
			double length2 = ray.originPoint.connectingVector(intersectionPoint2).norm();

			if (length1 < length2)
				intersectionPoint = intersectionPoint1;
			else
				intersectionPoint = intersectionPoint2;
		}

		return intersectionPoint;
	}

	@Override
	public Vector3 normal(Vector3 point, Ray ray) {
		Vector3 n = center.connectingVector(point).normalize();

		Vector3 v = ray.direction.multiply(-1).normalize();
		if (n.dotProduct(v) < 0)
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

}
