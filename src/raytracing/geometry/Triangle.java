package raytracing.geometry;

import raytracing.util.Ray;
import raytracing.util.Vector3;

public class Triangle implements Shape {

	private Vector3 vertex1;
	private Vector3 vertex2;
	private Vector3 vertex3;

	public Triangle(Vector3 vertex1, Vector3 vertex2, Vector3 vertex3) {
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.vertex3 = vertex3;
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

	public Vector3 getVertex1() {
		return vertex1;
	}

	public void setVertex1(Vector3 vertex1) {
		this.vertex1 = vertex1;
	}

	public Vector3 getVertex2() {
		return vertex2;
	}

	public void setVertex2(Vector3 vertex2) {
		this.vertex2 = vertex2;
	}

	public Vector3 getVertex3() {
		return vertex3;
	}

	public void setVertex3(Vector3 vertex3) {
		this.vertex3 = vertex3;
	}

}
