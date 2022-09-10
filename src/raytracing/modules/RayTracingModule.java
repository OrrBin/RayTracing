package raytracing.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import raytracing.geometry.Shape;
import raytracing.geometry.SuperShape;
import raytracing.math.SimpleVector3Factory;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;
import raytracing.parsing.SceneJsonParser;
import raytracing.parsing.SceneParser;
import raytracing.parsing.serialization.json.CustomVector3Deserializers;
import raytracing.parsing.serialization.json.ShapeDeserializer;
import raytracing.parsing.serialization.json.SuperShapeDeserializer;
import raytracing.rendering.SceneRenderer;
import raytracing.rendering.SceneRendererImpl;
import raytracing.video.ImagesToVideoConverter;
import raytracing.video.SimpleImagesToVideoConverter;

import javax.inject.Inject;

public class RayTracingModule extends AbstractModule {

    protected void configure() {

    }

    @Provides
    public Vector3Factory getVector3Factory() {
        return new SimpleVector3Factory();
    }

    @Provides
    @Inject
    public SceneParser getSceneParser(final Gson gson, final Vector3Factory vector3Factory) {
//        return new SceneParserCustomFormat(vector3Factory);
        return new SceneJsonParser(vector3Factory, gson);
    }

    @Provides
    public SceneRenderer getSceneRenderer() {
        return new SceneRendererImpl();
    }

    @Provides
    public ImagesToVideoConverter imagesToVideoConverter() {
        return new SimpleImagesToVideoConverter();
    }

    @Provides
    @Inject
    public Gson getGson(final Vector3Factory vector3Factory) {
        GsonBuilder gsonBuilder = new GsonBuilder();

        JsonDeserializer<Vector3> vector3Deserializer = new CustomVector3Deserializers(vector3Factory);
        JsonDeserializer<Shape> shapeJsonDeserializer = new ShapeDeserializer(vector3Factory);
        JsonDeserializer<SuperShape> superShapeJsonDeserializer = new SuperShapeDeserializer(vector3Factory);

        gsonBuilder.registerTypeAdapter(Vector3.class, vector3Deserializer);
        gsonBuilder.registerTypeAdapter(Shape.class, shapeJsonDeserializer);
        gsonBuilder.registerTypeAdapter(SuperShape.class, superShapeJsonDeserializer);

        return gsonBuilder.create();

    }
}