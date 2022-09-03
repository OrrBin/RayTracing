package raytracing.math;

import raytracing.actors.Ray;

public class Transformation3D {

    private Matrix4 matrix;
    private Matrix4 invMatrix;
    private final Vector3Factory vector3Factory;

    public Transformation3D(final Vector3Factory vector3Factory) {
        this.vector3Factory = vector3Factory;
        this.matrix = new Matrix4(Matrix4.MATRIX4_IDENTITY, vector3Factory);
        this.invMatrix = new Matrix4(Matrix4.MATRIX4_IDENTITY, vector3Factory);
    }


    public Transformation3D translate(Vector3 translation) {
        this.matrix = Matrix4.translate(translation.x(), translation.y(), translation.z(), vector3Factory).times(this.matrix);
        this.invMatrix = this.invMatrix.times(Matrix4.translateInverse(translation.x(), translation.y(), translation.z(), vector3Factory));

        return this;
    }

    public Transformation3D rotateX(double angleDeg) {
        this.matrix = Matrix4.rotateX(angleDeg, vector3Factory).times( this.matrix );
        this.invMatrix = this.invMatrix.times( Matrix4.rotateXInverse(angleDeg, vector3Factory) );

        return this;
    }

    public Transformation3D rotateY(double angleDeg) {
        this.matrix = Matrix4.rotateY(angleDeg, vector3Factory).times( this.matrix );
        this.invMatrix = this.invMatrix.times( Matrix4.rotateYInverse(angleDeg, vector3Factory) );

        return this;
    }

    public Transformation3D rotateZ(double angleDeg) {
        this.matrix = Matrix4.rotateZ(angleDeg, vector3Factory).times( this.matrix );
        this.invMatrix = this.invMatrix.times( Matrix4.rotateZInverse(angleDeg, vector3Factory) );

        return this;
    }

    public void affine(Matrix4 otherMatrix, Matrix4 otherMatrixInverse) {
        this.matrix = otherMatrix.times( this.matrix );
        this.invMatrix = this.invMatrix.times( otherMatrixInverse );
    }

    public Ray transformRay(Ray ray) {
        Vector3 tfOrigin = this.invMatrix.transformPoint(ray.originPoint);
        Vector3 tfDirection = this.invMatrix.transformVector(ray.direction);

        return new Ray(tfOrigin, tfDirection);
    }

    public Vector3 transformNormal(Vector3 n) {
        return this.invMatrix.transformNormal(n);
    }
}
