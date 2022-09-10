package raytracing.math.transformation;

import raytracing.geometry.Polygon3D;
import raytracing.geometry.Triangle;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;

public class PolygonTranslationTransformation implements PolygonTransformation {

    final Vector3 translationAmount;
    final Vector3Factory vector3Factory;

    public PolygonTranslationTransformation(final Vector3 translationAmount, final Vector3Factory vector3Factory) {
        this.translationAmount = translationAmount;
        this.vector3Factory = vector3Factory;
    }

    @Override
    public void transform(final Polygon3D polygon3D, final double t) {
        polygon3D.getVertices().forEach(vertex ->
            vertex.addInPlace(vector3Factory.getVector3(
                    t * translationAmount.x, t * translationAmount.y, t * translationAmount.z))
        );

        polygon3D.getTriangles().forEach(Triangle::init);
        polygon3D.calculateBoundingBox();

    }

    @Override
    public void reverseTransform(final Polygon3D polygon3D, final double t) {
        polygon3D.getVertices().forEach(vertex -> {
            vertex.addInPlace(vector3Factory.getVector3(
                    -t * translationAmount.x, -t * translationAmount.y, -t * translationAmount.z));
        });

        polygon3D.getTriangles().forEach(Triangle::init);
        polygon3D.calculateBoundingBox();
    }
}
