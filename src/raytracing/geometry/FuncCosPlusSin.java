package raytracing.geometry;

import raytracing.actors.Ray;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;

public class FuncCosPlusSin extends FunctionGraph {

    private Vector3Factory vector3Factory;

    public FuncCosPlusSin(double zOffset, int material, Vector3Factory vector3Factory) {
        super(material);
        this.zOffset = zOffset;
        this.vector3Factory = vector3Factory;
    }

    private final double zOffset;

    public double func(Vector3 point) {
        return Math.cos(point.x) + Math.sin(point.y) + zOffset - point.z;
    }

    public boolean isOutOfBounds(Vector3 point, Vector3 dir) {
        return ((point.z > (2 + zOffset) && dir.z > 0)) || ((point.z < (-2 + zOffset) && dir.z < 0));
    }

    @Override
    public Vector3 normal(Vector3 point, Ray ray) {
        double x = -1 * Math.sin(point.x);
        double y = Math.cos(point.y);
        double z = -1;

        Vector3 normal = vector3Factory.getVector3(x, y, z);

        Vector3 v = ray.direction.multiply(-1).normalize();
        if (normal.dotProduct(v) < 0)
            return normal.multiply(-1);
        return normal;
    }

    @Override
    protected int maxIterations() {
        return 10000;
    }
}
