package raytracing.animation;

import raytracing.actors.Camera;


public abstract class CameraTransition extends SimpleTransition {

    protected final Camera camera;

    public CameraTransition(
            final int totalNumberOfFrames,
            final int startFrame,
            final int endFrame,
            final Camera camera) {
        super(totalNumberOfFrames, startFrame, endFrame);
        this.camera = camera;
    }
}
