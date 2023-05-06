package raytracing.geometry;

import lombok.AllArgsConstructor;
import raytracing.actors.Ray;
import raytracing.math.Vector3;

@AllArgsConstructor
public class BoundingBox {
    final Vector3 min;
    final Vector3 max;

    public boolean intersects(Ray ray) {
        double tx1 = (min.x - ray.originPoint.x)*ray.direction.invert().x;
        double tx2 = (max.x - ray.originPoint.x)*ray.direction.invert().x;

        double tmin = Math.min(tx1, tx2);
        double tmax = Math.max(tx1, tx2);

        double ty1 = (min.y - ray.originPoint.y)*ray.direction.invert().y;
        double ty2 = (max.y - ray.originPoint.y)*ray.direction.invert().y;

        tmin = Math.max(tmin, Math.min(ty1, ty2));
        tmax = Math.min(tmax, Math.max(ty1, ty2));

        double tz1 = (min.z - ray.originPoint.z)*ray.direction.invert().z;
        double tz2 = (max.z - ray.originPoint.z)*ray.direction.invert().z;

        tmin = Math.max(tmin, Math.min(tz1, tz2));
        tmax = Math.min(tmax, Math.max(tz1, tz2));

        return tmax >= tmin;
    }
}
