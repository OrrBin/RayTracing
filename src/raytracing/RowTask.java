package raytracing;

import java.util.concurrent.Callable;

import raytracing.util.Ray;
import raytracing.util.Vector3;

public class RowTask implements Callable<Boolean> {

	public RowTask(Scene scene, byte[] rgbData, int row) {
		this.scene = scene;
		this.rgbData = rgbData;
		this.row = row;
	}
	
	private Scene scene;
	private byte[] rgbData;
	private int row;
	
	@Override
	public Boolean call() throws Exception {
		for (int left = 0; left < scene.imageWidth; left++) {
			Ray ray = scene.constructRay(row, left);
			Vector3 color = scene.calculateColor(ray);
			rgbData[(row* scene.imageWidth + left) * 3] = (byte) ((int) (color.getX() * 255));
			rgbData[(row * scene.imageWidth + left) * 3 + 1] = (byte) ((int) (color.getY() * 255));
			rgbData[(row * scene.imageWidth + left) * 3 + 2] = (byte) ((int) (color.getZ() * 255));
		}
		
		return true;
	}


 
}
