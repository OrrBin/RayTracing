package raytracing.math;

public class VectorAPIVector3Factory implements Vector3Factory {
    @Override
    public VectorAPIVector3 getVector3(final double x, final double y, final double z) {
        return new VectorAPIVector3(x, y, z);
    }
}
