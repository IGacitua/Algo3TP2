package game.map;

import game.map.cell.Cell;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.*;
import game.utils.graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CellGrid {

    private static final OrthogonalDirection[] DIRECTIONS = {new North(), new South(), new East(), new West()};

    private final Cell[][] matrix; // Indexed [x][y]
    private final Graph<Position> graph = new Graph<>();

    public CellGrid(Cell[][] cells) {
        this.matrix = cells;

        // Create Nodes
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                graph.addNode(new Position(x, y));
            }
        }

        // Link Nodes
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                Position pos = new Position(x, y);
                for (OrthogonalDirection direction : DIRECTIONS) {
                    Position adj = pos.getAdjacent(direction);
                    if (graph.contains(adj)) {
                        graph.link(pos, adj);
                    }
                }
            }
        }
    }

    public Cell getCell(Position position) throws PositionOutOfBoundsException {
        int x = position.getX();
        int y = position.getY();
        return getCell(x, y);
    }

    public Cell getCell(int x, int y) throws PositionOutOfBoundsException {
        if (x < 0 || x >= matrix.length) {
            throw new PositionOutOfBoundsException(x, y);
        }
        if (y < 0 || y >= matrix[x].length) {
            throw new PositionOutOfBoundsException(x, y);
        }
        return matrix[x][y];
    }

    public List<Position> getCellsInRadius(Position center, double radius, Predicate<Cell> requirement) {
        if (!graph.contains(center)) {
            return new ArrayList<>();
        }
        return graph.getReachable(center, position -> {
            if (position.getDiagonalDistance(center) > radius) {
                return false;
            }
            return requirement.test(getCell(position));
        });
    }

    public Position getNearestCell(Position center, Predicate<Cell> requirement) {
        if (!graph.contains(center)) {
            return null;
        }
        return graph.findNearest(center, p -> requirement.test(getCell(p)))
                .map(Graph.SearchResult::getValue)
                .orElse(center); // TODO: Ver requerimientos de uso de este mensaje. Quizá sea ideal retornar null, o Inf,Inf, o lanzar excepción.
    }

    public double getDistanceToNearestCell(Position center, Predicate<Cell> requirement) {
        if (!graph.contains(center)) {
            return Double.POSITIVE_INFINITY;
        }
        return graph.findNearest(center, p -> requirement.test(getCell(p)))
                .map(r -> (double) r.getDistance())
                .orElse(Double.POSITIVE_INFINITY);
    }

    public List<Position> getPath(Position center, Position destination, Predicate<Cell> requirement) {
        if (!graph.contains(center) || !graph.contains(destination)) {
            throw new NoPathException(center, destination);
        }
        return graph.getPath(center, destination,
                position -> requirement.test(getCell(position))).orElseThrow(
                () -> new NoPathException(center, destination));
    }
}