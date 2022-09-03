package raytracing.math;

import lombok.Getter;
import lombok.Setter;
import org.ejml.simple.SimpleMatrix;
import raytracing.util.Constants;

@Getter
@Setter
public class EjmlVector3 extends Vector3 {
	public SimpleMatrix rowVector;
	public SimpleMatrix colVector;
    public SimpleMatrix vectorInv;
    public double norm;

	public EjmlVector3(final double x, final double y, final double z) {
		rowVector = new SimpleMatrix(1, 3);

		rowVector.set(0, 0, x) ;
		rowVector.set(0, 1, y) ;
		rowVector.set(0, 2, z) ;

        updateFields();
	}

	public EjmlVector3(final EjmlVector3 other) {
		rowVector = other.rowVector.copy();
        updateFields();
	}

    public EjmlVector3(final SimpleMatrix other) {
        this.rowVector = other;
        updateFields();
    }


    @Override
	public EjmlVector3 cpy() {
		return new EjmlVector3(this);
	}

	@Override
	public double norm() {
		return norm;
	}

	@Override
	public EjmlVector3 normalize() {
		if (norm < Constants.EPSILON)
			return cpy();

		return new EjmlVector3(rowVector.divide(norm));
	}

	@Override
	public Vector3 crossProduct(Vector3 other) {
        double a = rowVector.get(0, 1) * other.z - rowVector.get(0, 2) * other.y;
        double b = rowVector.get(0, 2) * other.x - rowVector.get(0, 0) * other.z;
        double c = rowVector.get(0, 0) * other.y - rowVector.get(0, 1) * other.x;
        return new EjmlVector3(a, b, c);

	}

	@Override
	public EjmlVector3 addInPlace(Vector3 other) {
        this.rowVector = this.rowVector.plus(((EjmlVector3) other).rowVector);
        updateFields();
		return this;
	}

	@Override
	public Vector3 multiply(Vector3 other) {

        return new EjmlVector3(this.rowVector.elementMult( ((EjmlVector3) other).rowVector));
	}

	@Override
	public EjmlVector3 multiply(double a) {
		return new EjmlVector3(this.rowVector.scale(a));
	}

	@Override
	public Vector3 connectingVector(Vector3 o) {
		final EjmlVector3 other = (EjmlVector3) o;

        return new EjmlVector3(other.rowVector.plus(this.rowVector.negative()));
	}

	@Override
	public double distance(Vector3 other) {
		return connectingVector(other).norm();
	}

	@Override
	public double dotProduct(Vector3 other) {
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
		rowVector.set(0, 0, Math.min(x, bounds[0]));
		rowVector.set(0, 1, Math.min(y, bounds[1]));
		rowVector.set(0, 2, Math.min(z, bounds[2]));
        updateFields();

		return this;
	}

    @Override
    public Vector3 invert() {
        return new EjmlVector3(vectorInv);
    }

    @Override
	public String toString() {
		return String.format("(%s, %s, %s)", x, y, z);
	}

    private void updateFields() {
		this.colVector = rowVector.transpose();
        this.vectorInv = rowVector.elementPower(-1);
        this.norm = rowVector.normF();
        this.x = rowVector.get(0, 0);
        this.y = rowVector.get(0, 1);
        this.z = rowVector.get(0, 2);
    }
}
