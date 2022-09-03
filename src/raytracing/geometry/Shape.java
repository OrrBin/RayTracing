package raytracing.geometry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raytracing.actors.Ray;
import raytracing.math.Vector3;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Shape {

	protected int material;
	protected SuperShape parent;

	public Shape(final int material) {
		this.material = material;
	}

	public abstract Vector3 intersection(Ray ray);

	public abstract Vector3 normal(Vector3 point, Ray ray);

}
