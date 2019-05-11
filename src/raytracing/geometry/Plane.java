package raytracing.geometry;

import raytracing.util.Constants;
import raytracing.util.Ray;
import raytracing.util.Vector3;

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

		Vector3 intersectionPoint = ray.getOriginPoint().add(ray.getDirection().multiply(t));

		return intersectionPoint;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((normal == null) ? 0 : normal.hashCode());
		long temp;
		temp = Double.doubleToLongBits(offset);
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
		Plane other = (Plane) obj;
		if (normal == null) {
			if (other.normal != null)
				return false;
		} else if (!normal.equals(other.normal))
			return false;
		if (Double.doubleToLongBits(offset) != Double.doubleToLongBits(other.offset))
			return false;
		return true;
	}

}
