package raytracing;

import java.awt.Color;

public class Settings {

	private Color backgroundColor;
	private int numOfShadowRays;
	private int maxRecursionLevel;
	private int superSamplingLevel;

	public Settings(Color backgroundColor, int numOfShadowRays, int maxRecursionLevel, int superSamplingLevel) {
		super();
		this.backgroundColor = backgroundColor;
		this.numOfShadowRays = numOfShadowRays;
		this.maxRecursionLevel = maxRecursionLevel;
		this.superSamplingLevel = superSamplingLevel;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
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
