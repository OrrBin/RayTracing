package raytracing;

import raytracing.util.Vector3;

public class Camera {

	private Vector3 position;
	private Vector3 lookAtPoint;
	private Vector3 upVector;
	private double screenDistance;
	private double screenWidth;

	private Vector3 normal;
	private Vector3 right, top, forward;

	public Camera(Vector3 position, Vector3 lookAtPoint, Vector3 upVector, double screenDistance, double screenWidth) {
		super();
		this.position = position;
		this.lookAtPoint = lookAtPoint;
		this.upVector = upVector;
		this.screenDistance = screenDistance;
		this.screenWidth = screenWidth;

		createCoordSystem();
	}

	private void createCoordSystem() {
		this.normal = this.position.connectingVector(this.lookAtPoint);
		this.forward = this.normal.normalize();
		this.right = this.upVector.crossProduct(this.normal).normalize();
		this.top = this.forward.crossProduct(this.right);
	}

	public Vector3 getPosition() {
		return position.cpy();
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Vector3 getLookAtPoint() {
		return lookAtPoint.cpy();
	}

	public void setLookAtPoint(Vector3 lookAtPoint) {
		this.lookAtPoint = lookAtPoint;
	}

	public Vector3 getUpVector() {
		return upVector.cpy();
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

	public Vector3 getNormal() {
		return normal.cpy();
	}

	public void setNormal(Vector3 normal) {
		this.normal = normal;
	}

	public Vector3 getRight() {
		return right.cpy();
	}

	public void setRight(Vector3 right) {
		this.right = right;
	}

	public Vector3 getTop() {
		return top.cpy();
	}

	public void setTop(Vector3 top) {
		this.top = top;
	}

	public Vector3 getForward() {
		return forward.cpy();
	}

	public void setForward(Vector3 forward) {
		this.forward = forward;
	}

	
}
