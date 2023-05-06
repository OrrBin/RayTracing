package raytracing.modules;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import raytracing.animation.StageManager;
import raytracing.geometry.Shape;
import raytracing.geometry.SuperShape;
import raytracing.math.SimpleVector3Factory;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;
import raytracing.parsing.SceneParser;
import raytracing.parsing.serialization.json.CustomVector3Deserializers;
import raytracing.parsing.serialization.json.SceneParserCustomFormat;
import raytracing.parsing.serialization.json.ShapeDeserializer;
import raytracing.parsing.serialization.json.SuperShapeDeserializer;
import raytracing.projection.EquirectangularProjection;
import raytracing.projection.Projection;
import raytracing.projection.SphericalMercatorProjection;
import raytracing.rendering.SceneRenderer;
import raytracing.rendering.SceneRendererImpl;
import raytracing.util.ImageUtils;
import raytracing.video.ImagesToVideoConverter;
import raytracing.video.SimpleImagesToVideoConverter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

public class RayTracingModule extends AbstractModule {

    protected void configure() {

    }

    @Singleton
    public StageManager getStageManager() {
        return new StageManager();
    }

    @Provides
    public Vector3Factory getVector3Factory() {
        return new SimpleVector3Factory();
    }

    @Provides
    @Inject
    public SceneParser getSceneParser(final StageManager stageManager, final Gson gson,
                                      final ImageUtils imageUtils,
                                      final Vector3Factory vector3Factory,
                                      final Map<String, Projection> projectionMap) {
        return new SceneParserCustomFormat(vector3Factory, stageManager, imageUtils, projectionMap);
//        return new SceneJsonParser(stageManager, vector3Factory, gson);
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

    @Provides
    @Inject
    public ImageUtils getImageUtils(final Vector3Factory vector3Factory) {
        return new ImageUtils(vector3Factory);
    }

    @Provides
    @Inject
    public Map<String, Projection> getProjectionsByName(
            final SphericalMercatorProjection sphericalMercatorProjection,
            final EquirectangularProjection equirectangularProjection
    ) {
        return ImmutableMap.of(
                sphericalMercatorProjection.getName(), sphericalMercatorProjection,
                equirectangularProjection.getName(), equirectangularProjection);

    }
}