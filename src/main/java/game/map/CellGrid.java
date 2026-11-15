package game.map;

import game.map.cell.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CellGrid {

    private final Cell[][] matrix;

    public CellGrid(Cell[][] cells) {
        this.matrix = cells;
    }

    public void replaceCell(Cell cell, Position2D pos) {
        int row = pos.getX();
        int col = pos.getY();
        replaceCell(cell, row, col);
    }

    private void replaceCell(Cell cell, int row, int col) {
        if (row < 0 || row >= matrix.length) {
            throw new IndexOutOfBoundsException("Row index out of bounds: " + row);
        }
        if (col < 0 || col >= matrix[row].length) {
            throw new IndexOutOfBoundsException("Column index out of bounds: " + col);
        }
        matrix[row][col] = cell;
    }

    public Cell getCell(Position2D position) throws PositionOutOfBoundsException {
        int row = position.getX();
        int col = position.getY();
        return getCell(row, col);
    }

    public Cell getCell(int row, int col) throws PositionOutOfBoundsException {
        if (row < 0 || row >= matrix.length) {
            throw new PositionOutOfBoundsException(row, col);
        }
        if (col < 0 || col >= matrix[row].length) {
            throw new PositionOutOfBoundsException(row, col);
        }
        return matrix[row][col];
    }

    public List<Position2D> getCellsInRadius(Position2D center, double radius, Predicate<Cell> requirement) {
        List<Position2D> result = new ArrayList<>();
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                if (new Position2D(row, col).getDistance(center) <= radius
                        && requirement.test(matrix[row][col])) {
                    result.add(new Position2D(row, col));
                }
            }
        }
        return result;
    }

    public Position2D getNearestCell(Position2D center, Predicate<Cell> requirement) {
        Position2D nearest = null;
        double best = Double.POSITIVE_INFINITY;
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                if (requirement.test(matrix[row][col])) {
                    double d = new Position2D(row, col).getDistance(center);
                    if (d < best) {
                        best = d;
                        nearest = new Position2D(row, col);
                    }
                }
            }
        }
        return nearest;
    }

    public double getDistanceToNearestCell(Position2D center, Predicate<Cell> requirement) {
        Position2D nearest = getNearestCell(center, requirement);
        if (nearest == null) {
            return Double.POSITIVE_INFINITY;
        }
        return new Position2D(nearest.getX(), nearest.getY()).getDistance(center);
    }
}

