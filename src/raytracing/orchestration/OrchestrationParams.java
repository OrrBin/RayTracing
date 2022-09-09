package raytracing.orchestration;

import lombok.Getter;

import java.io.File;
import java.io.IOException;

@Getter
public class OrchestrationParams {

    private final String sceneFileName;
    private final String outputDirectoryFileName;
    private final int imageWidth;
    private final int imageHeight;

    public OrchestrationParams(
            final String sceneFileName,
            final String outputDirectoryFileName,
            final int imageWidth,
            final int imageHeight) throws IOException {
        this.sceneFileName = sceneFileName;
        this.outputDirectoryFileName = outputDirectoryFileName;
        final File outputFile = new File(this.outputDirectoryFileName);
        if(!outputFile.exists()) {
            if (!outputFile.mkdirs()) {
                throw new IOException("output directory does not exist and could not createit");
            }
        }
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }
}
