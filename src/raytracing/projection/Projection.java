package raytracing.projection;

public interface Projection {

    String getName();
    int lon2x(final double lon, final int mapWidth);
    int lat2y(final double lat, final int mapHeight);
}
