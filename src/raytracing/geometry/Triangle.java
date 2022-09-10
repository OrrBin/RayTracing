package raytracing.geometry;

import lombok.Getter;
import raytracing.actors.Ray;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;
import raytracing.math.util.IntersectionUtils;
import raytracing.util.Constants;

public class Triangle extends Shape {

    @Getter
    private final Vector3 p1;
    @Getter
    private final Vector3 p2;
    @Getter
    private final Vector3 p3;

    private Vector3 normal;
    private double offset;
    private Vector3 p1p2Vec;
    private Vector3 p1p3Vec;
    private double dot1213;
    private double f;
    private double dot1313;
    private double dot1212;

    public double[][] bounds;

    public Triangle(Vector3 vertex1, Vector3 vertex2, Vector3 vertex3,
                    int material, final Vector3Factory vector3Factory) {
        this(vertex1, vertex2, vertex3, material, null,vector3Factory);
    }

    public Triangle(Vector3 vertex1, Vector3 vertex2, Vector3 vertex3,
                    int material, final SuperShape parent, final Vector3Factory vector3Factory) {
        super(material, parent);
        this.p1 = vertex1;
        this.p2 = vertex2;
        this.p3 = vertex3;

        init();

    }

    public void init() {
        Vector3 v = p1.cpy().addInPlace(p2.multiply(-1));
        Vector3 u = p1.cpy().addInPlace(p3.multiply(-1));

        normal = v.crossProduct(u).normalize();
        offset = normal.cpy().dotProduct(p1);

        p1p2Vec = p1.connectingVector(p2);
        p1p3Vec = p1.connectingVector(p3);

        dot1213 = p1p2Vec.dotProduct(p1p3Vec);
        dot1313 = p1p3Vec.dotProduct(p1p3Vec);
        dot1212 = p1p2Vec.dotProduct(p1p2Vec);
        f = Math.pow(dot1213, 2) - dot1212 * dot1313;


        this.bounds = new double[][]{
                {
                        Math.min(p1.x, Math.min(p2.x, p3.x)),
                        Math.min(p1.y, Math.min(p2.y, p3.y)),
                        Math.min(p1.z, Math.min(p2.z, p3.z))
                },
                {
                        Math.max(p1.x, Math.max(p2.x, p3.x)),
                        Math.max(p1.y, Math.max(p2.y, p3.y)),
                        Math.max(p1.z, Math.max(p2.z, p3.z))
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

        Vector3 pointOfContact = ray.getOriginPointCpy().addInPlace(ray.direction.multiply(t));
        Vector3 w = p1.connectingVector(pointOfContact);

        double dotw13 = w.dotProduct(p1p3Vec);
        double dotw12 = w.dotProduct(p1p2Vec);

        double a = (dot1213 * dotw13 - dot1313 * dotw12) / f;
        double b = (dot1213 * dotw12 - dot1212 * dotw13) / f;

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
