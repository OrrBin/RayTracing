package raytracing.geometry;


import raytracing.actors.Ray;
import raytracing.math.Vector3;

public abstract class FunctionGraph extends Shape {

	public FunctionGraph(int material) {
		super(material);
	}
	
	abstract protected int maxIterations();
	
	public abstract double func(Vector3 point);
	
	public abstract boolean isOutOfBounds(Vector3 point, Vector3 dir);

	@Override
	public Vector3 intersection(Ray ray) {
		Vector3 point = ray.getOriginPointCpy();
		Vector3 dir = ray.direction.cpy();
		double result = func(point);
		boolean isLastPhasePositive = result > 0;
		int iterationIdx = 0;
		double epsilon = 1;
		
		while(Math.abs(result) > 0.01) {
			if(iterationIdx++ > maxIterations()) {
				return null;
			}
			if(isOutOfBounds(point, dir))
				return null;
			
			point.addInPlace(dir.multiply(epsilon));
			result = func(point);
			if(isLastPhasePositive && result < 0) {				
				isLastPhasePositive = false;
				dir = dir.multiply(-1);
				epsilon *= 0.5;
			}
			else if(!isLastPhasePositive && result > 0) {				
				isLastPhasePositive = true;
				dir = dir.multiply(-1);
				epsilon *= 0.5;
			}
				
		}
		return point;
	}



}
