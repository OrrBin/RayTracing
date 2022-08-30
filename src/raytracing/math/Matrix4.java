package raytracing.math;

import java.util.Arrays;

public class Matrix4 {

    double[] m;

    public Matrix4(double[] elements) {
        if (elements.length != 16)
            throw new RuntimeException("Expecting array of length 16 but found: " + Arrays.toString(elements) + ".");

        this.m = elements.clone();
    }

    double get(int row, int col)  {
        return this.m[row * 4 + col];
    }

    public Matrix4 times(Matrix4 otherMatrix) {
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

        return new Matrix4(copy);
    }

    public Matrix4 add(Matrix4 otherMatrix) {
        double[] copy = new double[16];

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                copy[row * 4 + col] = this.get(row, col) + otherMatrix.get(row,col);
            }
        }

        return new Matrix4(copy);
    }

    public Matrix4 transpose() {
        double[] copy = new double[16];

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                copy[col * 4 + row] = this.get(row, col);
            }
        }

        return new Matrix4(copy);
    }

    public Vector3 transformPoint(Vector3 point) {
        return new Vector3(
                m[0] * point.x + m[1] * point.y + m[2] * point.z + m[3],
                m[4] * point.x + m[5] * point.y + m[6] * point.z + m[7],
                m[8] * point.x + m[9] * point.y + m[10] * point.z + m[11]);
    }

    /**
     * We use equality {@code (transposed matrix)*normal = transformed_normal}.
     */
    public Vector3 transformNormal(Vector3 n)  {
        return new Vector3(
                this.get(0, 0) * n.getX() + this.get(1, 0) * n.getY() + this.get(2, 0) * n.getZ(),
                this.get(0, 1) * n.getX() + this.get(1, 1) * n.getY() + this.get(2, 1) * n.getZ(),
                this.get(0, 2) * n.getX() + this.get(1, 2) * n.getY() + this.get(2, 2) * n.getZ());
    }

    public static Matrix4 translate(double dx, double dy, double dz) {
        return new Matrix4(new double[]{
                1.0, 0.0, 0.0, dx,
                0.0, 1.0, 0.0, dy,
                0.0, 0.0, 1.0, dz,
                0.0, 0.0, 0.0, 1.0
        });
    }

    public static Matrix4 translateInverse(double dx, double dy, double dz) {
        return new Matrix4(new double[]{
                1.0, 0.0, 0.0, -dx,
                0.0, 1.0, 0.0, -dy,
                0.0, 0.0, 1.0, -dz,
                0.0, 0.0, 0.0, 1.0
        });
    }

    public static Matrix4 rotateX(double angleDeg) {
        double sin = Math.sin(deg2rad(angleDeg));
        double cos = Math.cos(deg2rad(angleDeg));

        return new Matrix4(new double[] {
                1.0,0.0, 0.0, 0.0,
                0.0, cos, -sin, 0.0,
                0.0, sin, cos, 0.0,
                0.0, 0.0, 0.0, 1.0
                });
    }

    public static Matrix4  rotateXInverse(double angleDeg) {
        double sin = Math.sin(deg2rad(angleDeg));
        double cos = Math.cos(deg2rad(angleDeg));

        return new Matrix4(new double[]{
                1.0, 0.0, 0.0, 0.0,
                0.0, cos, sin, 0.0,
                0.0, -sin, cos, 0.0,
                0.0, 0.0, 0.0, 1.0
        });
    }

    public static Matrix4  rotateY(double angleDeg) {
        double sin = Math.sin(deg2rad(angleDeg));
        double cos = Math.cos(deg2rad(angleDeg));

        return new Matrix4(new double[]{
                cos, 0.0, sin, 0.0,
                0.0, 1.0, 0.0, 0.0,
                -sin, 0.0, cos, 0.0,
                0.0, 0.0, 0.0, 1.0
        });
    }

    public static Matrix4 rotateYInverse(double angleDeg) {
        double sin = Math.sin(deg2rad(angleDeg));
        double cos = Math.cos(deg2rad(angleDeg));

        return new Matrix4(new double[]{
                cos, 0.0, -sin, 0.0,
                0.0, 1.0, 0.0, 0.0,
                sin, 0.0, cos, 0.0,
                0.0, 0.0, 0.0, 1.0
        });
    }

    public static Matrix4 rotateZ(double angleDeg) {
        double sin = Math.sin(deg2rad(angleDeg));
        double cos = Math.cos(deg2rad(angleDeg));

        return new Matrix4(new double[]{
                cos, -sin, 0.0, 0.0,
                sin, cos, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 0.0, 1.0
        });
    }

    public static Matrix4 rotateZInverse(double angleDeg) {
        double sin = Math.sin(deg2rad(angleDeg));
        double cos = Math.cos(deg2rad(angleDeg));

        return new Matrix4(new double[]{
                cos, sin, 0.0, 0.0,
                -sin, cos, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 0.0, 1.0
        });
    }


    public static final Matrix4 MATRIX4_IDENTITY = new Matrix4(new double[]{
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
    });

    public static final Matrix4 MATRIX4_ZERO = new Matrix4(new double[]{
            0.0, 0.0, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0
    });

    public static double deg2rad(double angleDeg) {
        return (angleDeg / 180.0) * Math.PI;
    }
}