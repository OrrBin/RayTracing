package raytracing.actors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raytracing.math.Vector3;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Settings {

	private Vector3 backgroundColor;
	private int numOfShadowRays;
	private int maxRecursionLevel;
	private int superSamplingLevel;

	public Vector3 getBackgroundColor() {
		return backgroundColor.cpy();
	}
}
