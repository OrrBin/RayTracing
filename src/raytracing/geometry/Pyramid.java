package raytracing.geometry;


import java.util.List;

public abstract class Pyramid {

    protected List<Triangle> triangles;

    public List<Triangle> getTriangles() {
        return triangles;
    }
}
