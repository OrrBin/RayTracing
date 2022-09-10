package raytracing.animation.easings;

public class EaseOutQuad implements Easing {
    @Override
    public double ease(final double t) {
        return 1 - (1 - t) * (1 - t);
    }
}
