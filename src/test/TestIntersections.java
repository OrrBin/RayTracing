package test;

import raytracing.geometry.Plane;
import raytracing.geometry.Sphere;
import raytracing.geometry.Triangle;
import raytracing.util.Ray;
import raytracing.util.Vector3;

public class TestIntersections {

	public static void main(String[] args) {
		Sphere s = new Sphere(new Vector3(3, 3, 3), 1, 0);
		Ray ray = new Ray(new Vector3(1,1,2), new Vector3(0.5,1,0.5));
		
		Vector3 ip = s.intersection(ray);
		
		System.out.println("sphere ip: " + ip);
		
		Triangle t = new Triangle(new Vector3(1, 0 , -1), new Vector3(2, 1 , 1), new Vector3(3, 2 , 0), 0);
		ray = new Ray(new Vector3(0,0,0), new Vector3(1,0.3333333333334,0));
		
		ip = t.intersection(ray);
		
		System.out.println("triangle ip: " + ip);
		
		Plane p = new Plane(new Vector3(1,1,1), 2, 0);
		ray = new Ray(new Vector3(0,0,0), new Vector3(0,1,0));
		
		ip = p.intersection(ray);
		
		System.out.println("plane ip: " + ip);

	}
}
