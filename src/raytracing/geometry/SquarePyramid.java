package raytracing.geometry;

import raytracing.math.Vector3;

import java.util.LinkedList;

public class SquarePyramid extends Pyramid {

    public SquarePyramid(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 v4, Vector3 v5, int material) {
        triangles = new LinkedList<>();
        triangles.add(new Triangle(v1, v2, v3, material));
        triangles.add(new Triangle(v1, v3, v4, material));
        triangles.add(new Triangle(v1, v2, v5, material));
        triangles.add(new Triangle(v2, v3, v5, material));
        triangles.add(new Triangle(v3, v4, v5, material));
        triangles.add(new Triangle(v1, v4, v5, material));

    }
}
