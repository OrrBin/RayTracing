package raytracing.video;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import raytracing.actors.Scene;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

public class SimpleImagesToVideoConverter implements ImagesToVideoConverter {

    @Override
    public void createVideo(final List<File> imageFiles, final Scene scene, final String videoFilePath) {
        final List<String> imageFileNames = imageFiles.stream().map(File::getAbsolutePath).collect(Collectors.toList());
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(videoFilePath, scene.getImageWidth(), scene.getImageHeight());
        try {
            recorder.setFrameRate(scene.getStageManager().getFramesPerSecond());
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
            recorder.setVideoBitrate(9000);
            recorder.setFormat("mp4");
            recorder.setVideoQuality(0); // maximum quality
            recorder.start();
            for (String imageFileName : imageFileNames) {
                recorder.record(grabberConverter.convert(cvLoadImage(imageFileName)));
            }
            recorder.stop();
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            e.printStackTrace();
        }
    }
}
