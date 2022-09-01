package raytracing.rendering;

import lombok.extern.slf4j.Slf4j;
import raytracing.actors.Scene;
import raytracing.io.ImageUtils;
import raytracing.orchestration.PixelTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SceneRendererImpl implements SceneRenderer {
    /**
     * Renders the loaded scene and saves it to the specified file location.
     *
     */
    @Override
    public void renderScene(final Scene scene, final File outputFile) {
        long startTime = System.currentTimeMillis();

        // Create a byte array to hold the pixel data:
        byte[] rgbData = new byte[scene.getImageWidth() * scene.getImageHeight() * 3];

        ExecutorService executor = Executors.newFixedThreadPool(8);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (int top = 0; top < scene.getImageHeight(); top++) {
            for (int left = 0; left < scene.getImageWidth(); left++) {
                tasks.add(new PixelTask(scene, rgbData, top, left));
            }
        }

        log.info("Number of Pixel tasks: {}", tasks.size());

        tasks.stream().parallel().forEach(task -> {
            try {
                task.call();
            } catch (final Exception e) {
                log.error("Failed pixel task", e);
            }
        });

//        executor.invokeAll(tasks);
//        executor.shutdown();

        final long renderTime = System.currentTimeMillis() - startTime;

        log.info("Finished rendering scene in {} milliseconds", renderTime);

        ImageUtils.saveImage(scene.getImageWidth(), rgbData, outputFile);

        log.info("Saved file: {}", outputFile);

    }
}
