package raytracing.orchestration;

import lombok.Getter;

@Getter
public class OrchestrationParams {

    private final String sceneFileName;
    private final String outputFileName;
    private final int imageWidth;
    private final int imageHeight;

    public OrchestrationParams(final String sceneFileName, final String outputFileName, final int imageWidth, final int imageHeight) {
        this.sceneFileName = sceneFileName;
        this.outputFileName = outputFileName;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }
}
