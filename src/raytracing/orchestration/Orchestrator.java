package raytracing.orchestration;

import lombok.extern.slf4j.Slf4j;
import raytracing.actors.Scene;
import raytracing.math.ArrVector3Factory;
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

    private static final int DEFAULT_IMAGE_WIDTH = 500;
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

        int numOfIterations = 5;
        long sumTime = 0;
        long maxTime = 0;
        long minTime = Long.MAX_VALUE;

        // Render scene:

        for(int i = 0; i < numOfIterations; i++) {
            log.info("Starting rendering iteration: {}", i + 1);
            long startTime = System.currentTimeMillis();
            sceneRenderer.renderScene(scene, new File(orchestrationParams.getOutputFileName()));
            long totalTime = System.currentTimeMillis() - startTime;
            sumTime += totalTime;
            maxTime = Math.max(totalTime, maxTime);
            minTime = Math.min(totalTime, minTime);
            log.info("Finished rendering iteration: {} in {} millis", i + 1, totalTime);

        }

        log.info("Number of iterations: {}, average time: {}, max time: {}, min time: {}",
                numOfIterations, sumTime / numOfIterations, maxTime, minTime);
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

        final Vector3Factory vector3Factory = new ArrVector3Factory();
//        final Vector3Factory vector3Factory = new VectorAPIVector3Factory();
        final Orchestrator orchestrator = new Orchestrator();
        orchestrator.sceneParser = new SceneParserCustomFormat(vector3Factory);
        orchestrator.sceneRenderer = new SceneRendererImpl();

//        RayTracingModule module = new RayTracingModule();
//        Injector injector = Guice.createInjector(module);
//        injector.injectMembers(orchestrator); //injects the implementation of the service

//        Scanner scanner = new Scanner(System.in);
//        scanner.nextLine();

        orchestrator.execute(new OrchestrationParams(sceneFileName, outputFileName, imageWidth, imageHeight));
    }

}
