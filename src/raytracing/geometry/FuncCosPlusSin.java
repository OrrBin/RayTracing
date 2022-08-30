package raytracing.geometry;


import raytracing.actors.Ray;
import raytracing.math.Vector3;

public class FuncCosPlusSin extends FunctionGraph {

	public FuncCosPlusSin(double zOffset, int material) {
		super(material);
		this.zOffset = zOffset;
	}

	private double zOffset;
	
	public double func(Vector3 point) {

//		return  Math.cos(point.getX()) + Math.sin(point.getY()) + zOffset - point.getZ();
		return point.getX() * Math.cos(point.getX()) + point.getY() * Math.sin(point.getY()) + zOffset - point.getZ();
//		return  Math.pow(point.getX(), 2) + Math.pow(point.getY(), 2) - point.getZ();
	}

	@Override
	public Vector3 normal(Vector3 point, Ray ray) {
//		double x = -1*Math.sin(point.getX());
//		double y = Math.cos(point.getY());
//		double z = -1;

		double x = -1*Math.cos(point.getX()) + point.getX() * Math.sin(point.getX());
		double y = -1*Math.sin(point.getY()) - point.getY() * Math.cos(point.getY());
		double z = 1;

//		double x = 2 * (point.getX());
//		double y = 2 * (point.getY());
//		double z = -1;



		Vector3 normal = new Vector3(x, y, z).normalize();

		Vector3 v = ray.getDirection().multiply(-1).normalize();
		if (normal.dotProduct(v) < 0)
			return normal.multiply(-1);
		return normal;
	}
	
	public boolean isOutOfBounds(Vector3 point, Vector3 dir) {
//		return point.getZ() > 6;
//		return ( (point.getZ() > (2 + zOffset) && dir.getZ() > 0) ) ||
//				( (point.getZ() < (-2 + zOffset) && dir.getZ() < 0) );

//		 return (point.getX() > 6 && dir.getX() > 0) || (point.getY() > 6 && dir.getY() > 0) ||
//				(point.getX() < -6 && dir.getX() < 0) || (point.getY() < -6 && dir.getY() < 0);

		return false;
	}



	@Override
	protected int maxIterations() {
		return 10000;
	}
}
