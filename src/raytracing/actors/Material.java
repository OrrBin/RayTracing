package raytracing.actors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raytracing.math.Vector3;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Material {
	private Vector3 diffuseColor;
	private Vector3 specularColor;
	private Vector3 reflectionColor;
	private double  phongSpecularityCoefficient;
	private double  transparency;

	public Vector3 getDiffuseColor() {
		return diffuseColor.cpy();
	}

	public Vector3 getSpecularColor() {
		return specularColor.cpy();
	}

	public Vector3 getReflectionColor() {
		return reflectionColor.cpy();
	}

}
