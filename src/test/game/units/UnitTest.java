package game.units;

import game.map.GameMap;
import game.map.Position2D;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTest {

    @Test
    public void testDamageReducesUnitHealth()
            throws InvalidMapFileException {

        // Arrange: prepared a 1x1 map
        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = new Cell[1][1];
        cells[0][0] = new Cell(new Position2D(0, 0));

        Mockito.when(importer.importMap(Mockito.anyString()))
                .thenReturn(cells);

        GameMap.getInstance().initialize(importer, "");

        // Arrange: creates the unit
        Unit unit = new Unit(new Position2D(0, 0), null);

        // Act: received damaged
        unit.damage();

        // Assert: check the hp remaining
        assertEquals(2, unit.getCurrentHealth());
    }
}