package raytracing.math.transformation;

import raytracing.geometry.Polygon3D;

public interface PolygonTransformation {

    void transform(Polygon3D polygon3D, double t);

    void reverseTransform(Polygon3D polygon3D, double t);
}
