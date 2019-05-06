package raytracing;

import java.util.List;

import raytracing.geometry.Shape;

public class Scene {
	private Settings settings;
	private Camera camera;
	private List<Shape> shapes;
	private List<Material> materials;

	public Scene(Settings settings, Camera camera, List<Shape> shapes, List<Material> materials) {
		super();
		this.settings = settings;
		this.camera = camera;
		this.shapes = shapes;
		this.materials = materials;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public List<Shape> getShapes() {
		return shapes;
	}

	public void setShapes(List<Shape> shapes) {
		this.shapes = shapes;
	}

	public List<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(List<Material> materials) {
		this.materials = materials;
	}

}
