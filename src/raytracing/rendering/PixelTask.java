package raytracing.rendering;

import raytracing.actors.Ray;
import raytracing.actors.Scene;
import raytracing.math.SimpleVector3;
import raytracing.math.Vector3;

import java.util.concurrent.Callable;

public class PixelTask implements Callable<Boolean> {

	public PixelTask(Scene scene, byte[] rgbData, int row, int col) {
		this.scene = scene;
		this.rgbData = rgbData;
		this.row = row;
		this.col = col;
	}
	
	private Scene scene;
	private byte[] rgbData;
	private int row;
	private int col;
	
	@Override
	public Boolean call() {
			int N = scene.getSettings().getSuperSamplingLevel();
			int size = Math.max(1, N*N);
			Ray[] rays = scene.getSuperSamplingRays(row, col);

			Vector3 sum = new SimpleVector3(0,0,0);
			for(int i = 0; i < size; i++)
				sum.addInPlace(scene.calculateColor(rays[i]));

			
			Vector3 color = sum.multiply(1/((double)(size)));
			rgbData[(row* scene.getImageWidth() + col) * 3] = (byte) ((int) (color.x * 255));
			rgbData[(row * scene.getImageWidth() + col) * 3 + 1] = (byte) ((int) (color.y * 255));
			rgbData[(row * scene.getImageWidth() + col) * 3 + 2] = (byte) ((int) (color.z * 255));
		
		return true;
	}


 
}
