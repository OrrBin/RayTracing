//package raytracing.math;
//
//import jdk.incubator.vector.DoubleVector;
//import jdk.incubator.vector.VectorOperators;
//import jdk.incubator.vector.VectorSpecies;
//import lombok.Getter;
//import lombok.Setter;
//import raytracing.util.Constants;
//
//@Getter
//@Setter
//public class VectorAPIVector3 implements Vector3 {
//
//	public double[] arr;
//	private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;
//	private static final int speciesLength = SPECIES.length();
//
//	public VectorAPIVector3(double x, double y, double z) {
//		arr = new double[speciesLength];
//		arr[0] = x;
//		arr[1] = y;
//		arr[2] = z;
//	}
//
//	public VectorAPIVector3(VectorAPIVector3 other) {
//		arr = new double[speciesLength];
//		arr[0] = other.arr[0];
//		arr[1] = other.arr[1];
//		arr[2] = other.arr[2];
//	}
//
//	public VectorAPIVector3(final double[] other) {
//		arr = new double[speciesLength];
//		arr[0] = other[0];
//		arr[1] = other[1];
//		arr[2] = other[2];
//	}
//
//	@Override
//	public VectorAPIVector3 cpy() {
//		return new VectorAPIVector3(this);
//	}
//
//	@Override
//	public double norm() {
////		return Math.sqrt(x * x + y * y + z * z);
//		return Math.sqrt(arr[0] * arr[0] + arr[1] * arr[1] + arr[2] * arr[2]);
//	}
//
//	@Override
//	public VectorAPIVector3 normalize() {
//		if (norm() < Constants.EPSILON)
//			return cpy();
//
//		return multiply(1 / norm());
//	}
//
//	@Override
//	public Vector3 crossProduct(Vector3 other) {
//		double a = arr[1] * other.z() - arr[2] * other.y();
//		double b = arr[2] * other.x() - arr[0] * other.z();
//		double c = arr[0] * other.y() - arr[1] * other.x();
//		return new VectorAPIVector3(a, b, c);
//	}
//
//	@Override
//	public VectorAPIVector3 add(Vector3 o) {
//		final VectorAPIVector3 other = (VectorAPIVector3) o;
//
//		double[] result = new double[speciesLength];
//		int i = 0;
//		int upperBound = SPECIES.loopBound(this.arr.length);
//		for (; i < upperBound; i += SPECIES.length()) {
//			// FloatVector va, vb, vc;
//			var va = DoubleVector.fromArray(SPECIES, this.arr, i);
//			var vb = DoubleVector.fromArray(SPECIES, other.arr, i);
//			var vc = va.add(vb);
//			vc.intoArray(result, i);
//		}
////		for (; i < this.arr.length; i++) {
////			result[i] = (this.arr[i] + other.arr[i]);
////		}
//
//		this.arr[0] = result[0];
//		this.arr[1] = result[1];
//		this.arr[2] = result[2];
//
//		return this;
//	}
//
//	@Override
//	public Vector3 multiply(Vector3 o) {
//		final VectorAPIVector3 other = (VectorAPIVector3) o;
//		double[] result = new double[speciesLength];
//		int i = 0;
//		int upperBound = SPECIES.loopBound(this.arr.length);
////		System.out.println("upperBound: " + upperBound);
//		for (; i < upperBound; i += SPECIES.length()) {
////			System.out.println("Good iteration: " + i);
//			// FloatVector va, vb, vc;
//			var va = DoubleVector.fromArray(SPECIES, this.arr, i);
//			var vb = DoubleVector.fromArray(SPECIES, other.arr, i);
//			var vc = va.mul(vb);
//			vc.intoArray(result, i);
//		}
////		for (; i < this.arr.length; i++) {
//////			System.out.println("Bad iteration: " + i);
////			result[i] = (this.arr[i] * other.arr[i]);
////		}
//
//		return new VectorAPIVector3(result);
//	}
//
//	@Override
//	public VectorAPIVector3 multiply(double a) {
//		double[] result = new double[speciesLength];
//		int i = 0;
//		int upperBound = SPECIES.loopBound(this.arr.length);
//		for (; i < upperBound; i += SPECIES.length()) {
//			// FloatVector va, vb, vc;
//			var va = DoubleVector.fromArray(SPECIES, this.arr, i);
//			var vc = va.mul(a);
//			vc.intoArray(result, i);
//		}
//		for (; i < this.arr.length; i++) {
//			result[i] = (this.arr[i] * a);
//		}
//
//		return new VectorAPIVector3(result);
//	}
//
//	@Override
//	public Vector3 connectingVector(Vector3 other) {
//		return other.cpy().add(this.multiply(-1));
//	}
//
//	@Override
//	public double distance(Vector3 other) {
//		return connectingVector(other).norm();
//	}
//
//	@Override
//	public double dotProduct(Vector3 o) {
//		VectorAPIVector3 other = (VectorAPIVector3) o;
//		var upperBound = SPECIES.loopBound(this.arr.length);
//		var sum = DoubleVector.zero(SPECIES);
//		var i = 0;
//		for (; i < upperBound; i += SPECIES.length()) {
//			// FloatVector va, vb, vc
//			var va = DoubleVector.fromArray(SPECIES, this.arr, i);
//			var vb = DoubleVector.fromArray(SPECIES, other.arr, i);
//			sum = va.fma(vb, sum);
//		}
//		var c = sum.reduceLanes(VectorOperators.ADD);
//		for (; i < this.arr.length; i++) { // Cleanup loop
//			c += this.arr[i] * other.arr[i];
//		}
//		return c;
//	}
//
//	/**
//	 * Bounds the values of x,y,z from above
//	 */
//	@Override
//	public Vector3 boundFromAbove(double[] bounds) {
//		arr[0] = Math.min(x(), bounds[0]);
//		arr[1] = Math.min(y(), bounds[1]);
//		arr[2] = Math.min(z(), bounds[2]);
//
//		return this;
//	}
//
//	@Override
//	public Vector3 invert() {
//		return new ArrVector3(1/arr[0], 1/arr[1], 1/arr[2]);
//	}
//
//	@Override
//	public double x() {
//		return arr[0];
//	}
//
//	@Override
//	public double y() {
//		return arr[1];
//	}
//
//	@Override
//	public double z() {
//		return arr[2];
//	}
//
//
//	@Override
//	public String toString() {
//		return String.format("(%s, %s, %s)", arr[0], arr[1], arr[2]);
//	}
//}
