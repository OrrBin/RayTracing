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
			Ray ray = scene.constructRay(row, col);
			Vector3 color = scene.calculateColor(ray);
			rgbData[(row* scene.imageWidth + col) * 3] = (byte) ((int) (color.getX() * 255));
			rgbData[(row * scene.imageWidth + col) * 3 + 1] = (byte) ((int) (color.getY() * 255));
			rgbData[(row * scene.imageWidth + col) * 3 + 2] = (byte) ((int) (color.getZ() * 255));
		
		return true;
	}


 
}
