package raytracing.rendering;

import raytracing.actors.Scene;

import java.io.File;

public interface SceneRenderer {
    void renderScene(final Scene scene, final File outputFile) throws InterruptedException;
}
