package raytracing.projection;

import static raytracing.util.MathUtils.bound;

public class SphericalMercatorProjection implements Projection {

    private static final String PROJECTION_NAME = "spherical-mercator";

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

        double latRad = Math.toRadians(bound(lat, -85, 85));
        double mercN = Math.log(Math.tan((Math.PI / 4) + (latRad / 2)));
        return (int) ((mapHeight / 2) - (mapHeight * mercN / (2 * Math.PI)) );
    }
}