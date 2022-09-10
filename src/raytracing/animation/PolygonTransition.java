package raytracing.animation;

import raytracing.animation.easings.Easing;
import raytracing.geometry.Polygon3D;
import raytracing.math.transformation.PolygonTransformation;

public class PolygonTransition extends SimpleTransition {

    private final Polygon3D polygon;
    private final PolygonTransformation polygonTransformation;
    private final Easing easing;

    public PolygonTransition(
            final Polygon3D polygon,
            final PolygonTransformation polygonTransformation,
            final int totalNumberOfFrames,
            final int startFrame,
            final int endFrame,
            final Easing easing) {
        super(totalNumberOfFrames, startFrame, endFrame);
        this.polygon = polygon;
        this.polygonTransformation = polygonTransformation;
        this.easing = easing;
    }

    @Override
    public boolean act(final int frame) {
        if(!super.act(frame)) {
            return false;
        }

        if(frame > startFrame) {
            final double prevT = easing.ease(((frame-1) - startFrame) / (double) numberOfFramesOfTransition);
            polygonTransformation.reverseTransform(polygon, prevT);
        }

        final double t = easing.ease((frame - startFrame) / (double) numberOfFramesOfTransition);
        polygonTransformation.transform(polygon, t);

        return true;
    }
}
