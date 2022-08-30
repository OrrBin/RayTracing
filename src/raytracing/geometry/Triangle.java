package raytracing.geometry;

import raytracing.actors.Ray;
import raytracing.math.Vector3;
import raytracing.util.Constants;

public class Triangle extends Shape {

	private final Vector3 p1;
	private final Vector3 p2;
	private final Vector3 p3;

	private final Vector3 normal;
	private final double offset;
	private final Vector3 p1ConnectingVectorTop2;
	private final Vector3 p1ConnectingVectorTop3;


	public Triangle(Vector3 vertex1, Vector3 vertex2, Vector3 vertex3, int material) {
		super(material);
		this.p1 = vertex1;
		this.p2 = vertex2;
		this.p3 = vertex3;

		Vector3 v = p1.cpy().add(p2.cpy().multiply(-1));
		Vector3 u = p1.cpy().add(p3.cpy().multiply(-1));

		normal = v.crossProduct(u).normalize();
		offset = normal.cpy().dotProduct(p1);

		p1ConnectingVectorTop2 = p1.connectingVector(p2);
		p1ConnectingVectorTop3 = p1.connectingVector(p3);
	}

	@Override
	public Vector3 intersection(Ray ray) {
		Vector3 normalCpy = normal.cpy();

		double angle = ray.getDirection().dotProduct(normalCpy);

		if (Math.abs(angle) < Constants.EPSILON)
			return null;

		double t = (offset - ray.getOriginPoint().dotProduct(normalCpy)) / angle;
		if (t < 0)
			return null;

		Vector3 pointOfContact = ray.getOriginPoint().add(ray.getDirection().multiply(t));
		Vector3 w = p1.connectingVector(pointOfContact);

		double a = (p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop3) * w.dotProduct(p1ConnectingVectorTop3) - p1ConnectingVectorTop3.dotProduct(p1ConnectingVectorTop3) * w.dotProduct(p1ConnectingVectorTop2))
				/ (Math.pow(p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop3), 2) - p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop2) * p1ConnectingVectorTop3.dotProduct(p1ConnectingVectorTop3));
		double b = (p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop3) * w.dotProduct(p1ConnectingVectorTop2) - p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop2) * w.dotProduct(p1ConnectingVectorTop3))
				/ (Math.pow(p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop3), 2) - p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop2) * p1ConnectingVectorTop3.dotProduct(p1ConnectingVectorTop3));

		if (a >= 0 && b >= 0 && a + b <= 1)
			return pointOfContact;

		return null;
	}

	@Override
	public Vector3 normal(Vector3 point, Ray ray) {
		Vector3 normalCpy = normal.cpy();

		Vector3 v = ray.getDirection().multiply(-1).normalize();
		if (normalCpy.dotProduct(v) < 0)
			return normalCpy.multiply(-1);
		return normalCpy;
	}

}
