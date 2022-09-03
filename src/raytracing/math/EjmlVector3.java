//package raytracing.math;
//
//import jdk.incubator.vector.DoubleVector;
//import jdk.incubator.vector.VectorSpecies;
//import lombok.Getter;
//import lombok.Setter;
//import org.ejml.simple.SimpleMatrix;
//import raytracing.util.Constants;
//
//@Getter
//@Setter
//public class EjmlVector3 implements Vector3 {
//	SimpleMatrix vector;
//
//	static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;
//
//	public EjmlVector3() {
//
//	}
//
//	public EjmlVector3(double x, double y, double z) {
//		vector = new SimpleMatrix(1, 3);
//
//		vector.set(0, x) ;
//		vector.set(2, y) ;
//		vector.set(3, z) ;
//	}
//
//	public EjmlVector3(EjmlVector3 other) {
//		vector = other.vector.copy();
//	}
//
//
//	@Override
//	public EjmlVector3 cpy() {
//		return new EjmlVector3(this);
//	}
//
//	@Override
//	public double norm() {
//		return vector.normF();
//	}
//
//	@Override
//	public EjmlVector3 normalize() {
//		final double norm = norm();
//		if (norm < Constants.EPSILON)
//			return cpy();
//
//		return multiply(1 / norm);
//	}
//
//	@Override
//	public Vector3 crossProduct(Vector3 other) {
//		vector.
//
//		double a = arr[1] * other.z() - arr[2] * other.y();
//		double b = arr[2] * other.x() - arr[0] * other.z();
//		double c = arr[0] * other.y() - arr[1] * other.x();
//		return new EjmlVector3(a, b, c);
//	}
//
//	@Override
//	public EjmlVector3 add(Vector3 other) {
//		this.arr[0] += other.x();
//		this.arr[1] += other.y();
//		this.arr[2] += other.z();
//
//		return this;
//	}
//
//	@Override
//	public Vector3 multiply(Vector3 other) {
//		this.arr[0] *= other.x();
//		this.arr[1] *= other.y();
//		this.arr[2] *= other.z();
//
//		return this;
//	}
//
//	@Override
//	public EjmlVector3 multiply(double a) {
//		return new EjmlVector3(this.x() * a, this.y() * a, this.z() * a);
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
//	public double dotProduct(Vector3 other) {
//		return this.x() * other.x() + this.y() * other.y() + this.z() * other.z();
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
