package raytracing.orchestration;

import lombok.extern.slf4j.Slf4j;
import raytracing.actors.Scene;
import raytracing.math.SimpleVector3;
import raytracing.math.SimpleVector3Factory;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;
import raytracing.parsing.SceneParser;
import raytracing.parsing.SceneParserCustomFormat;
import raytracing.rendering.SceneRenderer;
import raytracing.rendering.SceneRendererImpl;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

@Slf4j
public class Orchestrator {

    private static final int DEFAULT_IMAGE_WIDTH = 1024;
    private static final int DEFAULT_IMAGE_HEIGHT = 500;

    @Inject
    private SceneParser sceneParser;

    @Inject
    private SceneRenderer sceneRenderer;



    public void execute(final OrchestrationParams orchestrationParams) throws IOException, InterruptedException {
        // Parse scene file:
        final Scene scene = sceneParser.parseScene(
                new File(orchestrationParams.getSceneFileName()),
                orchestrationParams.getImageWidth(),orchestrationParams.getImageHeight());

        Vector3[] camPositions = new Vector3[] {
                new SimpleVector3(40 + 10, 100, 50),
                new SimpleVector3(40.2 + 10, 99, 49),
                new SimpleVector3(40.4 + 10, 98, 48),
                new SimpleVector3(40.6 + 10, 97, 47),
                new SimpleVector3(40.8 + 10, 96, 46),
                new SimpleVector3(41 + 10, 95, 45),
                new SimpleVector3(41.2 + 10, 94, 44),
                new SimpleVector3(41.4 + 10, 93, 43),
                new SimpleVector3(41.6 + 10, 92, 42),
                new SimpleVector3(41.8 + 10, 91, 41),
                new SimpleVector3(42   + 10, 91, 40),
                new SimpleVector3(42.2 + 10, 90, 39),
                new SimpleVector3(42.4 + 10, 89, 38),
                new SimpleVector3(42.6 + 10, 88, 37),
                new SimpleVector3(42.8 + 10, 87, 36),
                new SimpleVector3(43   + 10, 86, 35),
                new SimpleVector3(43.2 + 10, 85, 34),
                new SimpleVector3(43.4 + 10, 84, 33),
                new SimpleVector3(43.6 + 10, 83, 32),
                new SimpleVector3(43.8 + 10, 82, 31),
                new SimpleVector3(44   + 10, 81, 30),
                new SimpleVector3(44.2 + 10, 80, 29),
                new SimpleVector3(44.4 + 10, 79, 28),
                new SimpleVector3(44.6 + 10, 78, 27),
                new SimpleVector3(44.8 + 10, 77, 26),
                new SimpleVector3(45   + 10, 76, 25),
                new SimpleVector3(45.2 + 10, 75, 24),
                new SimpleVector3(45.4 + 10, 74, 23),
                new SimpleVector3(45.6 + 10, 73, 22),
                new SimpleVector3(45.8 + 10, 72, 21),
                new SimpleVector3(46   + 10, 71, 20),
        };


        long sumTime = 0;
        long maxTime = 0;
        long minTime = Long.MAX_VALUE;

        // Render scene:

        for(int i = 0; i < camPositions.length; i++) {
            scene.getCamera().setPosition(camPositions[i]);
            log.info("Starting rendering iteration: {}", i + 1);
            long startTime = System.currentTimeMillis();
            sceneRenderer.renderScene(scene, new File(orchestrationParams.getOutputFileName() + "_" + i + ".png"));
            long totalTime = System.currentTimeMillis() - startTime;
            sumTime += totalTime;
            maxTime = Math.max(totalTime, maxTime);
            minTime = Math.min(totalTime, minTime);
            log.info("Finished rendering iteration: {} in {} millis", i + 1, totalTime);

        }
    }

    /**
     * Runs the ray tracer. Takes scene file, output image file and image size as
     * input.
     *
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        if (args.length < 2) {
            throw new IllegalArgumentException(
                    "Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");
        }

        final String sceneFileName = args[0];
        final String outputFileName = args[1];

        int imageWidth = args.length > 3 ? Integer.parseInt(args[2]) : DEFAULT_IMAGE_WIDTH;
        int imageHeight = args.length > 3 ? Integer.parseInt(args[3]) : DEFAULT_IMAGE_HEIGHT;

        final Vector3Factory vector3Factory = new SimpleVector3Factory();
//        final Vector3Factory vector3Factory = new EjmlVector3Factory();
//        final Vector3Factory vector3Factory = new VectorAPIVector3Factory();
        final Orchestrator orchestrator = new Orchestrator();
        orchestrator.sceneParser = new SceneParserCustomFormat(vector3Factory);
        orchestrator.sceneRenderer = new SceneRendererImpl();

//        RayTracingModule module = new RayTracingModule();
//        Injector injector = Guice.createInjector(module);
//        injector.injectMembers(orchestrator); //injects the implementation of the service

        orchestrator.execute(new OrchestrationParams(sceneFileName, outputFileName, imageWidth, imageHeight));
    }

}
