package raytracing.util;

public class Ray {
	Vector3 position;
	Vector3 direction;

	public Ray(Vector3 position, Vector3 angle) {
		super();
		this.position = position;
		this.direction = angle;
	}

	public Vector3 point(double distance) {
		return position.add(direction.multiplyScalar(distance));
	}
	
	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Vector3 getAngle() {
		return direction;
	}

	public void setAngle(Vector3 angle) {
		this.direction = angle;
	}

}
