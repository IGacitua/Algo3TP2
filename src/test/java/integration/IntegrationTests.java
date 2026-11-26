package integration;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.cell.Building;
import game.map.creation.CellFactory;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Mech;
import game.units.Unit;
import game.units.Vek;
import game.units.actions.UnitAction;
import game.units.movement.Movement;
import game.units.weapons.Artillery;
import game.units.weapons.Weapon;
import game.utils.FileHandler;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

// Probablemente lo separemos en varias clases de tests de integración, pero por ahora le dejamos el nombre.
public class IntegrationTests {

    /*
     * Mech de Artillería dispara a un Vek que está al lado de un edificio:
     *   Daño en la casilla objetivo.
     *   Empuje de las unidades adyacentes.
     *   Choque contra el edificio.
     *   Descuento de la Red Eléctrica.
     */
    @Test
    public void TestArtilleryShootsVek() throws InvalidMapFileException, PositionOutOfBoundsException {
        // Arrange: Map & Network
        ElectricalNetwork electricalNetwork = new ElectricalNetwork(5);
        MapImporter importer = new MapImporter(FileHandler.getInstance(), new CellFactory(electricalNetwork));
        GameMap map = new GameMap(importer, "src/test/resources/testMap2.txt");

        final int targetX = 2;
        final int targetY = 2;

        // Arrange: Positions

        Position mechaPosition = new Position(targetX, targetY + 2);        // South of target ('Y' grows downward)
        Position vekCentroPosition = new Position(targetX, targetY);        // Target
        Position vekNortePosition = new Position(targetX, targetY - 2);     // North of target
        Position vekEstePosition = new Position(targetX + 1, targetY);      // East of target
        Position vekEsteExpectedPosition = new Position(targetX + 2, targetY); // Where the east unit should end up

        // Arrange: Units

        Weapon artillery = new Artillery(map);
        Unit mecha = new Mech(artillery, 5, Mockito.mock(Movement.class));
        Unit vekCentro = new Vek(null, 5, Mockito.mock(Movement.class));
        Unit vekNorte = new Vek(null, 5, Mockito.mock(Movement.class));
        Unit vekEste = new Vek(null, 5, Mockito.mock(Movement.class));

        map.tryPlaceUnit(mecha, mechaPosition);
        map.tryPlaceUnit(vekCentro, vekCentroPosition);
        map.tryPlaceUnit(vekNorte, vekNortePosition);
        map.tryPlaceUnit(vekEste, vekEstePosition);

        // Act

        UnitAction action = mecha.getAttackAction(mechaPosition, vekCentroPosition);
        action.tryExecute(map);

        // Assert

        Assert.assertTrue("Expected the unit at the target to be damaged after the attack.", vekCentro.isDamaged());

        Assert.assertTrue("Expected the center unit NOT to have moved.", map.hasUnitAt(vekCentroPosition));
        Assert.assertTrue("Expected the north unit to be pushed north.", map.hasUnitAt(vekNortePosition));
        Assert.assertTrue("Expected the east unit to be pushed east.", map.hasUnitAt(vekEsteExpectedPosition));
        Assert.assertFalse("Expected the east unit to have left its original position.", map.hasUnitAt(vekEstePosition));

        Assert.assertTrue(((Building) map.getCellAt(vekNortePosition)).isDestroyed());
        Assert.assertEquals(4, electricalNetwork.getPower());
    }
}