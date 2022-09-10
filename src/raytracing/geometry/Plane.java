package raytracing.geometry;

import lombok.AllArgsConstructor;
import raytracing.actors.Ray;
import raytracing.math.Vector3;
import raytracing.util.Constants;

@AllArgsConstructor
public class Plane extends Shape {

	private Vector3 normal;
	private double offset;

	public Plane(Vector3 normal, double offset, int material) {
		super(material);
		this.normal = normal.normalize();
		this.offset = offset;
	}

	@Override
	public Vector3 intersection(Ray ray) {
		double angle = ray.direction.dotProduct(normal);

		if (Math.abs(angle) < Constants.EPSILON)
			return null;

		double t = (this.offset - ray.originPoint.dotProduct(normal)) / angle;
		if (t < 0)
			return null;

		return ray.getOriginPointCpy().addInPlace(ray.direction.multiply(t));
	}

	@Override
	public Vector3 normal(Vector3 point, Ray ray) {

		Vector3 v = ray.direction.multiply(-1).normalize();
		if (normal.dotProduct(v) < 0)
			return normal.cpy().multiply(-1);
		return normal.cpy();
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
