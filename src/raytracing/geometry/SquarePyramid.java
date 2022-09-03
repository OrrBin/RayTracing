package raytracing.geometry;

import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;

import java.util.LinkedList;

public class SquarePyramid extends Pyramid {

    public SquarePyramid(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 v4, Vector3 v5, int material, final Vector3Factory vector3Factory) {
        triangles = new LinkedList<>();
        triangles.add(new Triangle(v1, v2, v3, material, vector3Factory));
        triangles.add(new Triangle(v1, v3, v4, material, vector3Factory));
        triangles.add(new Triangle(v1, v2, v5, material, vector3Factory));
        triangles.add(new Triangle(v2, v3, v5, material, vector3Factory));
        triangles.add(new Triangle(v3, v4, v5, material, vector3Factory));
        triangles.add(new Triangle(v1, v4, v5, material, vector3Factory));

    }
}
