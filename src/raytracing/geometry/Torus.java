package raytracing.geometry;

import raytracing.actors.Ray;
import raytracing.math.Transformation3D;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;
import raytracing.math.util.IntersectionUtils;

import java.util.ArrayList;
import java.util.Collections;

public class Torus extends Shape {

    private static final double K_EPSILON = 0.0001;

    private final double sweptRadius;
    private final double tubeRadius;
    private final Vector3 rotation;
    private final Transformation3D transformation;
    private final double[][] bounds;
    private final Vector3Factory vector3Factory;

    public Torus(double sweptRadius, double tubeRadius,
                 Vector3 rotation, Vector3 translation,
                 int material, Vector3Factory vector3Factory) {
        super(material);

        this.vector3Factory = vector3Factory;

        this.sweptRadius = sweptRadius;
        this.tubeRadius = tubeRadius;
        this.rotation = rotation;

        this.transformation = new Transformation3D(vector3Factory);
        transformation.rotateX(rotation.x());
        transformation.rotateY(rotation.y());
        transformation.rotateZ(rotation.z());
        transformation.translate(translation);

        this.bounds = new double[][] {
                { -sweptRadius - tubeRadius, -tubeRadius, -sweptRadius - tubeRadius },
                { sweptRadius + tubeRadius, tubeRadius, sweptRadius + tubeRadius }
        };

    }


    @Override
    public Vector3 intersection(final Ray ray) {
        Ray tfRay = this.transformation.transformRay(ray);

        if (!IntersectionUtils.intersectionWithBoundingBox(tfRay,bounds)) {
            return null;
        }

        Double t = this.findIntersection(tfRay);
        if (t == null)
            return null;

        return ray.pointAtDistance(t); // Changed from the code in github
    }



    @Override
    public Vector3 normal(final Vector3 point, final Ray ray) {
        Ray tfRay = this.transformation.transformRay(ray);

        Double t = this.findIntersection(tfRay);
        if (t == null)
            return null;

        Vector3 localHitPoint = tfRay.pointAtDistance(t);
        Vector3 localNormal = this.computeNormalAtPoint(localHitPoint);

        return this.transformation.transformNormal(localNormal);

    }

    private Vector3 computeNormalAtPoint(final Vector3 localHitPoint) {
        double paramSquared = this.sweptRadius * this.sweptRadius + this.tubeRadius * this.tubeRadius;

        double x = localHitPoint.x();
        double y = localHitPoint.y();
        double z = localHitPoint.z();
        double sumSquared = x * x + y * y + z * z;

        Vector3 tmp = vector3Factory.getVector3(
                4.0 * x * (sumSquared - paramSquared),
                4.0 * y * (sumSquared - paramSquared + 2.0 * this.sweptRadius * this.sweptRadius),
                4.0 * z * (sumSquared - paramSquared));

        return tmp.normalize();
    }

    private Double findIntersection(final Ray ray) {
        double ox = ray.originPoint.x();
        double oy = ray.originPoint.y();
        double oz = ray.originPoint.z();

        double dx = ray.direction.x();
        double dy = ray.direction.y();
        double dz = ray.direction.z();

        // define the coefficients of the quartic equation
        double sumDSqrd = dx * dx + dy * dy + dz * dz;
        double e = ox * ox + oy * oy + oz * oz -
                this.sweptRadius * this.sweptRadius - this.tubeRadius * this.tubeRadius;
        double f = ox * dx + oy * dy + oz * dz;
        double fourASqrd = 4.0 * this.sweptRadius * this.sweptRadius;

        double[] coeffs = new double[]{
                e * e - fourASqrd * (this.tubeRadius * this.tubeRadius - oy * oy),
                4.0 * f * e + 2.0 * fourASqrd * oy * dy,
                2.0 * sumDSqrd * e + 4.0 * f * f + fourASqrd * dy * dy,
                4.0 * sumDSqrd * f,
                sumDSqrd * sumDSqrd
        };

        ArrayList<Double> solution = solve4(coeffs);

        // ray misses the torus
        if (solution == null)
            return null;

        // find the smallest root greater than kEpsilon, if any
        // the roots array is not sorted
        double mint = Double.POSITIVE_INFINITY;
        for (double t : solution) {
            if ((t > K_EPSILON) && (t < mint)) {
                mint = t;
            }
        }

        return mint < Double.POSITIVE_INFINITY ? mint : null;
    }


