package raytracing.geometry;


import raytracing.util.Ray;
import raytracing.util.Vector3;

public abstract class FunctionGraph extends Shape {

	public FunctionGraph(int material) {
		super(material);
	}
	
	abstract protected int maxIterations();
	
	public abstract double func(Vector3 point);
	
	public abstract boolean isOutOfBounds(Vector3 point, Vector3 dir);

	@Override
	public Vector3 intersection(Ray ray) {
		Vector3 point = ray.getOriginPoint();
		Vector3 dir = ray.getDirection();
		double result = func(point);
		boolean isLastPhasePositive = result > 0;
		double z = point.getZ();
		int iterationIdx = 0;
		double epsilon = 1;
		
		while(Math.abs(result) > 0.01) {
			if(iterationIdx++ > maxIterations()) {
				return null;
			}
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

		Vector3 v = ray.getDirection().multiply(-1).normalize();
		if (normal.dotProduct(v) < 0)
			return normal.multiply(-1);
		return normal;
	}

}
