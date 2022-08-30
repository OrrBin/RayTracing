package raytracing.util;

import raytracing.math.Vector3;

import java.util.LinkedList;

public class Vector3Pool {

    final LinkedList<Vector3> pool;
    private final int baseSize;
    private final int maxSize;

    public Vector3Pool(final int baseSize, final int maxSize) {
        this.baseSize = baseSize;
        this.maxSize = maxSize;

        pool = new LinkedList<>();
        for(int i = 0; i < baseSize; i++){
            pool.addLast(new Vector3());
        }
    }

    public Vector3 get() {
        synchronized (this) {
            if (pool.isEmpty()) {
                return new Vector3();
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
