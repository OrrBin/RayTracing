package raytracing.geometry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class SuperShape {

    protected SuperShape parent;
    protected double[][] bounds;

    public abstract void calculateBoundingBox();
    public abstract Collection<SuperShape> getSuperShapeChildren();
    public abstract Collection<? extends Shape> getShapeChildren();
}
