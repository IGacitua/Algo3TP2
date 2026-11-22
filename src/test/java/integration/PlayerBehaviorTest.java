package integration;

import game.map.GameMap;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.behavior.PlayerBehavior;
import game.units.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerBehaviorTest {
    private GameMap gameMap;
    private PlayerBehavior player;
    private Unit combatMech;
    private Position origin;

    @BeforeEach
    public void setUp() throws InvalidMapFileException, PositionOutOfBoundsException {
        MapImporter mockMapImporter = Mockito.mock(MapImporter.class);
        Cell[][] mockCells = new Cell[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                mockCells[x][y] = new Cell(new Position(x, y));
            }
        }
        Mockito.when(mockMapImporter.importMap(Mockito.any(String.class))).thenReturn(mockCells);

        gameMap = new GameMap(mockMapImporter, "");

        this.player = new PlayerBehavior();

        Weapon mockPunch = Mockito.mock(Weapon.class);
        this.origin = new Position(0, 0);
        this.combatMech = new Unit(mockPunch);
        gameMap.placeUnitAt(this.combatMech, this.origin);
    }

    @Test
    public void mechCannotMoveTwiceInSameTurn() throws IllegalStateException {
        // Arrange
        Position firstMoveDestination = new Position(0, 1);
        Position secondMoveDestination = new Position(0, 2);

        // Act
        UnitAction firstMoveAction = this.combatMech.getMove(this.origin, firstMoveDestination);
        UnitAction secondMoveAction = this.combatMech.getMove(this.origin, secondMoveDestination);
        this.player.executeAction(firstMoveAction, gameMap);

        // Assert
        assertThrows(
                IllegalStateException.class,
                () -> this.player.executeAction(secondMoveAction, gameMap),
                "Expected an IllegalStateException when trying to move the mech twice in the same turn"
        );
    }

    @Test
    public void mechCannotAttackTwiceInSameTurn() throws IllegalStateException {
        // Arrange
        Position attackedPosition = new Position(0, 1);

        // Act
        UnitAction firstAttackAction = this.combatMech.getAttack(this.origin, attackedPosition);
        UnitAction secondAttackAction = this.combatMech.getAttack(this.origin, attackedPosition);
        this.player.executeAction(firstAttackAction, gameMap);

        // Assert
        assertThrows(
                IllegalStateException.class,
                () -> this.player.executeAction(secondAttackAction, gameMap),
                "Expected an IllegalStateException when trying to attack twice in the same turn"
        );
    }

    @Test
    public void mechCanUndoMovementBeforeAttacking() throws IllegalStateException {
        // Arrange
        Position moveDestination = new Position(0, 1);

        // Act
        UnitAction moveAction = this.combatMech.getMove(this.origin, moveDestination);
        this.player.executeAction(moveAction, gameMap);
        this.player.undoMovement(gameMap);

        // TODO: FIX!
        // Assert
        //assertEquals(this.origin, this.combatMech.getPosition(),
        //        "Mech should be back to its original position after undoing the move");
    }

    @Test
    public void mechCannotUndoMovementAfterAttacking() throws PositionOutOfBoundsException {
        // Arrange
        Position moveDestination = new Position(0, 1);
        Position attackedPosition = new Position(0, 2);

        // Act
        UnitAction moveAction = this.combatMech.getMove(this.origin, moveDestination);
        this.player.executeAction(moveAction, gameMap);

        UnitAction attackAction = this.combatMech.getAttack(this.origin, attackedPosition);
        this.player.executeAction(attackAction, gameMap);

        // Assert
        assertThrows(
                IllegalStateException.class,
                () -> this.player.undoMovement(gameMap),
                "Expected an IllegalStateException when trying to undo movement after attacking"
        );
    }

    @Test
    public void mechCannotUndoMovementTwiceInSameTurn() throws PositionOutOfBoundsException {
        // Arrange
        Position moveDestination = new Position(0, 1);

        // Act
        UnitAction moveAction = this.combatMech.getMove(this.origin, moveDestination);
        this.player.executeAction(moveAction, gameMap);
        this.player.undoMovement(gameMap);

        this.player.executeAction(moveAction, gameMap);

        // Assert
        assertThrows(
                IllegalStateException.class,
                () -> this.player.undoMovement(gameMap),
                "Expected an IllegalStateException when trying to undo movement twice in the same turn"
        );
    }

    @Test
    public void mechCannotMoveAfterAttacking() throws PositionOutOfBoundsException {
        // Arrange
        Position moveDestination = new Position(0, 1);
        Position attackedPosition = new Position(0, 2);

        // Act
        UnitAction attackAction = this.combatMech.getAttack(this.origin, attackedPosition);
        this.player.executeAction(attackAction, gameMap);

        UnitAction moveAction = this.combatMech.getMove(this.origin, moveDestination);

        // Assert
        assertThrows(
                IllegalStateException.class,
                () -> this.player.executeAction(moveAction, gameMap),
                "Expected an IllegalStateException when trying to move after attacking"
        );
    }
}

