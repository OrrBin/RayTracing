package raytracing.math.util;

import raytracing.actors.Ray;

public class IntersectionUtils {

    public static boolean intersectionWithBoundingBox(final Ray r, final double[][] bounds) {
        double tmin, tmax, tymin, tymax, tzmin, tzmax;

        tmin = (bounds[r.sign[0]][0] - r.originPoint.x()) * r.directionInverted.x();
        tmax = (bounds[1 - r.sign[0]][0] - r.originPoint.x()) * r.directionInverted.x();
        tymin = (bounds[r.sign[1]][1] - r.originPoint.y()) * r.directionInverted.y();
        tymax = (bounds[1 - r.sign[1]][1] - r.originPoint.y()) * r.directionInverted.y();

        if ((tmin > tymax) || (tymin > tmax))
            return false;

        if (tymin > tmin)
            tmin = tymin;
        if (tymax < tmax)
            tmax = tymax;

        tzmin = (bounds[r.sign[2]][2] - r.originPoint.z()) * r.directionInverted.z();
        tzmax = (bounds[1 - r.sign[2]][2] - r.originPoint.z()) * r.directionInverted.z();

        return (!(tmin > tzmax)) && (!(tzmin > tmax));

    }
}
