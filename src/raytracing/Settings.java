package raytracing;

import raytracing.util.Vector3;

public class Settings {

	private Vector3 backgroundColor;
	private int numOfShadowRays;
	private int maxRecursionLevel;
	private int superSamplingLevel;
	
	public Settings(){
	}
	
	public Settings(Vector3 backgroundColor, int numOfShadowRays, int maxRecursionLevel, int superSamplingLevel) {
		this.backgroundColor = backgroundColor;
		this.numOfShadowRays = numOfShadowRays;
		this.maxRecursionLevel = maxRecursionLevel;
		this.superSamplingLevel = superSamplingLevel;
	}

	public Vector3 getBackgroundColor() {
		return backgroundColor.cpy();
	}

	public void setBackgroundColor(Vector3 backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getNumOfShadowRays() {
		return numOfShadowRays;
	}

	public void setNumOfShadowRays(int numOfShadowRays) {
		this.numOfShadowRays = numOfShadowRays;
	}

	public int getMaxRecursionLevel() {
		return maxRecursionLevel;
	}

	public void setMaxRecursionLevel(int maxRecursionLevel) {
		this.maxRecursionLevel = maxRecursionLevel;
	}

	public int getSuperSamplingLevel() {
		return superSamplingLevel;
	}

	public void setSuperSamplingLevel(int superSamplingLevel) {
		this.superSamplingLevel = superSamplingLevel;
	}

}
