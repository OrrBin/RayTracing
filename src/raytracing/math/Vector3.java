package raytracing.math;

import lombok.Getter;
import lombok.Setter;
import raytracing.util.Constants;

@Getter
@Setter
public class Vector3 {
	public double x;
	public double y;
	public double z;

//	final static Vector3Pool vector3Pool = new Vector3Pool(100, 1000000);

	public Vector3() {

	}

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3(Vector3 other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

	public Vector3 cpy() {
		return new Vector3(this);
//		final Vector3 cpy = vector3Pool.get();
//		cpy.setX(this.x);
//		cpy.setY(this.y);
//		cpy.setZ(this.z);
//
//		return cpy;
	}

//	public void release() {
//		vector3Pool.returnToPool(this);
//	}

	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public Vector3 normalize() {
		if (norm() < Constants.EPSILON)
			return cpy();

		return cpy().multiply(1 / norm());
	}

	public Vector3 crossProduct(Vector3 other) {
		double a = y * other.z - z * other.y;
		double b = z * other.x - x * other.z;
		double c = x * other.y - y * other.x;
		return new Vector3(a, b, c);
	}

	public Vector3 add(Vector3 other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;

		return this;
	}

	public Vector3 multiply(Vector3 other) {
		this.x *= other.x;
		this.y *= other.y;
		this.z *= other.z;

		return this;
	}

	public Vector3 multiply(double a) {
		return new Vector3(this.x * a, this.y * a, this.z * a);
	}

	public Vector3 connectingVector(Vector3 other) {
		return other.cpy().add(cpy().multiply(-1));
	}

	public double distance(Vector3 other) {
		return connectingVector(other).norm();
	}

	public double dotProduct(Vector3 other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	public double angle(Vector3 other) {
		return Math.acos(dotProduct(other) / (norm() * other.norm()));
	}

	/**
	 * Bounds the values of x,y,z from above
	 */
	public Vector3 boundFromAbove(int i, int j, int k) {
		x = Math.min(x, i);
		y = Math.min(y, j);
		z = Math.min(z, k);

		return this;
	}

	/**
	 * Bounds the values of x,y,z from below
	 */
	public Vector3 boundFromBelow(int i, int j, int k) {
		x = Math.max(x, i);
		y = Math.max(y, j);
		z = Math.max(z, k);

		return this;
	}

	@Override
	public String toString() {
		return String.format("(%s, %s, %s)", x, y, z);
	}
}
