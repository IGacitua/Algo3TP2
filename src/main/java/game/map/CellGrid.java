package game.map;

import game.map.cell.Cell;

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

    public Cell getCell(Position2D position) {
        int row = position.getX();
        int col = position.getY();
        return getCell(row, col);
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= matrix.length) {
            throw new IndexOutOfBoundsException("Row index out of bounds: " + row);
        }
        if (col < 0 || col >= matrix[row].length) {
            throw new IndexOutOfBoundsException("Column index out of bounds: " + col);
        }
        return matrix[row][col];
    }
}

