package raytracing.geometry;

import raytracing.util.Constants;
import raytracing.util.Ray;
import raytracing.util.Vector3;

public class Triangle extends Shape {

	private Vector3 p1;
	private Vector3 p2;
	private Vector3 p3;

	public Triangle(Vector3 vertex1, Vector3 vertex2, Vector3 vertex3) {
		this.p1 = vertex1;
		this.p2 = vertex2;
		this.p3 = vertex3;
	}

	@Override
	public Vector3 intersection(Ray ray) {
        Vector3 normal = normal();
        double offset = normal.dotProduct(p1);
        double angle = ray.getDirection().dotProduct(normal);

        if(Math.abs(angle) < Constants.EPSILON)
            return null;

        double t = (offset - ray.getPosition().dotProduct(normal)) / angle;
        if(t < 0)
            return null;

        Vector3 pointOfContact = ray.getPosition().add(ray.getDirection().multiply(t));

        Vector3 u = p1.connectingVector(p2);
        Vector3 v = p1.connectingVector(p3);
        Vector3 w = p1.connectingVector(pointOfContact);

        double a = (u.dotProduct(v))*w.dotProduct(v) - v.dotProduct(v)*w.dotProduct(u)) / (Math.pow(u.dotProduct(v), 2) - u.dotProduct(u)*v.dotProduct(v));
        double b = (Vector.dot(u, v)*Vector.dot(w, u) - Vector.dot(u, u)*Vector.dot(w, v)) / (Math.pow(Vector.dot(u, v), 2) - Vector.dot(u, u)*Vector.dot(v, v));

        if (a >= 0 && b >= 0 && a+b <= 1)
            return new Intersection(scene, this, ray, pointOfContact);
        return null;
	}

	@Override
	public Vector3 normal(Vector3 point, Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}

	private Vector3 normal() {
		Vector3 v = p1.add(p2.multiply(-1));
        Vector3 u = p1.add(p3.multiply(-1));
        return v.crossProduct(u).normalize();
	} 
	
	public Vector3 getVertex1() {
		return p1;
	}

	public void setVertex1(Vector3 vertex1) {
		this.p1 = vertex1;
	}

	public Vector3 getVertex2() {
		return p2;
	}

	public void setVertex2(Vector3 vertex2) {
		this.p2 = vertex2;
	}

	public Vector3 getVertex3() {
		return p3;
	}

	public void setVertex3(Vector3 vertex3) {
		this.p3 = vertex3;
	}

}
