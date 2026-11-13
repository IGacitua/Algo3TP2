package integration;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.Position2D;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.weapons.Artillery;
import game.units.weapons.Cannon;
import game.units.weapons.Punch;
import game.units.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class SquadWeaponsIntegrationTest {

    @BeforeEach
    public void setUp() throws InvalidMapFileException {
        ElectricalNetwork.getInstance().reset();

        MapImporter mockMapImporter = Mockito.mock(MapImporter.class);
        Cell[][] mockCells = new Cell[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                mockCells[x][y] = new Cell(new Position2D(x, y));
            }
        }
        Mockito.when(mockMapImporter.importMap(Mockito.any(String.class))).thenReturn(mockCells);

        GameMap.getInstance().Initialize(mockMapImporter, "");
    }

    /*
     * Verificar que el Puño cause 2 de daño a una unidad adyacente
     * y la empuje 1 casilla en la dirección del golpe.
     */
    @Test
    public void punchDealsTwoDamageToAdjacentUnitAndPushesItOneTileInHitDirection() {
        // Arrange
        Weapon punch = new Punch();
        Position2D mechPosition = new Position2D(2, 2);
        Position2D targetPosition = new Position2D(2, 3);
        Position2D pushedPosition = new Position2D(2, 4);

        Unit combatMech = new Unit(mechPosition, punch);
        Unit targetVek = new Unit(targetPosition, null);

        UnitAction punchAttack = combatMech.getAttack(targetPosition);

        // Act
        punchAttack.execute();

        // Assert
        assertTrue(targetVek.isDamaged(), "Target Vek should take damage from Punch");
        assertEquals(1, targetVek.getCurrentHealth(), "Target Vek health should decrease by 2 points");
        assertEquals(pushedPosition, targetVek.getPosition(), "Target Vek should be pushed 1 tile in the hit direction");
    }

    /*
     * Verificar que el Cañón impacte a la primera unidad u obstáculo en línea recta
     * y no a las que están detrás.
     */
    @Test
    public void cannonHitsFirstUnitOrObstacleInStraightLineAndDoesNotHitBehind() {
        // Arrange
        Weapon cannon = new Cannon();
        Position2D mechPosition = new Position2D(1, 1);
        Position2D firstVekPosition = new Position2D(1, 3);
        Position2D secondVekPosition = new Position2D(1, 5);
        Position2D aimPosition = new Position2D(1, 6);

        Unit cannonMech = new Unit(mechPosition, cannon);
        Unit firstVek = new Unit(firstVekPosition, null);
        Unit secondVekBehind = new Unit(secondVekPosition, null);

        UnitAction cannonAttack = cannonMech.getAttack(aimPosition);

        // Act
        cannonAttack.execute();

        // Assert
        assertTrue(firstVek.isDamaged(), "First unit in line of sight should be hit by Cannon");
        assertEquals(2, firstVek.getCurrentHealth(), "First unit should receive 1 damage from Cannon");
        assertFalse(secondVekBehind.isDamaged(), "Unit behind the first target should not take damage");
        assertEquals(3, secondVekBehind.getCurrentHealth(), "Unit behind should keep its full health");
    }

    /*
     * Verificar que la Artillería pueda atacar por encima de obstáculos,
     * que no pueda atacar casillas adyacentes y que dañe la casilla objetivo
     * y empuje hacia afuera a las unidades de las 4 casillas adyacentes.
     */
    @Test
    public void artilleryAttacksOverObstaclesCannotTargetAdjacentTilesAndPushesFourAdjacentUnits() {
        // Arrange
        Weapon artillery = new Artillery();
        Position2D mechPosition = new Position2D(0, 3);
        Position2D adjacentObstaclePosition = new Position2D(1, 3);
        Position2D targetPosition = new Position2D(4, 3);

        GameMap.getInstance().placeBuilding(adjacentObstaclePosition);

        Unit artilleryMech = new Unit(mechPosition, artillery);
        Unit centerVek = new Unit(targetPosition, null);
        Unit northVek = new Unit(new Position2D(4, 2), null);
        Unit southVek = new Unit(new Position2D(4, 4), null);
        Unit eastVek = new Unit(new Position2D(5, 3), null);
        Unit westVek = new Unit(new Position2D(3, 3), null);

        UnitAction adjacentAttack = artilleryMech.getAttack(adjacentObstaclePosition);
        UnitAction validArtilleryAttack = artilleryMech.getAttack(targetPosition);

        // Act
        validArtilleryAttack.execute();

        // Assert
        assertThrows(RuntimeException.class, adjacentAttack::execute, "Artillery should not be able to target adjacent tiles");
        assertTrue(centerVek.isDamaged(), "Unit on target cell should take damage over obstacle");
        assertEquals(2, centerVek.getCurrentHealth(), "Unit on target cell should receive 1 damage over obstacle");
        assertEquals(new Position2D(4, 1), northVek.getPosition(), "North unit should be pushed to (4, 1)");
        assertEquals(new Position2D(4, 5), southVek.getPosition(), "South unit should be pushed to (4, 5)");
        assertEquals(new Position2D(6, 3), eastVek.getPosition(), "East unit should be pushed to (6, 3)");
        assertEquals(new Position2D(2, 3), westVek.getPosition(), "West unit should be pushed to (2, 3)");
    }
}