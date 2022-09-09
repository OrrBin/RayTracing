package raytracing.video;

import raytracing.actors.Scene;

import java.io.File;
import java.util.List;

public interface ImagesToVideoConverter {

    void createVideo(List<File> imageFiles, Scene scene, String videoFilePath);
}
