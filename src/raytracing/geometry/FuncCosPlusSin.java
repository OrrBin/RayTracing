package raytracing.geometry;

import raytracing.actors.Ray;
import raytracing.math.Vector3;

public class FuncCosPlusSin extends FunctionGraph {

	public FuncCosPlusSin(double zOffset, int material) {
		super(material);
		this.zOffset = zOffset;
	}

	private final double zOffset;

	public double func(Vector3 point) {
		return  Math.cos(point.getX()) + Math.sin(point.getY()) + zOffset - point.z;
	}

	public boolean isOutOfBounds(Vector3 point, Vector3 dir) {
		return ( (point.getZ() > (2 + zOffset) && dir.getZ() > 0) ) || ( (point.getZ() < (-2 + zOffset) && dir.getZ() < 0) );
	}

	@Override
	public Vector3 normal(Vector3 point, Ray ray) {
		double x = -1*Math.sin(point.getX());
		double y = Math.cos(point.getY());
		double z = -1;

		Vector3 normal = new Vector3(x, y, z);

		Vector3 v = ray.direction.multiply(-1).normalize();
		if (normal.dotProduct(v) < 0)
			return normal.multiply(-1);
		return normal;
	}

	@Override
	protected int maxIterations() {
		return 10000;
	}
}
