package raytracing.orchestration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import raytracing.actors.Scene;
import raytracing.math.SimpleVector3;
import raytracing.math.Vector3;
import raytracing.modules.RayTracingModule;
import raytracing.parsing.SceneParser;
import raytracing.rendering.SceneRenderer;
import raytracing.video.ImagesToVideoConverter;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Orchestrator {

    private static final int DEFAULT_IMAGE_WIDTH = 1024;
    private static final int DEFAULT_IMAGE_HEIGHT = 500;

    @Inject
    private SceneParser sceneParser;

    @Inject
    private SceneRenderer sceneRenderer;

    @Inject
    private ImagesToVideoConverter imagesToVideoConverter;


    public void execute(final OrchestrationParams orchestrationParams) throws IOException, InterruptedException {
        // Parse scene file:
        final Scene scene = sceneParser.parseScene(
                new File(orchestrationParams.getSceneFileName()),
                orchestrationParams.getImageWidth(), orchestrationParams.getImageHeight());

        final int FRAME_NUMBER = 50;
        final Vector3 initialPos = new SimpleVector3(-80, 40, 40);
        final Vector3 moveAmount = new SimpleVector3(160, 20, 0);
        final List<Vector3> camPositions = new ArrayList<>();
        for (int i = 0; i < FRAME_NUMBER; i++) {
            camPositions.add(initialPos.cpy().addInPlace(new SimpleVector3(
                    i * (moveAmount.x / FRAME_NUMBER),
                    (i < FRAME_NUMBER / 2.0) ? (i * (moveAmount.y / FRAME_NUMBER)) : ((FRAME_NUMBER - i) * (moveAmount.y / FRAME_NUMBER)),
                    i * (moveAmount.z) / FRAME_NUMBER)));
        }


        long sumTime = 0;
        long maxTime = 0;
        long minTime = Long.MAX_VALUE;

        // Render scene:
        final List<File> imageFiles = new ArrayList<>();
        for (int i = 0; i < camPositions.size(); i++) {
            scene.getCamera().setPosition(camPositions.get(i));
            log.info("Starting rendering iteration: {}", i + 1);
            long startTime = System.currentTimeMillis();
            final File outputFile = getOutputFile(orchestrationParams, scene, i);
            sceneRenderer.renderScene(scene, outputFile);
            imageFiles.add(outputFile);
            long totalTime = System.currentTimeMillis() - startTime;
            sumTime += totalTime;
            maxTime = Math.max(totalTime, maxTime);
            minTime = Math.min(totalTime, minTime);
            log.info("Finished rendering iteration: {} in {} millis", i + 1, totalTime);
        }

        final String videoFilePath = getVideoFileName(orchestrationParams.getOutputDirectoryFileName(),  scene);
        imagesToVideoConverter.createVideo(imageFiles, scene, videoFilePath);

    }

    private File getOutputFile(final OrchestrationParams orchestrationParams, final Scene scene, final int index) {
        return new File(orchestrationParams.getOutputDirectoryFileName(),
                scene.getSceneId() + "_" + index + ".jpeg");
    }

    private String getVideoFileName(final String outputDirectoryFileName, final Scene scene) {
        return outputDirectoryFileName + File.separator + scene.getSceneId() + ".mp4";
    }

    /**
     * Runs the ray tracer. Takes scene file, output image file and image size as
     * input.
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        if (args.length < 2) {
            throw new IllegalArgumentException(
                    "Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");
        }

        final String sceneFileName = args[0];
        final String outputDirectoryFileName = args[1];

        int imageWidth = args.length > 3 ? Integer.parseInt(args[2]) : DEFAULT_IMAGE_WIDTH;
        int imageHeight = args.length > 3 ? Integer.parseInt(args[3]) : DEFAULT_IMAGE_HEIGHT;


        final Orchestrator orchestrator = new Orchestrator();
        final RayTracingModule module = new RayTracingModule();
        final Injector injector = Guice.createInjector(module);
        injector.injectMembers(orchestrator); //injects the implementation of the service

        orchestrator.execute(new OrchestrationParams(sceneFileName, outputDirectoryFileName, imageWidth, imageHeight));
    }

}
