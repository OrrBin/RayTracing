package raytracing.animation.easings;

public class BezierBlendEase implements Easing {
    @Override
    public double ease(final double t) {
        return t * t * (3.0d - 2.0f * t);
    }
}
