package raytracing;

import raytracing.util.Vector3;

public class Camera {

	private Vector3 position;
	private Vector3 lookAtPoint;
	private Vector3 upVector;
	private double screenDistance;
	private double screenWidth;

	public Camera() {
		
	};

	public Camera(Vector3 position, Vector3 lookAtPoint, Vector3 upVector, double screenDistance, double screenWidth) {
		super();
		this.position = position;
		this.lookAtPoint = lookAtPoint;
		this.upVector = upVector;
		this.screenDistance = screenDistance;
		this.screenWidth = screenWidth;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Vector3 getLookAtPoint() {
		return lookAtPoint;
	}

	public void setLookAtPoint(Vector3 lookAtPoint) {
		this.lookAtPoint = lookAtPoint;
	}

	public Vector3 getUpVector() {
		return upVector;
	}

	public void setUpVector(Vector3 upVector) {
		this.upVector = upVector;
	}

	public double getScreenDistance() {
		return screenDistance;
	}

	public void setScreenDistance(double screenDistance) {
		this.screenDistance = screenDistance;
	}

	public double getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(double screenWidth) {
		this.screenWidth = screenWidth;
	}

}
