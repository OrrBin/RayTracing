package raytracing.actors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;

import java.util.Random;

@Setter
@Getter
@RequiredArgsConstructor
public class Light {
	private final Vector3 position;
	private final Vector3 color;
	private final double specularIntensity;
	private final double shadowIntensity;
	private final double lightRadius;
	private Vector3 up;
	private Vector3Factory vector3Factory;

	/**
	 * Calculate multiple rays for this light source to create shadows. We create
	 * numOfShadowRays rays, from around the light
	 */
	public Ray[] getRays(Vector3 intersectionPoint, int numOfShadowRays) {
		Ray[] rays = new Ray[numOfShadowRays * numOfShadowRays];

		Vector3 normal = this.position.connectingVector(intersectionPoint).normalize();
		Vector3 right = up.crossProduct(normal).normalize();
		Vector3 top = normal.crossProduct(right);

		Random rnd = new Random();

		double rectWidth = this.lightRadius / numOfShadowRays;
		for (int i = 0; i < numOfShadowRays; i++) {
			double yMin = rectWidth * i - this.lightRadius / 2;
			for (int j = 0; j < numOfShadowRays; j++) {
				double xMin = rectWidth * j - this.lightRadius / 2;
				double xOffset = xMin + rnd.nextDouble() * rectWidth;
				double yOffset = yMin + rnd.nextDouble() * rectWidth;

				Vector3 light = this.position.cpy().addInPlace(top.multiply(yOffset)).addInPlace(right.multiply(xOffset));
				rays[i * numOfShadowRays + j] = new Ray(light, light.connectingVector(intersectionPoint).normalize());
			}
		}

		return rays;
	}

	public Vector3 getPosition() {
		return position.cpy();
	}

	public Vector3 getColor() {
		return color.cpy();
	}
}
