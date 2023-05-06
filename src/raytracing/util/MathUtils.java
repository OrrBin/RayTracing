package raytracing.util;

public class MathUtils {

    public static double bound(double val, double valMin, double valMax) {
        double res;
        res = Math.max(val, valMin);
        res = Math.min(res, valMax);
        return res;
    }
}
