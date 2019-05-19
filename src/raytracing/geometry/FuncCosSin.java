package raytracing.geometry;

import raytracing.util.Vector3;

public class FuncCosSin extends FunctionGraph{

	
	private double cosCoeff;
	private double sinCoeff;
	private double boundry;
	
	public FuncCosSin(double cosCoeff, double sinCoeff,int material) {
		super(material);
		this.cosCoeff = cosCoeff;
		this.sinCoeff = sinCoeff;
		boundry = Math.abs(sinCoeff) + Math.abs(cosCoeff);
	}

	public double func(Vector3 point) {
		return  cosCoeff*Math.cos(point.getZ()) + sinCoeff*Math.sin(point.getY()) + - point.getX();
	}
	
	public boolean isOutOfBounds(Vector3 point, Vector3 dir) {
		return ( (point.getX() > (boundry) && dir.getX() > 0) ) || ( (point.getX() < (-boundry) && dir.getX() < 0) );
	}

	@Override
	protected int maxIterations() {
		return 1000000;
	}

}
