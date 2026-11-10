package integration;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.Position2D;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.weapons.Artillery;
import game.units.weapons.Weapon;
import org.junit.Test;

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
    public void TestArtilleryShootsVek() {
        // Arrange
        Weapon artillery = new Artillery();
        Unit mecha = new Unit(new Position2D(-10, -10), artillery);

        Unit vekCentro = new Unit(new Position2D(0, 0), null);
        Unit vekNorte = new Unit(new Position2D(0, 1), null);
        Unit vekEste = new Unit(new Position2D(1, 0), null);

        GameMap map = GameMap.getInstance();

        map.placeBuilding(new Position2D(0, 2));

        // Act

        UnitAction action = mecha.getAttack(vekCentro.getPosition());
        action.execute();

        // Assert
        assertTrue("Expected the cell at (0,0) to be damaged after the attack.", GameMap.getInstance().isDamaged(vekCentro.getPosition()));
        assertTrue("Expected the unit at (0,0) to be damaged after the attack.", vekCentro.isDamaged());

        double distanceCentro = vekCentro.getPosition().getDistance(0, 0);
        double distanceNorte = vekNorte.getPosition().getDistance(0, 2);
        double distanceEste = vekEste.getPosition().getDistance(2, 0);
        assertEquals("Expected the unit at (0,0) to NOT have moved after the attack.", 0, distanceCentro, DELTA);
        assertEquals("Expected the unit at (0,1) to have moved after the attack.", 0, distanceNorte, DELTA);
        assertEquals("Expected the unit at (1,0) to have moved after the attack.", 0, distanceEste, DELTA);

        assertTrue(map.getCell(new Position2D(0, 2)).isDamaged());
        assertEquals(4, ElectricalNetwork.getInstance().getPower());
    }
}
