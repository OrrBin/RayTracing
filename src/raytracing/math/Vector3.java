package raytracing.math;

public abstract class Vector3{

    public double x, y, z;

    public abstract Vector3 cpy();

    public abstract double norm();

    public abstract Vector3 normalize();

    public abstract Vector3 crossProduct(Vector3 other);

    public abstract Vector3 add(Vector3 other);

    public abstract Vector3 multiply(Vector3 other);

    public abstract Vector3 multiply(double a);

    public abstract Vector3 connectingVector(Vector3 other);

    public abstract double distance(Vector3 other);

    public abstract double dotProduct(Vector3 other);

    public abstract Vector3 boundFromAbove(double[] bounds);

    public abstract Vector3 invert();
}
