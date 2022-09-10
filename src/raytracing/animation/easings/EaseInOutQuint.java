package raytracing.animation.easings;

public class EaseInOutQuint implements Easing {
    @Override
    public double ease(final double t) {
        return t < 0.5 ? 16 * t * t * t * t * t : 1 - Math.pow(-2 * t + 2, 5) / 2;
    }
}
