package raytracing.actors;

import raytracing.geometry.Shape;
import raytracing.math.Vector3;

public class IntersectionData {
	private Shape shape;
	private Vector3 intersectionPoint;

	public IntersectionData(Shape shape, Vector3 intersectionPoint) {
		super();
		this.shape = shape;
		this.intersectionPoint = intersectionPoint;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public Vector3 getIntersectionPoint() {
		return intersectionPoint.cpy();
	}

	public void setIntersectionPoint(Vector3 intersectionPoint) {
		this.intersectionPoint = intersectionPoint;
	}

}
