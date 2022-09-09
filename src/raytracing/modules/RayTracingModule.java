package raytracing.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import raytracing.math.SimpleVector3Factory;
import raytracing.math.Vector3Factory;
import raytracing.parsing.SceneParser;
import raytracing.parsing.SceneParserCustomFormat;
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
    public SceneParser getSceneParser(final Vector3Factory vector3Factory) {
        return new SceneParserCustomFormat(vector3Factory);
    }

    @Provides
    public SceneRenderer getSceneRenderer() {
        return new SceneRendererImpl();
    }

    @Provides
    public ImagesToVideoConverter imagesToVideoConverter() {
        return new SimpleImagesToVideoConverter();
    }
}