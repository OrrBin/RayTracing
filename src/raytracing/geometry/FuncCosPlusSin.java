package raytracing.geometry;


import raytracing.util.Vector3;

public class FuncCosPlusSin extends FunctionGraph {

	public FuncCosPlusSin(double zOffset, int material) {
		super(material);
		this.zOffset = zOffset;
	}

	private double zOffset;
	
	public double func(Vector3 point) {
		return  Math.cos(point.getX()) + Math.sin(point.getY()) + zOffset - point.getZ();
	}
	
	public boolean isOutOfBounds(Vector3 point, Vector3 dir) {
		return ( (point.getZ() > (2 + zOffset) && dir.getZ() > 0) ) || ( (point.getZ() < (-2 + zOffset) && dir.getZ() < 0) );
	}

	@Override
	protected int maxIterations() {
		return 1000000;
	}
}
