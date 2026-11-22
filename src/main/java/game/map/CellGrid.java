package game.map;

import game.map.cell.Cell;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;

public class CellGrid {

    private final Cell[][] matrix; // Indexed [x][y]

    public CellGrid(Cell[][] cells) {
        this.matrix = cells;
    }

    public void replaceCell(Cell cell, Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        replaceCell(cell, x, y);
    }

    private void replaceCell(Cell cell, int x, int y) {
        if (x < 0 || x >= matrix.length) {
            throw new IndexOutOfBoundsException("X index out of bounds: " + x);
        }
        if (y < 0 || y >= matrix[x].length) {
            throw new IndexOutOfBoundsException("Y index out of bounds: " + y);
        }

        matrix[x][y] = cell;
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
        List<Position> result = new ArrayList<>();

        int cx = center.getX();
        int cy = center.getY();
        if (cx < 0 || cx >= matrix.length || cy < 0 || cy >= matrix[cx].length) {
            return result;
        }

        boolean[][] visited = new boolean[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            visited[i] = new boolean[matrix[i].length];
        }

        Deque<Position> queue = new ArrayDeque<>();
        queue.add(center);
        visited[cx][cy] = true;

        OrthogonalDirection[] directions = {new North(), new South(), new East(), new West()};

        while (!queue.isEmpty()) {
            Position current = queue.poll();
            for (OrthogonalDirection direction : directions) {
                Position displaced = current.getAdjacent(direction);
                int x = displaced.getX();
                int y = displaced.getY();

                if (x < 0 || x >= matrix.length || y < 0 || y >= matrix[x].length) {
                    continue;
                }

                if (visited[x][y]) {
                    continue;
                }
                Position next = new Position(x, y);
                if (next.getDiagonalDistance(center) <= radius && requirement.test(matrix[x][y])) {
                    visited[x][y] = true;
                    result.add(next);
                    queue.add(next);
                }
            }
        }
        return result;
    }

    public Position getNearestCell(Position center, Predicate<Cell> requirement) {
        Position nearest = null;
        double best = Double.POSITIVE_INFINITY;
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                if (requirement.test(matrix[x][y])) {
                    double d = new Position(x, y).getDiagonalDistance(center);
                    if (d < best) {
                        best = d;
                        nearest = new Position(x, y);
                    }
                }
            }
        }
        return nearest;
    }

    public double getDistanceToNearestCell(Position center, Predicate<Cell> requirement) {
        Position nearest = getNearestCell(center, requirement);
        if (nearest == null) {
            return Double.POSITIVE_INFINITY;
        }
        return nearest.getOrthogonalDistance(center);
    }
}