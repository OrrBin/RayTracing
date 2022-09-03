package raytracing.geometry;

import raytracing.actors.Ray;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;
import raytracing.math.util.IntersectionUtils;
import raytracing.util.Constants;

public class Triangle extends Shape {

    private final Vector3 p1;
    private final Vector3 p2;
    private final Vector3 p3;

    private final Vector3 normal;
    private final double offset;
    private final Vector3 p1ConnectingVectorTop2;
    private final Vector3 p1ConnectingVectorTop3;

//    private final Vector3 min;
//    private final Vector3 max;
    private final double[][] bounds;

    public Triangle(Vector3 vertex1, Vector3 vertex2, Vector3 vertex3, int material, final Vector3Factory vector3Factory) {
        super(material);
        this.p1 = vertex1;
        this.p2 = vertex2;
        this.p3 = vertex3;

        Vector3 v = p1.cpy().add(p2.multiply(-1));
        Vector3 u = p1.cpy().add(p3.multiply(-1));

        normal = v.crossProduct(u).normalize();
        offset = normal.cpy().dotProduct(p1);

        p1ConnectingVectorTop2 = p1.connectingVector(p2);
        p1ConnectingVectorTop3 = p1.connectingVector(p3);

        this.bounds = new double[][]{
                {
                        Math.min(p1.x(), Math.min(p2.x(), p3.x())),
                        Math.min(p1.y(), Math.min(p2.y(), p3.y())),
                        Math.min(p1.z(), Math.min(p2.z(), p3.z()))
                },
                {
                        Math.max(p1.x(), Math.max(p2.x(), p3.x())),
                        Math.max(p1.y(), Math.max(p2.y(), p3.y())),
                        Math.max(p1.z(), Math.max(p2.z(), p3.z()))
                }
        };

    }

    @Override
    public Vector3 intersection(Ray ray) {
        double angle = ray.direction.dotProduct(normal);

        if (!IntersectionUtils.intersectionWithBoundingBox(ray, bounds)) {
            return null;
        }

        if (Math.abs(angle) < Constants.EPSILON)
            return null;

        double t = (offset - ray.originPoint.dotProduct(normal)) / angle;
        if (t < 0)
            return null;

        Vector3 pointOfContact = ray.getOriginPointCpy().add(ray.direction.multiply(t));
        Vector3 w = p1.connectingVector(pointOfContact);

        double a = (p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop3) * w.dotProduct(p1ConnectingVectorTop3) - p1ConnectingVectorTop3.dotProduct(p1ConnectingVectorTop3) * w.dotProduct(p1ConnectingVectorTop2))
                / (Math.pow(p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop3), 2) - p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop2) * p1ConnectingVectorTop3.dotProduct(p1ConnectingVectorTop3));
        double b = (p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop3) * w.dotProduct(p1ConnectingVectorTop2) - p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop2) * w.dotProduct(p1ConnectingVectorTop3))
                / (Math.pow(p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop3), 2) - p1ConnectingVectorTop2.dotProduct(p1ConnectingVectorTop2) * p1ConnectingVectorTop3.dotProduct(p1ConnectingVectorTop3));

        if (a >= 0 && b >= 0 && a + b <= 1)
            return pointOfContact;

        return null;
    }

    @Override
    public Vector3 normal(Vector3 point, Ray ray) {
        Vector3 normalCpy = normal.cpy();

        Vector3 v = ray.direction.multiply(-1).normalize();
        if (normalCpy.dotProduct(v) < 0)
            return normalCpy.multiply(-1);
        return normalCpy;
    }

}
