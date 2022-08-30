package raytracing.actors;

import raytracing.math.Vector3;

public class Ray {
	public Vector3 originPoint;
	public Vector3 direction;

	public Ray(Vector3 position, Vector3 angle) {
		super();
		this.originPoint = position;
		this.direction = angle;
	}

	public Vector3 point(double distance) {
		return originPoint.add(direction.multiply(distance));
	}

	public Vector3 getOriginPoint() {
		return originPoint.cpy();
	}

	public void setOriginPoint(Vector3 position) {
		this.originPoint = position;
	}

	public Vector3 getDirection() {
		return direction.cpy();
	}

	public void setDirection(Vector3 angle) {
		this.direction = angle;
	}

	public Vector3 pointAtDistance(double t) {
		return this.originPoint.cpy().add(
				this.direction.cpy().multiply(t));
	}

	@Override
	public String toString() {
		return String.format("position: %s, direction: %s", originPoint, direction);
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
//		result = prime * result + ((originPoint == null) ? 0 : originPoint.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Ray other = (Ray) obj;
//		if (direction == null) {
//			if (other.direction != null)
//				return false;
//		} else if (!direction.equals(other.direction))
//			return false;
//		if (originPoint == null) {
//			if (other.originPoint != null)
//				return false;
//		} else if (!originPoint.equals(other.originPoint))
//			return false;
//		return true;
//	}

}
