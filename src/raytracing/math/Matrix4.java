package raytracing.math;


import java.util.Arrays;

public class Matrix4 {

    double[] m;
    private final Vector3Factory vector3Factory;

    public Matrix4(final double[] elements, final Vector3Factory vector3Factory) {
        if (elements.length != 16)
            throw new RuntimeException("Expecting array of length 16 but found: " + Arrays.toString(elements) + ".");

        this.m = elements.clone();
        this.vector3Factory = vector3Factory;
    }

    double get(int row, int col) {
        return this.m[row * 4 + col];
    }

    public Matrix4 times(Matrix4 otherMatrix) {
//        double[] result = mult(this.m, otherMatrix.m, 4);
//        return new Matrix4(result);
        double[] copy = new double[16];

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                double sum = 0.0;

                for (int j = 0; j < 4; j++) {
                    sum += this.get(row, j) * otherMatrix.get(j, col);
                }

                copy[row * 4 + col] = sum;
            }
        }

        return new Matrix4(copy, vector3Factory);
    }

    public Matrix4 add(Matrix4 otherMatrix) {
        double[] copy = new double[16];

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                copy[row * 4 + col] = this.get(row, col) + otherMatrix.get(row, col);
            }
        }

        return new Matrix4(copy, vector3Factory);
    }

    public Matrix4 transpose() {
        double[] copy = new double[16];

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                copy[col * 4 + row] = this.get(row, col);
            }
        }

        return new Matrix4(copy, vector3Factory);
    }

    public Vector3 transformPoint(Vector3 point) {
        return vector3Factory.getVector3(
                m[0] * point.x + m[1] * point.y + m[2] * point.z + m[3],
                m[4] * point.x + m[5] * point.y + m[6] * point.z + m[7],
                m[8] * point.x + m[9] * point.y + m[10] * point.z + m[11]);
    }

    public Vector3 transformVector(Vector3 vec) {
        return vector3Factory.getVector3(
                m[0] * vec.x + m[1] * vec.y + m[2] * vec.z,
                m[4] * vec.x + m[5] * vec.y + m[6] * vec.z,
                m[8] * vec.x + m[9] * vec.y + m[10] * vec.z);
    }


    /**
     * We use equality {@code (transposed matrix)*normal = transformed_normal}.
     */
    public Vector3 transformNormal(Vector3 n) {
        return vector3Factory.getVector3(
                m[0] * n.x + m[4] * n.y + m[8] * n.z,
                m[1] * n.x + m[5] * n.y + m[9] * n.z,
                m[2] * n.x + m[6] * n.y + m[10] * n.z);
    }

    public static Matrix4 translate(final double dx, final double dy, final double dz, final Vector3Factory vector3Factory) {
        return new Matrix4(new double[]{
                1.0, 0.0, 0.0, dx,
                0.0, 1.0, 0.0, dy,
                0.0, 0.0, 1.0, dz,
                0.0, 0.0, 0.0, 1.0
        }, vector3Factory);
    }

    public static Matrix4 translateInverse(final double dx, final double dy, final double dz, final Vector3Factory vector3Factory) {
        return new Matrix4(new double[]{
                1.0, 0.0, 0.0, -dx,
                0.0, 1.0, 0.0, -dy,
                0.0, 0.0, 1.0, -dz,
                0.0, 0.0, 0.0, 1.0
        }, vector3Factory);
    }

    public static Matrix4 rotateX(final double angleDeg, final Vector3Factory vector3Factory) {
        double sin = Math.sin(deg2rad(angleDeg));
        double cos = Math.cos(deg2rad(angleDeg));

        return new Matrix4(new double[]{
                1.0, 0.0, 0.0, 0.0,
                0.0, cos, -sin, 0.0,
                0.0, sin, cos, 0.0,
                0.0, 0.0, 0.0, 1.0
        }, vector3Factory);
    }

    public static Matrix4 rotateXInverse(final double angleDeg, final Vector3Factory vector3Factory) {
        double sin = Math.sin(deg2rad(angleDeg));
        double cos = Math.cos(deg2rad(angleDeg));

        return new Matrix4(new double[]{
                1.0, 0.0, 0.0, 0.0,
                0.0, cos, sin, 0.0,
                0.0, -sin, cos, 0.0,
                0.0, 0.0, 0.0, 1.0
        }, vector3Factory);
    }

    public static Matrix4 rotateY(double angleDeg, final Vector3Factory vector3Factory) {
        double sin = Math.sin(deg2rad(angleDeg));
        double cos = Math.cos(deg2rad(angleDeg));

        return new Matrix4(new double[]{
                cos, 0.0, sin, 0.0,
                0.0, 1.0, 0.0, 0.0,
                -sin, 0.0, cos, 0.0,
                0.0, 0.0, 0.0, 1.0
        }, vector3Factory);
    }

    public static Matrix4 rotateYInverse(double angleDeg, final Vector3Factory vector3Factory) {
        double sin = Math.sin(deg2rad(angleDeg));
        double cos = Math.cos(deg2rad(angleDeg));

        return new Matrix4(new double[]{
                cos, 0.0, -sin, 0.0,
                0.0, 1.0, 0.0, 0.0,
                sin, 0.0, cos, 0.0,
                0.0, 0.0, 0.0, 1.0
        }, vector3Factory);
    }

    public static Matrix4 rotateZ(double angleDeg, final Vector3Factory vector3Factory) {
        double sin = Math.sin(deg2rad(angleDeg));
        double cos = Math.cos(deg2rad(angleDeg));

        return new Matrix4(new double[]{
                cos, -sin, 0.0, 0.0,
                sin, cos, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 0.0, 1.0
        }, vector3Factory);
    }

    public static Matrix4 rotateZInverse(double angleDeg, final Vector3Factory vector3Factory) {
        double sin = Math.sin(deg2rad(angleDeg));
        double cos = Math.cos(deg2rad(angleDeg));

        return new Matrix4(new double[]{
                cos, sin, 0.0, 0.0,
                -sin, cos, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 0.0, 1.0
        }, vector3Factory);
    }



    public static final double[] MATRIX4_IDENTITY = new double[]{
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
    };

    public static final double[] MATRIX4_ZERO = new double[]{
            0.0, 0.0, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0
    };

    public static double deg2rad(double angleDeg) {
        return (angleDeg / 180.0) * Math.PI;
    }
}
