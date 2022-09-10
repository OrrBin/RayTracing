package raytracing.animation;

import raytracing.actors.Camera;
import raytracing.animation.easings.Easing;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;

public class LinearCameraTransition extends CameraTransition {

    private final Vector3 destinationPoint;
    private Vector3 originalPoint;
    private final Easing easing;
    private double dx;
    private double dy;
    private double dz;

    private final Vector3Factory vector3Factory;

    public LinearCameraTransition(
            final int totalNumberOfFrames,
            final int startFrame,
            final int endFrame,
            final Camera camera,
            final Vector3 destinationPoint,
            final Easing easing,
            final Vector3Factory vector3Factory) {
        super(totalNumberOfFrames, startFrame, endFrame, camera);
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
            this.originalPoint = camera.getPosition();
            this.dx = (destinationPoint.x - originalPoint.x);// /numberOfFramesOfTransition;
            this.dy = (destinationPoint.y - originalPoint.y);// /numberOfFramesOfTransition;
            this.dz = (destinationPoint.z - originalPoint.z);// /numberOfFramesOfTransition;
        }

        final double t = easing.ease((frame - startFrame) / (double) numberOfFramesOfTransition);
        System.out.println("t: " + t);
        camera.setPosition(originalPoint.cpy().addInPlace(vector3Factory.getVector3(t*dx, t*dy, t*dz)));

        return true;
    }
}
