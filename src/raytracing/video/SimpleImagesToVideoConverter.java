package raytracing.video;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import raytracing.actors.Scene;
import raytracing.math.SimpleVector3Factory;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.bytedeco.opencv.helper.opencv_imgcodecs.cvLoadImage;

public class SimpleImagesToVideoConverter implements ImagesToVideoConverter {

    @Override
    public void createVideo(final List<File> imageFiles, final Scene scene, final String videoFilePath) {
        final List<String> imageFileNames = imageFiles.stream().map(File::getAbsolutePath).collect(Collectors.toList());
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(videoFilePath, scene.getImageWidth(), scene.getImageHeight());
        try {
            recorder.setFrameRate(25);
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

    public static void main(String[] args) {
        final SimpleImagesToVideoConverter simpleImagesToVideoConverter = new SimpleImagesToVideoConverter();
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Enter path for directory with the images");
        final String imageDirectoryPath = scanner.nextLine();
        System.out.println("Enter video file name");
        final String videoFileName = scanner.nextLine();
        System.out.println("Enter scene name");
        final String sceneName = scanner.nextLine();
        System.out.println("Enter video width");
        final int width = scanner.nextInt();
        System.out.println("Enter video height");
        final int height = scanner.nextInt();

        final File imageDirectoryFile = new File(imageDirectoryPath);
        final File[] imageFiles = imageDirectoryFile.listFiles((dir, fileName) -> fileName.endsWith("jpeg"));

        assert imageFiles != null;
        simpleImagesToVideoConverter.createVideo(
                Arrays.stream(imageFiles).sorted(Comparator.comparing(File::lastModified)).collect(Collectors.toList()),
                new Scene(sceneName, width, height, new SimpleVector3Factory()),
                imageDirectoryPath + File.separator + videoFileName);
    }
}
