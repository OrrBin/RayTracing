package raytracing.geometry;

import raytracing.math.Vector3;

import java.util.LinkedList;

public class TriangularPyramid extends Pyramid {

    public TriangularPyramid(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 v4, int material) {
        triangles = new LinkedList<>();
        triangles.add(new Triangle(v1, v2, v3, material));
        triangles.add(new Triangle(v1, v2, v4, material));
        triangles.add(new Triangle(v1, v3, v4, material));
        triangles.add(new Triangle(v2, v3, v4, material));

    }
}
