package raytracing.actors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raytracing.math.Vector3;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Camera {

	private Vector3 position;
	private Vector3 lookAtPoint;
	private Vector3 upVector;
	private double  screenDistance;
	private double  screenWidth;

	//Coordinate system attributes
	private Vector3 normal;
	private Vector3 right, top, forward;

	public Camera(Vector3 position, Vector3 lookAtPoint, Vector3 upVector, double screenDistance, double screenWidth) {
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
		createCoordSystem();
	}

	public Vector3 getLookAtPoint() {
		return lookAtPoint.cpy();
	}

	public Vector3 getUpVector() {
		return upVector.cpy();
	}

	public Vector3 getNormal() {
		return normal.cpy();
	}

	public Vector3 getRight() {
		return right.cpy();
	}

	public Vector3 getTop() {
		return top.cpy();
	}

	public Vector3 getForward() {
		return forward.cpy();
	}



}


