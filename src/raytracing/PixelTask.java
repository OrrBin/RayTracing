package raytracing;

import java.util.concurrent.Callable;

import raytracing.util.Ray;
import raytracing.util.Vector3;

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
	public Boolean call() throws Exception {
			int N = scene.getSettings().getSuperSamplingLevel();
			int size = Math.max(1, N*N);
			Ray[] rays = scene.getSuperSamplingRays(row, col);
			Vector3[] colors = new Vector3[size];
			for(int i = 0; i < size; i++)
				colors[i] = scene.calculateColor(rays[i]);
			
			Vector3 sum = new Vector3(0,0,0);
			for(int i = 0; i < size; i++)
				sum.add(colors[i]);
			
			Vector3 color = sum.multiply(1/((double)(size)));
			rgbData[(row* scene.imageWidth + col) * 3] = (byte) ((int) (color.getX() * 255));
			rgbData[(row * scene.imageWidth + col) * 3 + 1] = (byte) ((int) (color.getY() * 255));
			rgbData[(row * scene.imageWidth + col) * 3 + 2] = (byte) ((int) (color.getZ() * 255));
		
		return true;
	}


 
}
