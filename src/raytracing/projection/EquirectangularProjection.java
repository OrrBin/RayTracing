package raytracing.projection;

public class EquirectangularProjection implements Projection {

    private static final String PROJECTION_NAME = "equirectangular";

    @Override
    public String getName() {
        return PROJECTION_NAME;
    }

    @Override
    public int lon2x(final double lon, final int mapWidth) {
        final double pixelsPerLonDegree = mapWidth / 360.0;
        return (int) ((mapWidth/2.0) - (lon  * pixelsPerLonDegree));
    }

    @Override
    public int lat2y(final double lat, final int mapHeight) {
        final double pixelsPerLatDegree = mapHeight / 180.0;
        return (int) ((mapHeight / 2) - (lat  * pixelsPerLatDegree));
    }
}
