package raytracing.math;

import lombok.Getter;
import lombok.Setter;
import raytracing.util.Constants;

@Getter
@Setter
public class SimpleVector3 extends Vector3 {

	public final double invX, invY, invZ;
	public final double norm;

	public SimpleVector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;

		invX = 1/this.x;
		invY= 1/this.y;
		invZ = 1/this.z;

		norm = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	public SimpleVector3(SimpleVector3 other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;

		invX = 1/this.x;
		invY= 1/this.y;
		invZ = 1/this.z;

		norm = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	@Override
	public SimpleVector3 cpy() {
		return new SimpleVector3(this);
	}

	@Override
	public double norm() {
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	@Override
	public SimpleVector3 normalize() {
		if (norm() < Constants.EPSILON)
			return cpy();

		return multiply(1 / norm());
	}

	@Override
	public Vector3 crossProduct(Vector3 other) {
		double a = this.y * other.z - this.z * other.y;
		double b = this.z * other.x - this.x * other.z;
		double c = this.x * other.y - this.y * other.x;
		return new SimpleVector3(a, b, c);
	}

	@Override
	public SimpleVector3 addInPlace(Vector3 other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;

		return this;
	}

	@Override
	public Vector3 multiply(Vector3 other) {
		this.x *= other.x;
		this.y *= other.y;
		this.z *= other.z;

		return this;
	}

	@Override
	public SimpleVector3 multiply(double a) {
		return new SimpleVector3(this.x * a, this.y * a, this.z * a);
	}

	@Override
	public Vector3 connectingVector(Vector3 other) {
		return other.cpy().addInPlace(this.multiply(-1));
	}

	@Override
	public double distance(Vector3 other) {
		return connectingVector(other).norm();
	}

	@Override
	public double dotProduct(final Vector3 o) {
		final SimpleVector3 other = (SimpleVector3) o;
		double sum = Math.fma(x, other.x, 0);
		sum = Math.fma(y, other.y, sum);
		sum = Math.fma(z, other.z, sum);

		return sum;
	}

	/**
	 * Bounds the values of x,y,z from above
	 */
	@Override
	public Vector3 boundFromAboveInPlace(double[] bounds) {
		this.x = Math.min(x, bounds[0]);
		this.y = Math.min(y, bounds[1]);
		this.z = Math.min(z, bounds[2]);

		return this;
	}

	@Override
	public Vector3 invert() {
		return new SimpleVector3(invX, invY, invZ);
	}

	@Override
	public String toString() {
		return String.format("(%s, %s, %s)", this.x, this.y, this.z);
	}
}
