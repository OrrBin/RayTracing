package raytracing.actors;

import raytracing.math.Vector3;

public class Ray {
    public final Vector3 originPoint;
    public final Vector3 direction;
    public final Vector3 directionInverted;
    public final int[] sign;

    public Ray(Vector3 position, Vector3 angle) {
        super();
        this.originPoint = position;
        this.direction = angle;
        this.directionInverted = direction.invert();
        this.sign = new int[] {
                (directionInverted.x < 0) ? 1 : 0,
                (directionInverted.y < 0) ? 1 : 0,
                (directionInverted.z < 0) ? 1 : 0
        };
    }

    public Vector3 getOriginPointCpy() {
        return originPoint.cpy();
    }

    public Vector3 pointAtDistance(double t) {
        return this.originPoint.cpy().add(
                this.direction.multiply(t));
    }

    @Override
    public String toString() {
        return String.format("position: %s, direction: %s", originPoint, direction);
    }

}
