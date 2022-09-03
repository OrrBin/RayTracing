package raytracing.util;

import raytracing.math.ArrVector3;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;

import java.util.LinkedList;

public class Vector3Pool {

    private final LinkedList<Vector3> pool;
    private final int baseSize;
    private final int maxSize;
    private final Vector3Factory vector3Factory;

    public Vector3Pool(final int baseSize, final int maxSize, final Vector3Factory vector3Factory) {
        this.baseSize = baseSize;
        this.maxSize = maxSize;

        this.vector3Factory = vector3Factory;

        pool = new LinkedList<>();
        for(int i = 0; i < baseSize; i++){
            pool.addLast(new ArrVector3()); // TODO: This should be changed to use Vector3Factory if used
        }
    }

    public Vector3 get() {
        synchronized (this) {
            if (pool.isEmpty()) {
                return new ArrVector3(); // TODO: This should be changed to use Vector3Factory if used
            }

            return pool.getLast();
        }
    }

    public void returnToPool(final Vector3 v) {
        synchronized (this) {
            if(pool.size() < maxSize) {
                pool.addLast(v);
            }
        }
    }
}
