package raytracing.geometry;

import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public abstract class TriangulatedShape extends SuperShape {

    protected List<Triangle> triangles;

    @Override
    public Collection<? extends Shape> getShapeChildren() {
        return triangles;
    }

    @Override
    public Collection<SuperShape> getSuperShapeChildren() {
        return Collections.emptyList();
    }

    @Override
    public void calculateBoundingBox() {
        this.bounds = new double[][] {
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY }
        };

        for(Triangle triangle : triangles) {
            for(int i = 0; i < 3; i++) {
                double triangleMin = triangle.bounds[0][i];
                bounds[0][i] = Math.min(triangleMin, bounds[0][i]);
            }

            for(int i = 0; i < 3; i++) {
                double triangleMax = triangle.bounds[1][i];
                bounds[1][i] = Math.max(triangleMax, bounds[1][i]);
            }
        }


    }


}
