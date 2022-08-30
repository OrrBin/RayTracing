package raytracing.geometry;

import raytracing.actors.Ray;
import raytracing.math.Vector3;
import raytracing.util.Constants;

public class Plane extends Shape {

	private Vector3 normal;
	private double offset;

	public Plane() {

	}

	public Plane(Vector3 normal, double offset, int material) {
		super(material);
		this.normal = normal;
		this.offset = offset;
	}

	@Override
	public Vector3 intersection(Ray ray) {
		double angle = ray.getDirection().dotProduct(normal);

		if (Math.abs(angle) < Constants.EPSILON)
			return null;

		double t = (this.offset - ray.getOriginPoint().dotProduct(normal)) / angle;
		if (t < 0)
			return null;

		return ray.getOriginPoint().add(ray.getDirection().multiply(t));
	}

	@Override
	public Vector3 normal(Vector3 point, Ray ray) {
		Vector3 n = normal.normalize();

		Vector3 v = ray.getDirection().multiply(-1).normalize();
		if (n.dotProduct(v) < 0)
			return n.multiply(-1);
		return n;
	}

	public Vector3 getNormal() {
		return normal.cpy();
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
