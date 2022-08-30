package raytracing.parsing;

import raytracing.actors.Scene;

import java.io.File;
import java.io.IOException;

public interface SceneParser {

    Scene parseScene(File sceneFile, int imageWidth, int imageHeight) throws IOException;
}
