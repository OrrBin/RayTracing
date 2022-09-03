package raytracing.math;

public class EjmlVector3Factory implements Vector3Factory {
    @Override
    public Vector3 getVector3(final double x, final double y, final double z) {
        return new EjmlVector3(x, y, z);
    }
}
