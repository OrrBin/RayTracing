package raytracing.math;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;
import lombok.Getter;
import lombok.Setter;
import raytracing.util.Constants;

@Getter
@Setter
public class ArrVector3 implements Vector3 {
	public double arr[] = new double[3];

	static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

	public ArrVector3() {

	}

	public ArrVector3(double x, double y, double z) {
		arr[0] = x;
		arr[1] = y;
		arr[2] = z;
	}

	public ArrVector3(ArrVector3 other) {
		arr[0] = other.arr[0];
		arr[1] = other.arr[1];
		arr[2] = other.arr[2];
	}

	public ArrVector3(final double[] other) {
		arr[0] = other[0];
		arr[1] = other[1];
		arr[2] = other[2];
	}

	@Override
	public ArrVector3 cpy() {
		return new ArrVector3(this);
	}

	@Override
	public double norm() {
		return Math.sqrt(arr[0] * arr[0] + arr[1] * arr[1] + arr[2] * arr[2]);
	}

	@Override
	public ArrVector3 normalize() {
		if (norm() < Constants.EPSILON)
			return cpy();

		return multiply(1 / norm());
	}

	@Override
	public Vector3 crossProduct(Vector3 other) {
		double a = arr[1] * other.z() - arr[2] * other.y();
		double b = arr[2] * other.x() - arr[0] * other.z();
		double c = arr[0] * other.y() - arr[1] * other.x();
		return new ArrVector3(a, b, c);
	}

	@Override
	public ArrVector3 add(Vector3 other) {
		this.arr[0] += other.x();
		this.arr[1] += other.y();
		this.arr[2] += other.z();

		return this;
	}

	@Override
	public Vector3 multiply(Vector3 other) {
		this.arr[0] *= other.x();
		this.arr[1] *= other.y();
		this.arr[2] *= other.z();

		return this;
	}

	@Override
	public ArrVector3 multiply(double a) {
		return new ArrVector3(this.x() * a, this.y() * a, this.z() * a);
	}

	@Override
	public Vector3 connectingVector(Vector3 other) {
		return other.cpy().add(this.multiply(-1));
	}

	@Override
	public double distance(Vector3 other) {
		return connectingVector(other).norm();
	}

	@Override
	public double dotProduct(final Vector3 o) {
		final ArrVector3 other = (ArrVector3) o;
		double sum = 0f;
		for (int i = 0; i < 3; ++i) {
			sum = Math.fma(this.arr[i], other.arr[i], sum);
		}
		return sum;
	}

	/**
	 * Bounds the values of x,y,z from above
	 */
	@Override
	public Vector3 boundFromAbove(double[] bounds) {
		arr[0] = Math.min(x(), bounds[0]);
		arr[1] = Math.min(y(), bounds[1]);
		arr[2] = Math.min(z(), bounds[2]);

		return this;
	}

	@Override
	public Vector3 invert() {
		return new ArrVector3(1/arr[0], 1/arr[1], 1/arr[2]);
	}

	@Override
	public double x() {
		return arr[0];
	}

	@Override
	public double y() {
		return arr[1];
	}

	@Override
	public double z() {
		return arr[2];
	}


	@Override
	public String toString() {
		return String.format("(%s, %s, %s)", arr[0], arr[1], arr[2]);
	}
}
