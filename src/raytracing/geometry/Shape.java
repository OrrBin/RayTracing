package raytracing.geometry;

import raytracing.util.Ray;
import raytracing.util.Vector3;

public interface Shape {
	
	Vector3 intersection(Ray ray);
	Vector3 normal(Vector3 point, Ray ray);
}
