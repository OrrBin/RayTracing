package raytracing.geometry;

import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;

import java.util.List;
import java.util.stream.Collectors;

public class Polygon3D extends TriangulatedShape {

    private final Vector3Factory vector3Factory;

    public Polygon3D(List<Vector3> vertices, int material, Vector3Factory vector3Factory) {
        this.vector3Factory = vector3Factory;
        List<PolygonPoint> polygonPoints = getPolygonPointList(vertices);
        // Prepare input data
        Polygon polygon = new Polygon(polygonPoints);
        // Launch tessellation
        Poly2Tri.triangulate(polygon);
        // Gather triangles
        List<DelaunayTriangle> delaunayTriangles = polygon.getTriangles();
        System.out.println("Number of triangles: " + delaunayTriangles.size());

        this.triangles = delaunayTriangles.stream()
                .map(delaunayTriangle -> new Triangle(
                        getVector3FromTriangulationPoint(delaunayTriangle.points[0]),
                        getVector3FromTriangulationPoint(delaunayTriangle.points[1]),
                        getVector3FromTriangulationPoint(delaunayTriangle.points[2]),
                        material,
                        this,
                        vector3Factory))
                .collect(Collectors.toList());

        this.calculateBoundingBox();


    }

    private Vector3 getVector3FromTriangulationPoint(final TriangulationPoint point) {
        return vector3Factory.getVector3(point.getX(), point.getY(), point.getZ());
    }

    private List<PolygonPoint> getPolygonPointList(final List<Vector3> vertices) {
        return vertices.stream()
                .map(vertex -> new PolygonPoint(vertex.x, vertex.y, vertex.z))
                .collect(Collectors.toList());
    }
}