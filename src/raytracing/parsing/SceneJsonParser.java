package raytracing.parsing;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import raytracing.actors.Scene;
import raytracing.actors.SceneSettings;
import raytracing.animation.StageManager;
import raytracing.math.Vector3Factory;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;

@Slf4j
@AllArgsConstructor()
public class SceneJsonParser implements SceneParser {

    private StageManager stageManager;
    private Vector3Factory vector3Factory;
    private Gson gson;

    @Override
    public Scene parseScene(final File sceneFile, final int imageWidth, final int imageHeight) throws IOException {

        final String sceneId = sceneFile.getName().substring(0, sceneFile.getName().indexOf("."));
        final Scene scene = new Scene(sceneId, imageWidth, imageHeight, stageManager, vector3Factory);

        log.info("Started parsing json scene file: {}", sceneFile.getAbsolutePath());

        // create a reader
        final Reader reader = Files.newBufferedReader(sceneFile.toPath());

        // convert JSON string to User object
        final SceneSettings sceneSettings = gson.fromJson(reader, SceneSettings.class);

        scene.setCamera(sceneSettings.getCamera());
        scene.setSettings(sceneSettings.getSettings());
        scene.setLights(sceneSettings.getLights());
        scene.getLights().forEach(light -> light.setUp(scene.getCamera().getUpVector()));
        scene.setMaterials(sceneSettings.getMaterials());
        sceneSettings.getShapes().forEach(scene::addShape);
        sceneSettings.getSuperShapes().forEach(scene::addSuperShape);

        log.info("Finished parsing json scene file: {}", sceneFile.getAbsolutePath());

        scene.getStageManager().init(sceneSettings.getSettings().getNumberOfFrames(), sceneSettings.getSettings().getFramesPerSecond());
        return scene;
    }
}
