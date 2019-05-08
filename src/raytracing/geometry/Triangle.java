package raytracing.geometry;

import raytracing.util.Constants;
import raytracing.util.Ray;
import raytracing.util.Vector3;

public class Triangle extends Shape {

	private Vector3 p1;
	private Vector3 p2;
	private Vector3 p3;

	public Triangle() {
	}
	
	public Triangle(Vector3 vertex1, Vector3 vertex2, Vector3 vertex3 ,int material) {
		super(material);
		this.p1 = vertex1;
		this.p2 = vertex2;
		this.p3 = vertex3;
	}

	@Override
	public Vector3 intersection(Ray ray) {
		Vector3 normal = normal();
		double offset = normal.dotProduct(p1);
		double angle = ray.getDirection().dotProduct(normal);

		if (Math.abs(angle) < Constants.EPSILON)
			return null;

		double t = (offset - ray.getPosition().dotProduct(normal)) / angle;
		if (t < 0)
			return null;

		Vector3 pointOfContact = ray.getPosition().add(ray.getDirection().multiply(t));

		Vector3 u = p1.connectingVector(p2);
		Vector3 v = p1.connectingVector(p3);
		Vector3 w = p1.connectingVector(pointOfContact);

		double a = (u.dotProduct(v) * w.dotProduct(v) - v.dotProduct(v) * w.dotProduct(u))
				/ (Math.pow(u.dotProduct(v), 2) - u.dotProduct(u) * v.dotProduct(v));
		double b = (u.dotProduct(v) * w.dotProduct(u) - u.dotProduct(u) * w.dotProduct(v))
				/ (Math.pow(u.dotProduct(v), 2) - u.dotProduct(u) * v.dotProduct(v));

		if (a >= 0 && b >= 0 && a + b <= 1)
			return pointOfContact;
		return null;
	}

	@Override
	public Vector3 normal(Vector3 point, Ray ray) {
		Vector3 n = normal();

		Vector3 v = ray.getDirection().multiply(-1).normalize();
		if (n.dotProduct(v) < 0)
			return n.multiply(-1);
		return n;
	}

	private Vector3 normal() {
		Vector3 v = p1.cpy().add(p2.cpy().multiply(-1));
		Vector3 u = p1.cpy().add(p3.cpy().multiply(-1));
		return v.crossProduct(u).normalize();
	}

	public Vector3 getVertex1() {
		return p1.cpy();
	}

	public void setVertex1(Vector3 vertex1) {
		this.p1 = vertex1;
	}

	public Vector3 getVertex2() {
		return p2.cpy();
	}

	public void setVertex2(Vector3 vertex2) {
		this.p2 = vertex2;
	}

	public Vector3 getVertex3() {
		return p3.cpy();
	}

	public void setVertex3(Vector3 vertex3) {
		this.p3 = vertex3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
		result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
		result = prime * result + ((p3 == null) ? 0 : p3.hashCode());
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
		Triangle other = (Triangle) obj;
		if (p1 == null) {
			if (other.p1 != null)
				return false;
		} else if (!p1.equals(other.p1))
			return false;
		if (p2 == null) {
			if (other.p2 != null)
				return false;
		} else if (!p2.equals(other.p2))
			return false;
		if (p3 == null) {
			if (other.p3 != null)
				return false;
		} else if (!p3.equals(other.p3))
			return false;
		return true;
	}

	
	
}
