package raytracing.util;

public class Vector3 {
	private double x;
	private double y;
	private double z;

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public Vector3 normalize() {
		if(norm() < Constants.EPSILON) // "equals zero"
			return this;
		
		return new Vector3(x,y,z).multiply(1/norm());
	}
	
	public Vector3 crossProduct(Vector3 other) {
		return new Vector3(y*other.z - other.y*z, other.x*z - z*other.x, x*other.y - y*other.x);
	}

	public Vector3 add(Vector3 other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;

		return this;
	}

	public Vector3 multiply(double a) {
		this.x *= a;
		this.y *= a;
		this.z *= a;

		return this;
	}

	public Vector3 connectingVector(Vector3 other) {
		return new Vector3(x,y,z).add(other.multiply(-1));
	}
	
	public double dotProduct(Vector3 other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	public double angle(Vector3 other) {
		return Math.acos(dotProduct(other) / (norm() * other.norm()));
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

}
