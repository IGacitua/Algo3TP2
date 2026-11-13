package integration;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.Position2D;
import game.map.PositionOutOfBoundsException;
import game.map.cell.Building;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.weapons.Artillery;
import game.units.weapons.Weapon;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// Probablemente lo separemos en varias clases de tests de integración, pero por ahora le dejamos el nombre.
public class IntegrationTests {

    public static final double DELTA = 0.01;

    /*
     * Mech de Artillería dispara a un Vek que está al lado de un edificio:
     *   Daño en la casilla objetivo.
     *   Empuje de las unidades adyacentes.
     *   Choque contra el edificio.
     *   Descuento de la Red Eléctrica.
     */
    @Test
    public void TestArtilleryShootsVek() throws InvalidMapFileException, PositionOutOfBoundsException {
        // Arrange: Network

        ElectricalNetwork.getInstance().reset();

        // Arrange: Map (5x5, (0,0) is top-left, Position2D is (x, y))

        MapImporter mockMapImporter = Mockito.mock(MapImporter.class);
        Cell[][] mockCells = new Cell[5][5];
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                mockCells[x][y] = new Cell(new Position2D(x, y));
            }
        }
        Mockito.when(mockMapImporter.importMap(Mockito.any(String.class))).thenReturn(mockCells);

        final int targetX = 2;
        final int targetY = 2;
        final Position2D buildingPosition = new Position2D(targetX, targetY - 2); // where the north unit lands

        GameMap map = GameMap.getInstance();
        map.initialize(mockMapImporter, "");
        map.placeBuilding(buildingPosition);

        // Arrange: Units

        Weapon artillery = new Artillery();
        Unit mecha = new Unit(new Position2D(targetX, targetY + 2), artillery);   // South of target (y grows downward)

        Unit vekCentro = new Unit(new Position2D(targetX, targetY), null);        // Target
        Unit vekNorte = new Unit(new Position2D(targetX, targetY - 1), null);    // North of target
        Unit vekEste = new Unit(new Position2D(targetX + 1, targetY), null);    // East of target

        // Act

        UnitAction action = mecha.getAttack(vekCentro.getPosition());
        action.execute();

        // Assert

        assertTrue("Expected the unit at the target to be damaged after the attack.", vekCentro.isDamaged());

        assertEquals("Expected the center unit NOT to have moved.", new Position2D(targetX, targetY), vekCentro.getPosition());
        assertEquals("Expected the north unit to be pushed north.", new Position2D(targetX, targetY - 2), vekNorte.getPosition());
        assertEquals("Expected the east unit to be pushed east.", new Position2D(targetX + 2, targetY), vekEste.getPosition());

        assertTrue(((Building) map.getCell(buildingPosition)).isDestroyed());
        assertEquals(4, ElectricalNetwork.getInstance().getPower());
    }
}
