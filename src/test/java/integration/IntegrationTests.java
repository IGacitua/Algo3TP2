package integration;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.cell.Building;
import game.map.creation.CellFactory;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Team;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.movement.Grounded;
import game.units.movement.Movement;
import game.units.weapons.Artillery;
import game.units.weapons.Weapon;
import game.utils.FileHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

// Probablemente lo separemos en varias clases de tests de integración, pero por ahora le dejamos el nombre.
public class IntegrationTests {

    /*
     * Unit de Artillería dispara a un Vek que está al lado de un edificio:
     *   Daño en la casilla objetivo.
     *   Empuje de las unidades adyacentes.
     *   Choque contra el edificio.
     *   Descuento de la Red Eléctrica.
     */
    // TODO: Rehacer, algo raro hay con las posiciones de los vek!
    @Test
    public void TestArtilleryShootsVek() throws InvalidMapFileException, PositionOutOfBoundsException {
        // Arrange: Map & Network
        ElectricalNetwork electricalNetwork = new ElectricalNetwork(5);
        MapImporter importer = new MapImporter(FileHandler.getInstance(), new CellFactory(electricalNetwork));
        GameMap map = new GameMap(importer, "src/test/resources/testMap2.txt");

        // Arrange: Positions
        Position mechaPosition = new Position(2, 4);               // South of target ('Y' grows downward)
        Position vekCentroPosition = new Position(2, 2);           // Target
        Position vekNortePosition = new Position(2, 1);            // North of target
        Position northBuildingPosition = new Position(2, 0);       // Building in testMap2.txt
        Position vekEstePosition = new Position(3, 2);             // East of target
        Position vekEsteExpectedPosition = new Position(4, 2);     // Where the east unit should end up

        // Arrange: Units
        Weapon artillery = new Artillery(map);
        Unit mecha = new Unit(Team.Player, artillery, 5, new Grounded(3));
        Unit vekCentro = new Unit(Team.Enemy, null, 5, new Grounded(2));
        Unit vekNorte = new Unit(Team.Enemy, null, 5, new Grounded(2));
        Unit vekEste = new Unit(Team.Enemy, null, 5, new Grounded(2));

        map.tryPlaceUnit(mecha, mechaPosition);
        map.tryPlaceUnit(vekCentro, vekCentroPosition);
        map.tryPlaceUnit(vekNorte, vekNortePosition);
        map.tryPlaceUnit(vekEste, vekEstePosition);

        // Act
        UnitAction action = mecha.getAttackAction(mechaPosition, vekCentroPosition);
        action.tryExecute(map);

        // Assert
        Assertions.assertTrue(vekCentro.isDamaged(), "Expected the unit at the target to be damaged after the attack.");
        Assertions.assertTrue(map.hasUnitAt(vekCentroPosition), "Expected the center unit NOT to have moved.");

        Assertions.assertTrue(map.hasUnitAt(vekNortePosition), "Expected the north unit to stay in its position after hitting the building.");
        Assertions.assertFalse(map.hasUnitAt(northBuildingPosition), "Expected the north unit NOT to enter the intact building.");
        Assertions.assertTrue(vekNorte.isDamaged(), "Expected the north unit to take collision damage.");

        Assertions.assertTrue(map.hasUnitAt(vekEsteExpectedPosition), "Expected the east unit to be pushed east.");
        Assertions.assertFalse(map.hasUnitAt(vekEstePosition), "Expected the east unit to have left its original position.");

        Assertions.assertTrue(((Building) map.getCellAt(northBuildingPosition)).isDestroyed());
        Assertions.assertEquals(4, electricalNetwork.getPower());
    }
}