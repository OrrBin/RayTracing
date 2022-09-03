package raytracing.actors.graph;

import raytracing.geometry.Shape;

public class ShapeNode extends GraphNode<Shape> {

    public ShapeNode(final Shape value) {
        super(value);
    }

}
