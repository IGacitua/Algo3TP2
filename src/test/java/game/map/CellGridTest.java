package game.map;

import game.map.cell.Cell;
import game.position.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

// TODO!!!
public class CellGridTest {

    private Cell[][] generateGeneric(int rows, int cols) {
        Cell[][] cells = new Cell[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                cells[x][y] = new Cell(new Position(x, y));
            }
        }
        return cells;
    }

    @Test
    public void testCreationThrowsNothing() {
        // Arrange
        Cell[][] matrix = generateGeneric(5, 5);

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> new CellGrid(matrix));
    }
}
