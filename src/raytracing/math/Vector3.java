package raytracing.math;

public interface Vector3{
    Vector3 cpy();

    double norm();

    Vector3 normalize();

    Vector3 crossProduct(Vector3 other);

    Vector3 add(Vector3 other);

    Vector3 multiply(Vector3 other);

    Vector3 multiply(double a);

    Vector3 connectingVector(Vector3 other);

    double distance(Vector3 other);

    double dotProduct(Vector3 other);

    Vector3 boundFromAbove(double[] bounds);

    Vector3 invert();

    double x();

    double y();

    double z();
}
