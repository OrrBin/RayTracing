package raytracing.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import raytracing.parsing.SceneParser;
import raytracing.parsing.SceneParserCustomFormat;
import raytracing.rendering.SceneRenderer;
import raytracing.rendering.SceneRendererImpl;

public class RayTracingModule extends AbstractModule {

    protected void configure() {

    }

    @Provides
    public SceneParser getSceneParser() {
        return new SceneParserCustomFormat();
    }

    @Provides
    public SceneRenderer getSceneRenderer() {
        return new SceneRendererImpl();
    }
}