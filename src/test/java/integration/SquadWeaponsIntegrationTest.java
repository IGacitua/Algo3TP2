package integration;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.cell.Building;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Team;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.movement.Grounded;
import game.units.weapons.Artillery;
import game.units.weapons.Cannon;
import game.units.weapons.Punch;
import game.units.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class SquadWeaponsIntegrationTest {

    private GameMap gameMap;

    @BeforeEach
    public void setUp() throws InvalidMapFileException {
        Cell[][] mockCells = new Cell[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                mockCells[x][y] = new Cell(new Position(x, y));
            }
        }
        mockCells[1][3] = new Building(new Position(1, 3), Mockito.mock(ElectricalNetwork.class)); // Adjacent obstacle for artillery test

        MapImporter mockMapImporter = Mockito.mock(MapImporter.class);
        Mockito.when(mockMapImporter.importMap(Mockito.any(GameMap.class), Mockito.any(String.class))).thenReturn(mockCells);

        gameMap = new GameMap(mockMapImporter, "");
    }

    /*
     * Verificar que el Puño cause 2 de daño a una unidad adyacente
     * y la empuje 1 casilla en la dirección del golpe.
     */
    @Test
    public void punchDealsTwoDamageToAdjacentUnitAndPushesItOneTileInHitDirection() throws PositionOutOfBoundsException {
        // Arrange
        Weapon punch = new Punch(gameMap);
        Position mechPosition = new Position(2, 2);
        Position targetPosition = new Position(2, 3);
        Position pushedPosition = new Position(2, 4);

        Unit combatMech = new Unit(Team.Player, punch, 3, new Grounded(3));
        Unit targetVek = new Unit(Team.Player, null, 3, new Grounded(3));
        gameMap.tryPlaceUnit(combatMech, mechPosition);
        gameMap.tryPlaceUnit(targetVek, targetPosition);

        UnitAction punchAttack = combatMech.getAttackAction(mechPosition, targetPosition);

        // Act
        boolean hit = punchAttack.tryExecute(gameMap);

        // Assert
        assertTrue(hit, "Punch attack on valid adjacent target should return true");
        assertTrue(targetVek.isDamaged(), "Target Vek should take damage from Punch");
        assertEquals(1, targetVek.getHealth(), "Target Vek health should decrease by 2 points");
        assertTrue(gameMap.hasUnitAt(pushedPosition), "Target Vek should be pushed 1 tile in the hit direction");
        assertFalse(gameMap.hasUnitAt(targetPosition), "Target Vek should have left its original position");
    }

    /*
     * Verificar que el Cañón impacte a la primera unidad u obstáculo en línea recta
     * y no a las que están detrás.
     */
    @Test
    public void cannonHitsFirstUnitOrObstacleInStraightLineAndDoesNotHitBehind() throws PositionOutOfBoundsException {
        // Arrange
        Weapon cannon = new Cannon(gameMap);

        Position mechPosition = new Position(2, 1);
        Position firstVekPosition = new Position(2, 3);
        Position secondVekPosition = new Position(2, 5);

        Position aimPosition = new Position(2, 6);

        Unit mech = new Unit(Team.Player, cannon, 3, new Grounded(3));
        Unit firstVek = new Unit(Team.Player, null, 3, new Grounded(3));
        Unit secondVek = new Unit(Team.Player, null, 3, new Grounded(3));

        assertTrue(gameMap.tryPlaceUnit(mech, mechPosition), "Should have been able to place cannon mech.");
        assertTrue(gameMap.tryPlaceUnit(firstVek, firstVekPosition), "Should have been able to place first vek.");
        assertTrue(gameMap.tryPlaceUnit(secondVek, secondVekPosition), "Should have been able to place second vek behind first.");

        UnitAction cannonAttack = mech.getAttackAction(mechPosition, aimPosition);

        // Act
        boolean hit = cannonAttack.tryExecute(gameMap);

        // Assert
        assertTrue(hit, "Cannon should return true when hitting the first unit in line of sight");
        assertTrue(firstVek.isDamaged(), "First unit in line of sight should be hit by Cannon");
        assertEquals(2, firstVek.getHealth(), "First unit should receive 1 damage from Cannon");
        assertFalse(secondVek.isDamaged(), "Unit behind the first target should not take damage");
        assertEquals(3, secondVek.getHealth(), "Unit behind should keep its full health");
    }

    /*
     * Verificar que el Cañón devuelva false si dispara en línea recta
     * sin encontrar ninguna unidad u obstáculo hasta el borde del tablero.
     */
    @Test
    public void cannonReturnsFalseWhenNoObstacleOrUnitIsHitBeforeMapEdge() throws PositionOutOfBoundsException {
        // Arrange
        Weapon cannon = new Cannon(gameMap);
        Position mechPosition = new Position(4, 4);
        Position aimPosition = new Position(4, 5);

        Unit cannonMech = new Unit(Team.Player, cannon, 5, new Grounded(3));
        gameMap.tryPlaceUnit(cannonMech, mechPosition);

        UnitAction cannonAttack = cannonMech.getAttackAction(mechPosition, aimPosition);

        // Act
        boolean hit = cannonAttack.tryExecute(gameMap);

        // Assert
        assertFalse(hit, "Cannon should return false when reaching map bounds without hitting anything");
    }

    /*
     * Verificar que la Artillería pueda atacar por encima de obstáculos,
     * que no pueda atacar casillas adyacentes y que dañe la casilla objetivo
     * y empuje hacia afuera a las unidades de las 4 casillas adyacentes.
     */
    @Test
    public void artilleryAttacksOverObstaclesCannotTargetAdjacentTilesAndPushesFourAdjacentUnits() throws PositionOutOfBoundsException {
        // Arrange
        Weapon artillery = new Artillery(gameMap);
        Position mechPosition = new Position(0, 3);
        Position adjacentObstaclePosition = new Position(1, 3);
        Position targetPosition = new Position(4, 3);

        Position northPosition = new Position(4, 2);
        Position southPosition = new Position(4, 4);
        Position eastPosition = new Position(5, 3);
        Position westPosition = new Position(3, 3);

        Position northPushedPosition = new Position(4, 1);
        Position southPushedPosition = new Position(4, 5);
        Position eastPushedPosition = new Position(6, 3);
        Position westPushedPosition = new Position(2, 3);

        Unit artilleryMech = new Unit(Team.Player, artillery, 3, new Grounded(3));
        Unit centerVek = new Unit(Team.Player, null, 3, new Grounded(3));
        Unit northVek = new Unit(Team.Player, null, 3, new Grounded(3));
        Unit southVek = new Unit(Team.Player, null, 3, new Grounded(3));
        Unit eastVek = new Unit(Team.Player, null, 3, new Grounded(3));
        Unit westVek = new Unit(Team.Player, null, 3, new Grounded(3));

        gameMap.tryPlaceUnit(artilleryMech, mechPosition);
        gameMap.tryPlaceUnit(centerVek, targetPosition);
        gameMap.tryPlaceUnit(northVek, northPosition);
        gameMap.tryPlaceUnit(southVek, southPosition);
        gameMap.tryPlaceUnit(eastVek, eastPosition);
        gameMap.tryPlaceUnit(westVek, westPosition);

        UnitAction adjacentAttack = artilleryMech.getAttackAction(mechPosition, adjacentObstaclePosition);
        UnitAction validArtilleryAttack = artilleryMech.getAttackAction(mechPosition, targetPosition);

        // Act
        boolean hit = validArtilleryAttack.tryExecute(gameMap);

        // Assert
        assertTrue(hit, "Valid artillery attack should return true");
        assertThrows(RuntimeException.class, () -> adjacentAttack.tryExecute(gameMap), "Artillery should not be able to target adjacent tiles");
        assertTrue(centerVek.isDamaged(), "Unit on target cell should take damage over obstacle");
        assertEquals(2, centerVek.getHealth(), "Unit on target cell should receive 1 damage over obstacle");
        assertTrue(gameMap.hasUnitAt(targetPosition), "Unit on target cell should not move");
        assertTrue(gameMap.hasUnitAt(northPushedPosition), "North unit should be pushed to (4, 1)");
        assertTrue(gameMap.hasUnitAt(southPushedPosition), "South unit should be pushed to (4, 5)");
        assertTrue(gameMap.hasUnitAt(eastPushedPosition), "East unit should be pushed to (6, 3)");
        assertTrue(gameMap.hasUnitAt(westPushedPosition), "West unit should be pushed to (2, 3)");
        assertFalse(gameMap.hasUnitAt(northPosition), "North unit should have left its original position");
        assertFalse(gameMap.hasUnitAt(southPosition), "South unit should have left its original position");
        assertFalse(gameMap.hasUnitAt(eastPosition), "East unit should have left its original position");
        assertFalse(gameMap.hasUnitAt(westPosition), "West unit should have left its original position");
    }
}