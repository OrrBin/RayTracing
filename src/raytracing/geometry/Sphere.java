package raytracing.geometry;

import lombok.Getter;
import lombok.Setter;
import raytracing.actors.Ray;
import raytracing.geo.LatLon;
import raytracing.math.SimpleVector3;
import raytracing.math.Vector3;
import raytracing.projection.Projection;
import raytracing.util.Constants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Getter
@Setter
public class Sphere extends Shape {

    private Vector3 center;
    private double radius;

    private final BufferedImage image;
    private final BoundingBox boundingBox;
    private final Projection projection;

    public Sphere(final Vector3 center, final double radius, final int material,
                  final String imagePath, final Projection projection) throws IOException {
        super(material);
        this.center = center;
        this.radius = radius;
        if(imagePath != null) {
            final File imageFile = new File(imagePath);
            this.image = ImageIO.read(imageFile);
        } else {
            this.image = null;
        }

        this.projection = projection;

        boundingBox = new BoundingBox(
                center.cpy().addInPlace(new SimpleVector3(-1*radius, -1*radius, -1*radius)),
                center.cpy().addInPlace(new SimpleVector3(radius, radius, radius)));
    }

    @Override
    public Vector3 intersection(Ray ray) {
        if(!boundingBox.intersects(ray)) {
            return null;
        }

        Vector3 centerToOrigin = center.connectingVector(ray.originPoint);

        double b = 2.0 * ray.direction.dotProduct(centerToOrigin);
        double c = Math.pow(centerToOrigin.norm(), 2) - Math.pow(radius, 2);

        double discriminant = Math.pow(b, 2) - 4 * c;
        if (discriminant < 0)
            return null;

        if (discriminant - Constants.EPSILON <= 0) {
            double t = (-b) / (2);

            if (t < 0)
                return null;

            return ray.getOriginPointCpy().addInPlace(ray.direction.multiply(t));
        }
        double t1 = (-b + Math.sqrt(discriminant)) / (2);
        double t2 = (-b - Math.sqrt(discriminant)) / (2);

        if (t1 < 0 && t2 < 0)
            return null;

        Vector3 intersectionPoint1 = ray.getOriginPointCpy().addInPlace(ray.direction.multiply(t1));
        Vector3 intersectionPoint2 = ray.getOriginPointCpy().addInPlace(ray.direction.multiply(t2));

        Vector3 intersectionPoint;

        if (t2 < 0)
            intersectionPoint = intersectionPoint1;
        else if (t1 < 0)
            intersectionPoint = intersectionPoint2;
        else {
            double length1 = ray.originPoint.connectingVector(intersectionPoint1).norm();
            double length2 = ray.originPoint.connectingVector(intersectionPoint2).norm();

            if (length1 < length2)
                intersectionPoint = intersectionPoint1;
            else
                intersectionPoint = intersectionPoint2;
        }

        return intersectionPoint;
    }

    @Override
    public Vector3 normal(Vector3 point, Ray ray) {
        Vector3 n = center.connectingVector(point).normalize();

        Vector3 v = ray.direction.multiply(-1).normalize();
        if (n.dotProduct(v) < 0)
            return n.multiply(-1);
        return n;
    }

    public LatLon getLatLon(final Vector3 point) {
        final Vector3 normalizedPoint = point.cpy().addInPlace(center.multiply(-1));
        double lat = Math.toDegrees(Math.atan2(normalizedPoint.z,
                Math.sqrt(normalizedPoint.x * normalizedPoint.x + normalizedPoint.y * normalizedPoint.y)));
        double lon = Math.toDegrees(Math.atan2(normalizedPoint.y, normalizedPoint.x));

        return new LatLon(lat, lon);
    }

    public Vector3 getCenter() {
        return center.cpy();
    }

}
