package raytracing.math;

public class ArrVector3Factory implements Vector3Factory {
    @Override
    public Vector3 getVector3(final double x, final double y, final double z) {
        return new ArrVector3(x, y, z);
    }
}
