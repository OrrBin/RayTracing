package raytracing.actors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raytracing.geometry.Shape;
import raytracing.geometry.SuperShape;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SceneSettings {

    private Settings settings;
    private Camera camera;
    private List<Light> lights;
    private List<Shape> shapes;
    private List<SuperShape> superShapes;
    private List<Material> materials;
}
