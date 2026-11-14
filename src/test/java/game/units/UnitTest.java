
package game.units;

import game.map.GameMap;
import game.map.Position2D;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTest {

    @BeforeEach
    public void setUp() throws InvalidMapFileException {
        // Preparar un mapa de 1x1 antes de cada test
        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = new Cell[1][1];
        cells[0][0] = new Cell(new Position2D(0, 0));

        Mockito.when(importer.importMap(Mockito.anyString()))
                .thenReturn(cells);

        GameMap.getInstance().initialize(importer, "");
    }

    @Test
    public void testDamageReducesUnitHealth() {
        // Arrange
        Unit unit = new Unit(new Position2D(0, 0), null);

        // Act
        unit.damage();

        // Assert
        assertEquals(2, unit.getCurrentHealth());
    }

    @Test
    public void testUnitCanHaveCustomMaximumHealth() {
        // Arrange
        Unit unit = new Unit(new Position2D(0, 0), null, 5);

        // Act
        unit.damage();

        // Assert
        assertEquals(4, unit.getCurrentHealth());
    }
}
