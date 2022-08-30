package raytracing.math;

import raytracing.actors.Ray;

public class Transformation3D {

    private Matrix4 matrix = Matrix4.MATRIX4_IDENTITY;
    private Matrix4 invMatrix = Matrix4.MATRIX4_IDENTITY;

    public Transformation3D translate(Vector3 translation) {
        this.matrix = Matrix4.translate(translation.getX(), translation.getY(), translation.getZ()).times(this.matrix);
        this.invMatrix = this.invMatrix.times(Matrix4.translateInverse(translation.getX(), translation.getY(), translation.getZ()));

        return this;
    }

    public Transformation3D rotateX(double angleDeg) {
        this.matrix = Matrix4.rotateX(angleDeg).times( this.matrix );
        this.invMatrix = this.invMatrix.times( Matrix4.rotateXInverse(angleDeg) );

        return this;
    }

    public Transformation3D rotateY(double angleDeg) {
        this.matrix = Matrix4.rotateY(angleDeg).times( this.matrix );
        this.invMatrix = this.invMatrix.times( Matrix4.rotateYInverse(angleDeg) );

        return this;
    }

    public Transformation3D rotateZ(double angleDeg) {
        this.matrix = Matrix4.rotateZ(angleDeg).times( this.matrix );
        this.invMatrix = this.invMatrix.times( Matrix4.rotateZInverse(angleDeg) );

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
