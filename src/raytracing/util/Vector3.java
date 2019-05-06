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

	public double crossProduct(Vector3 other) {
		return norm() * other.norm() * Math.sin(angle(other));
	}

	public Vector3 add(Vector3 other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;

		return this;
	}

	public Vector3 multiplyScalar(double a) {
		this.x *= a;
		this.y *= a;
		this.z *= a;

		return this;
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
