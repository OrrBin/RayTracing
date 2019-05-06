package raytracing.util;

public class Ray {
	Vector3 position;
	double angle;

	public Ray(Vector3 position, double angle) {
		super();
		this.position = position;
		this.angle = angle;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

}
