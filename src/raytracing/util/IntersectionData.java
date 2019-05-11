package raytracing.util;

import raytracing.geometry.Shape;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((intersectionPoint == null) ? 0 : intersectionPoint.hashCode());
		result = prime * result + ((shape == null) ? 0 : shape.hashCode());
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
		IntersectionData other = (IntersectionData) obj;
		if (intersectionPoint == null) {
			if (other.intersectionPoint != null)
				return false;
		} else if (!intersectionPoint.equals(other.intersectionPoint))
			return false;
		if (shape == null) {
			if (other.shape != null)
				return false;
		} else if (!shape.equals(other.shape))
			return false;
		return true;
	}

}