    /**
     * Solves equation:
     * <p>
     * c[0] + c[1]*x + c[2]*x^2 + c[3]*x^3 + c[4]*x^4 = 0
     */
    private ArrayList<Double> solve4(final double[] coeffs) {
        /* normal form: x^4 + Ax^3 + Bx^2 + Cx + D = 0 */

        double A = coeffs[3] / coeffs[4];
        double B = coeffs[2] / coeffs[4];
        double C = coeffs[1] / coeffs[4];
        double D = coeffs[0] / coeffs[4];

    /*  substitute x = y - A/4 to eliminate cubic term:
	x^4 + px^2 + qx + r = 0 */

        double sq_A = A * A;
        double p = -3.0 / 8 * sq_A + B;
        double q = 1.0 / 8 * sq_A * A - 1.0 / 2 * A * B + C;
        double r = -3.0 / 256 * sq_A * sq_A + 1.0 / 16 * sq_A * B - 1.0 / 4 * A * C + D;
        ArrayList<Double> s;

        if (isZero(r)) {
            /* no absolute term: y(y^3 + py + q) = 0 */

            double[] coeffs2 = new double[]{q, p, 0d, 1d};

            s = solve3(coeffs2);

            s.add(0d);

        } else {
            /* solve the resolvent cubic ... */
            double[] coeffs2 = new double[]{
                    1.0 / 2 * r * p - 1.0 / 8 * q * q,
                    -r,
                    -1.0 / 2 * p,
                    1d
            };

            s = solve3(coeffs2);

            /* ... and take the one real solution ... */

            double z = s.get(0);

            /* ... to build two quadric equations */

            double u = z * z - r;
            double v = 2 * z - p;

            if (isZero(u))
                u = 0;
            else if (u > 0)
                u = Math.sqrt(u);
            else
                return null;

            if (isZero(v))
                v = 0;
            else if (v > 0)
                v = Math.sqrt(v);
            else
                return null;

            coeffs2 = new double[]{
                    z - u,
                    q < 0d ? -v : v,
                    1d
            };

            s = solve2(coeffs2);


            coeffs2 = new double[]{
                    z + u,
                    q < 0d ? v : -v,
                    1d
            };

            s.addAll(solve2(coeffs2));
        }

        /* resubstitute */

        double sub = 1.0 / 4 * A;

        for (int i = 0; i < s.size(); ++i)
            s.set(i, s.get(i) - sub);

        return s;
    }

    private ArrayList<Double> solve3(double[] coeffs) {

        /* normal form: x^3 + Ax^2 + Bx + C = 0 */

        double A = coeffs[2] / coeffs[3];
        double B = coeffs[1] / coeffs[3];
        double C = coeffs[0] / coeffs[3];

    /*  substitute x = y - A/3 to eliminate quadric term:
	x^3 +px + q = 0 */

        double sq_A = A * A;
        double p = 1.0 / 3 * (-1.0 / 3 * sq_A + B);
        double q = 1.0 / 2 * (2.0 / 27 * A * sq_A - 1.0 / 3 * A * B + C);

        /* use Cardano's formula */

        double cbP = p * p * p;
        double D = q * q + cbP;

        ArrayList<Double> s;

        if (isZero(D)) {
            if (isZero(q)) /* one triple solution */ {
                s = new ArrayList<Double>() {{
                    add(0d);
                }};

            } else /* one single and one double solution */ {
                double u = Math.cbrt(-q);
                s = new ArrayList<Double>() {{
                    add(2 * u);
                    add(-u);
                }};
            }
        } else if (D < 0) /* Casus irreducibilis: three real solutions */ {
            double phi = 1.0 / 3 * Math.acos(-q / Math.sqrt(-cbP));
            double t = 2 * Math.sqrt(-p);

            s = new ArrayList<Double>() {{
                add(t * Math.cos(phi));
                add(-t * Math.cos(phi + Math.PI / 3));
                add(-t * Math.cos(phi - Math.PI / 3));
            }};

        } else /* one real solution */ {
            double sqrtD = Math.sqrt(D);
            double u = Math.cbrt(sqrtD - q);
            double v = -Math.cbrt(sqrtD + q);

            s = new ArrayList<>();
            s.add(u + v);

        }

        /* resubstitute */

        double sub = 1.0 / 3 * A;

        for (int i = 0; i < s.size(); ++i)
            s.set(i, s.get(i) - sub);

        return s;
    }

    private static final double EQN_EPS = 1e-9;

    private boolean isZero(double x) {
        return ((x) > -EQN_EPS && (x) < EQN_EPS);
    }

    private ArrayList<Double> solve2(double[] coeffs) {
        /* normal form: x^2 + px + q = 0 */

        double p = coeffs[1] / (2 * coeffs[2]);
        double q = coeffs[0] / coeffs[2];

        double D = p * p - q;

        if (isZero(D)) {
            return new ArrayList<>(Collections.singletonList(-p));
        } else if (D < 0) {
            return new ArrayList<>();
        } else /* if (D > 0) */ {
            double sqrtD = Math.sqrt(D);

            return new ArrayList<Double>() {{
                add(sqrtD - p);
                add(-sqrtD - p);
            }};
        }
    }
}
