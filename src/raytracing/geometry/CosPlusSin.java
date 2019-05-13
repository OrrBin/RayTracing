package raytracing.geometry;


import raytracing.util.Ray;
import raytracing.util.Vector3;

public class CosPlusSin extends Shape {

	public CosPlusSin(double zOffset, int material) {
		super(material);
		this.zOffset = zOffset;
	}

	private double zOffset;
	
	double func(Vector3 point) {
		return  Math.cos(point.getX()) + Math.sin(point.getY()) + zOffset - point.getZ();
	}
	
	boolean isOutOfBounds(Vector3 point, Vector3 dir) {
		return ( (point.getZ() > (2 + zOffset) && dir.getZ() > 0) ) || ( (point.getZ() < (-2 + zOffset) && dir.getZ() < 0) );
	}

	@Override
	public Vector3 intersection(Ray ray) {
		Vector3 point = ray.getOriginPoint();
		Vector3 dir = ray.getDirection();
		double result = func(point);
		boolean isLastPhasePositive = result > 0;
		double epsilon = 0.8;
		
		while(Math.abs(result) > 0.1) {
			if(isOutOfBounds(point, dir))
				return null;
			
			point.add(dir.cpy().multiply(epsilon));
			result = func(point);
			if(isLastPhasePositive && result < 0) {				
				isLastPhasePositive = false;
				dir.multiply(-1);
				epsilon *= 0.5;
			}
			else if(!isLastPhasePositive && result > 0) {				
				isLastPhasePositive = true;
				dir.multiply(-1);
				epsilon *= 0.5;
			}
				
		}
		return point;
	}

	@Override
	public Vector3 normal(Vector3 point, Ray ray) {
		double x = Math.sin(point.getX());
		double y = -2*Math.cos(point.getY());
		double z = -1;

		Vector3 normal = new Vector3(x, y, z);

//		Vector3 v = ray.getDirection().multiply(-1).normalize();
//		if (normal.dotProduct(v) < 0)
//			return normal.multiply(-1);
		return normal;
	}

}
