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

	public Vector3 getOriginPointCpy() {
		return originPoint.cpy();
	}

	public Vector3 pointAtDistance(double t) {
		return this.originPoint.cpy().add(
				this.direction.multiply(t));
	}

	@Override
	public String toString() {
		return String.format("position: %s, direction: %s", originPoint, direction);
	}

}
