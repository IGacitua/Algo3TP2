package integration;

import game.map.GameMap;
import game.map.Position2D;
import game.map.cell.Acid;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.units.Unit;
import game.units.movement.FlyingMovement;
import game.map.direction.East;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcidIntegrationTest {

    @BeforeEach
    public void setUp() throws InvalidMapFileException {
        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = new Cell[1][1];
        cells[0][0] = new Acid(new Position2D(0, 0));

        Mockito.when(importer.importMap(Mockito.anyString()))
                .thenReturn(cells);

        GameMap.getInstance().initialize(importer, "");
    }

    @Test
    public void testGroundedUnitOnAcidReceivesDoubleDamage() {
        Unit unit = new Unit(
                new Position2D(0, 0),
                null,
                10
        );

        unit.damage(2);

        assertEquals(6, unit.getCurrentHealth());
    }

    @Test
    public void testFlyingUnitOnAcidReceivesNormalDamage() {
        Unit unit = new Unit(
                new Position2D(0, 0),
                null,
                10,
                new FlyingMovement()
        );

        unit.damage(2);

        assertEquals(8, unit.getCurrentHealth());
    }

    @Test
    public void testGroundedUnitPushedIntoAcidReceivesDoubleDamage()
            throws Exception {

        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = new Cell[2][1];
        cells[0][0] = new Cell(new Position2D(0, 0));
        cells[1][0] = new Acid(new Position2D(1, 0));

        Mockito.when(importer.importMap(Mockito.anyString()))
                .thenReturn(cells);

        GameMap.getInstance().initialize(importer, "");

        Unit unit = new Unit(
                new Position2D(0, 0),
                null,
                10
        );

        GameMap.getInstance().pushUnit(
                new Position2D(0, 0),
                new East()
        );

        unit.damage(2);

        assertEquals(6, unit.getCurrentHealth());
        assertEquals(1, unit.getPosition().getX());
        assertEquals(0, unit.getPosition().getY());
    }

    @Test
    public void testFlyingUnitPushedIntoAcidReceivesNormalDamage()
            throws Exception {

        MapImporter importer = Mockito.mock(MapImporter.class);

        Cell[][] cells = new Cell[2][1];
        cells[0][0] = new Cell(new Position2D(0, 0));
        cells[1][0] = new Acid(new Position2D(1, 0));

        Mockito.when(importer.importMap(Mockito.anyString()))
                .thenReturn(cells);

        GameMap.getInstance().initialize(importer, "");

        Unit unit = new Unit(
                new Position2D(0, 0),
                null,
                10,
                new FlyingMovement()
        );

        GameMap.getInstance().pushUnit(
                new Position2D(0, 0),
                new East()
        );

        unit.damage(2);

        assertEquals(8, unit.getCurrentHealth());
        assertEquals(1, unit.getPosition().getX());
        assertEquals(0, unit.getPosition().getY());
    }
}
