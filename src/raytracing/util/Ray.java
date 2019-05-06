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
		return position.add(direction.multiply(distance));
	}
	
	public Vector3 getPosition() {
		return new Vector3(position.getX(), position.getY(), position.getY());
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Vector3 getDirection() {
		return new Vector3(direction.getX(), direction.getY(), direction.getY());
	}

	public void setDirection(Vector3 angle) {
		this.direction = angle;
	}

}
