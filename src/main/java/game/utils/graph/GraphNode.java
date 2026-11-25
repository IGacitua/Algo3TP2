package game.utils.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GraphNode<T> {

    final int id; // used for visited tracking
    final T value;
    final List<GraphNode<T>> neighbors = new ArrayList<>(4);

    public GraphNode(int id, T value) {
        this.id = id;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public List<GraphNode<T>> getNeighbors() {
        return Collections.unmodifiableList(neighbors);
    }

    public int getId() {
        return id;
    }

    public void addNeighbor(GraphNode<T> neighbor) {
        neighbors.add(neighbor);
    }
}