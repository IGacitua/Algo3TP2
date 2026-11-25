package game.utils.graph;

import java.util.*;
import java.util.function.Predicate;

/**
 * A directed graph keyed by value. Values must implement equals/hashCode
 * consistently, since they are the only handle callers have on a node.
 */
public class Graph<T> {

    private final Map<T, GraphNode<T>> nodes = new HashMap<>();

    /**
     * Adds a node holding the value.
     */
    public void addNode(T value) {
        if (nodes.containsKey(value)) {
            return;
        }

        nodes.put(value, new GraphNode<>(nodes.size(), value));
    }

    public boolean contains(T value) {
        return nodes.containsKey(value);
    }

    public int size() {
        return nodes.size();
    }

    /**
     * Adds a directed edge from -> to
     */
    public void link(T from, T to) {
        GraphNode<T> fromNode = getNode(from);
        GraphNode<T> toNode = getNode(to);

        fromNode.neighbors.add(toNode);
    }

    /**
     * Breadth-first traversal from start. A neighbor is only entered (and expanded)
     * if canEnter accepts its value. Returns entered values in visit order,
     * excluding start. The start value itself is never tested.
     */
    public List<T> getReachable(T start, Predicate<T> canEnter) {
        GraphNode<T> startNode = getNode(start);
        List<T> result = new ArrayList<>();
        boolean[] visited = new boolean[nodes.size()];
        Deque<GraphNode<T>> queue = new ArrayDeque<>();

        visited[startNode.id] = true;
        queue.add(startNode);

        while (!queue.isEmpty()) {
            GraphNode<T> current = queue.poll();
            for (GraphNode<T> next : current.neighbors) {
                if (visited[next.id]) continue;
                if (!canEnter.test(next.value)) continue;
                visited[next.id] = true;
                result.add(next.value);
                queue.add(next);
            }
        }
        return result;
    }

    /**
     * Breadth-first search for the closest value (in edges) satisfying target.
     * Start is tested too (distance 0). Stops at the first match, so only the
     * nearby part of the graph is touched. Empty if nothing matches.
     */
    public Optional<SearchResult<T>> findNearest(T start, Predicate<T> target) {
        GraphNode<T> startNode = getNode(start);
        int[] dist = new int[nodes.size()];
        Arrays.fill(dist, -1);
        Deque<GraphNode<T>> queue = new ArrayDeque<>();

        dist[startNode.id] = 0;
        queue.add(startNode);

        while (!queue.isEmpty()) {
            GraphNode<T> current = queue.poll();
            int d = dist[current.id];
            if (target.test(current.value)) {
                return Optional.of(new SearchResult<>(current.value, d));
            }
            for (GraphNode<T> next : current.neighbors) {
                if (dist[next.id] != -1) continue;
                dist[next.id] = d + 1;
                queue.add(next);
            }
        }
        return Optional.empty();
    }

    /**
     * Breadth-first search for a shortest path (in edges) from start to destination.
     * Only values accepted by canEnter may be stepped onto, destination included.
     * The start value is never tested. The returned list excludes start and ends
     * with destination; it is empty if start equals destination.
     * Returns empty if no path exists.
     */
    public Optional<List<T>> getPath(T start, T destination, Predicate<T> canEnter) {
        GraphNode<T> startNode = getNode(start);
        GraphNode<T> destinationNode = getNode(destination);

        if (startNode == destinationNode) {
            return Optional.of(new ArrayList<>());
        }

        // Doubles as the visited set. The start node maps to null.
        Map<GraphNode<T>, GraphNode<T>> previous = new HashMap<>();
        Deque<GraphNode<T>> queue = new ArrayDeque<>();

        previous.put(startNode, null);
        queue.add(startNode);

        while (!queue.isEmpty()) {
            GraphNode<T> current = queue.poll();
            for (GraphNode<T> next : current.neighbors) {
                if (previous.containsKey(next)) continue;
                if (!canEnter.test(next.value)) continue;
                previous.put(next, current);
                if (next == destinationNode) {
                    return Optional.of(buildPath(previous, destinationNode));
                }
                queue.add(next);
            }
        }
        return Optional.empty();
    }

    private List<T> buildPath(Map<GraphNode<T>, GraphNode<T>> previous, GraphNode<T> destination) {
        List<T> path = new ArrayList<>();
        // The start node's predecessor is null, so the loop stops before adding it.
        for (GraphNode<T> node = destination; previous.get(node) != null; node = previous.get(node)) {
            path.add(node.value);
        }
        Collections.reverse(path);
        return path;
    }

    private GraphNode<T> getNode(T value) {
        GraphNode<T> node = nodes.get(value);
        if (node == null) {
            throw new IllegalArgumentException("No node for value: " + value);
        }
        return node;
    }

    /**
     * A value found by a search, plus how many edges away it is from the start.
     */
    public static final class SearchResult<T> {
        private final T value;
        private final int distance;

        private SearchResult(T value, int distance) {
            this.value = value;
            this.distance = distance;
        }

        public T getValue() {
            return value;
        }

        public int getDistance() {
            return distance;
        }
    }
}