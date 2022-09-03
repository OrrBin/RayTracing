package raytracing.geometry;

import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;

import java.util.LinkedList;

public class SquarePyramid extends TriangulatedShape {

    public SquarePyramid(final Vector3 v1, final Vector3 v2, final Vector3 v3, final Vector3 v4, final Vector3 v5,
                         final int material, final Vector3Factory vector3Factory) {
        triangles = new LinkedList<>();
        triangles.add(new Triangle(v1, v2, v3, material, this, vector3Factory));
        triangles.add(new Triangle(v1, v3, v4, material, this, vector3Factory));
        triangles.add(new Triangle(v1, v2, v5, material, this, vector3Factory));
        triangles.add(new Triangle(v2, v3, v5, material, this, vector3Factory));
        triangles.add(new Triangle(v3, v4, v5, material, this, vector3Factory));
        triangles.add(new Triangle(v1, v4, v5, material, this, vector3Factory));

        this.calculateBoundingBox();
    }
}
