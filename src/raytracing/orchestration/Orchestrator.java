package raytracing.orchestration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import raytracing.actors.Scene;
import raytracing.modules.RayTracingModule;
import raytracing.parsing.SceneParser;
import raytracing.rendering.SceneRenderer;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

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

        // Render scene:
        sceneRenderer.renderScene(scene, new File(orchestrationParams.getOutputFileName()));
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


        final Orchestrator orchestrator = new Orchestrator();

        RayTracingModule module = new RayTracingModule();
        Injector injector = Guice.createInjector(module);
        injector.injectMembers(orchestrator); //injects the implementation of the service

//        Scanner scanner = new Scanner(System.in);
//        scanner.nextLine();

        orchestrator.execute(new OrchestrationParams(sceneFileName, outputFileName, imageWidth, imageHeight));
    }

}
