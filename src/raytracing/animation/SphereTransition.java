package raytracing.animation;

import raytracing.animation.easings.Easing;
import raytracing.geometry.Sphere;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;

public class SphereTransition extends SimpleTransition {

    private final Sphere sphere;
    private final Vector3 destinationPoint;
    private Vector3 originalPoint;
    private final Easing easing;
    private double dx;
    private double dy;
    private double dz;

    private final Vector3Factory vector3Factory;

    public SphereTransition(
            final Sphere sphere,
            final int totalNumberOfFrames,
            final int startFrame,
            final int endFrame,
            final Vector3 destinationPoint,
            final Easing easing,
            final Vector3Factory vector3Factory) {
        super(totalNumberOfFrames, startFrame, endFrame);
        this.sphere = sphere;
        this.destinationPoint = destinationPoint;
        this.easing = easing;
        this.vector3Factory = vector3Factory;
    }

    @Override
    public boolean act(final int frame) {
        if(!super.act(frame)) {
            return false;
        }

        if(frame == startFrame) {
            this.originalPoint = sphere.getCenter();
            this.dx = (destinationPoint.x - originalPoint.x);// /numberOfFramesOfTransition;
            this.dy = (destinationPoint.y - originalPoint.y);// /numberOfFramesOfTransition;
            this.dz = (destinationPoint.z - originalPoint.z);// /numberOfFramesOfTransition;
        }

        final double t = easing.ease((frame - startFrame) / (double) numberOfFramesOfTransition);
        sphere.setCenter(originalPoint.cpy().addInPlace(vector3Factory.getVector3(t*dx, t*dy, t*dz)));

        return true;
    }
}
