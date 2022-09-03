package raytracing.actors.graph;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class GraphNode<T> {
    protected final T value;
    protected final List<GraphNode<?>> children = new ArrayList<>();

    public void addChild(GraphNode<?> shapeNode) {
        this.children.add(shapeNode);
    }

    public void addChildren(Collection<GraphNode<?>> shapeNodes) {
        this.children.addAll(shapeNodes);
    }


}
