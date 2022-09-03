package raytracing.math;

public class SimpleVector3Factory implements Vector3Factory {
    @Override
    public Vector3 getVector3(final double x, final double y, final double z) {
        return new SimpleVector3(x, y, z);
    }
}
