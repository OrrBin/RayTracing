package raytracing.animation;

public abstract class SimpleTransition implements Transition {
    protected final int totalNumberOfFrames;
    protected final int startFrame;
    protected final int endFrame;
    protected final int numberOfFramesOfTransition;

    public SimpleTransition(final int totalNumberOfFrames, final int startFrame, final int endFrame) {
        if(startFrame < 0 || endFrame > totalNumberOfFrames -1 || startFrame >= endFrame) {
            throw new IllegalArgumentException(
                    "start frame: %d, must be between 0 and endFrame: %d.%n endFrame: %d must be between startFrame: %d and %d"
                            .formatted(startFrame, endFrame, endFrame, startFrame, totalNumberOfFrames - 1));
        }

        this.totalNumberOfFrames = totalNumberOfFrames;
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.numberOfFramesOfTransition = endFrame - startFrame;
    }

    @Override
    public boolean act(final int frame) {
        return frame >= startFrame && frame <= endFrame;
    }
}
