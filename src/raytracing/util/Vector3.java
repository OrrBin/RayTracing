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

	public Vector3(Vector3 other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	
	public Vector3 cpy() {
		return new Vector3(this);
	}
	
	public double norm() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public Vector3 normalize() {
		if(norm() < Constants.EPSILON)
			return this;
		
		return cpy().multiply(1/norm());
	}
	
	public Vector3 crossProduct(Vector3 other) {
		double a = y*other.z - z*other.y;
		double b = z*other.x - x*other.z;
		double c = x*other.y - y*other.x;
		return new Vector3(a,b,c);
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
		this.x *= a;
		this.y *= a;
		this.z *= a;

		return this;
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

	@Override
	public String toString() {
		return String.format("(%s, %s, %s)", x, y, z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3 other = (Vector3) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}
	
}
