package integration;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.Position2D;
import game.map.PositionOutOfBoundsException;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.behavior.PlayerBehavior;
import game.units.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerBehaviorTest {
    private PlayerBehavior player; // TODO: crear la clase PlayerBehavior
    private Unit combatMech;
    private Position2D origin;

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

        GameMap.getInstance().initialize(mockMapImporter, "");

        this.player = new PlayerBehavior();

        Weapon mockPunch = Mockito.mock(Weapon.class);
        this.origin = new Position2D(0, 0);
        this.combatMech = new Unit(this.origin, mockPunch);
    }

    @Test
    public void mechCannotMoveTwiceInSameTurn() throws PositionOutOfBoundsException {
        // Arrange
        Position2D firstMoveDestination = new Position2D(0, 1);
        Position2D secondMoveDestination = new Position2D(0, 2);

        // Act
        UnitAction firstMoveAction = this.combatMech.getMove(firstMoveDestination);
        UnitAction secondMoveAction = this.combatMech.getMove(secondMoveDestination);
        this.player.executeAction(firstMoveAction, GameMap.getInstance());

        // Assert
        assertThrows(IllegalStateException.class, () -> {
            this.player.executeAction(secondMoveAction, GameMap.getInstance());
        }, "Expected an IllegalStateException when trying to move the mech twice in the same turn");
    }

    @Test
    public void mechCannotAttackTwiceInSameTurn() throws PositionOutOfBoundsException {
        // Arrange
        Position2D attackedPosition = new Position2D(0, 1);

        // Act
        UnitAction firstAttackAction = this.combatMech.getAttack(attackedPosition);
        UnitAction secondAttackAction = this.combatMech.getAttack(attackedPosition);
        this.player.executeAction(firstAttackAction, GameMap.getInstance());

        // Assert
        assertThrows(IllegalStateException.class, () -> {
            this.player.executeAction(secondAttackAction, GameMap.getInstance());
        }, "Expected an IllegalStateException when trying to attack twice in the same turn");
    }

    @Test
    public void mechCanUndoMovementBeforeAttacking() throws PositionOutOfBoundsException {
        // Arrange
        Position2D moveDestination = new Position2D(0, 1);

        // Act
        UnitAction moveAction = this.combatMech.getMove(moveDestination);
        this.player.executeAction(moveAction, GameMap.getInstance());
        this.player.undoMovement(GameMap.getInstance());     // undoMovement deberia recibir la posicion original como argumento, o ya la deberia saber internamente?

        // Assert
        assertEquals(this.origin, this.combatMech.getPosition(),
                "Mech should be back to its original position after undoing the move");
    }

    @Test
    public void mechCannotUndoMovementAfterAttacking() throws PositionOutOfBoundsException {
        // Arrange
        Position2D moveDestination = new Position2D(0, 1);
        Position2D attackedPosition = new Position2D(0, 2);

        // Act
        UnitAction moveAction = this.combatMech.getMove(moveDestination);
        this.player.executeAction(moveAction, GameMap.getInstance());

        UnitAction attackAction = this.combatMech.getAttack(attackedPosition);
        this.player.executeAction(attackAction, GameMap.getInstance());

        // Assert
        assertThrows(IllegalStateException.class, () -> this.player.undoMovement(GameMap.getInstance()), "Expected an IllegalStateException when trying to undo movement after attacking");
    }

    @Test
    public void mechCannotUndoMovementTwiceInSameTurn() throws PositionOutOfBoundsException {
        // Arrange
        Position2D moveDestination = new Position2D(0, 1);

        // Act
        UnitAction moveAction = this.combatMech.getMove(moveDestination);
        this.player.executeAction(moveAction, GameMap.getInstance());
        this.player.undoMovement(GameMap.getInstance());

        this.player.executeAction(moveAction, GameMap.getInstance());

        // Assert
        assertThrows(IllegalStateException.class, () -> {
            this.player.undoMovement(GameMap.getInstance());
        }, "Expected an IllegalStateException when trying to undo movement twice in the same turn");
    }

    @Test
    public void mechCannotMoveAfterAttacking() throws PositionOutOfBoundsException {
        // Arrange
        Position2D moveDestination = new Position2D(0, 1);
        Position2D attackedPosition = new Position2D(0, 2);

        // Act
        UnitAction attackAction = this.combatMech.getAttack(attackedPosition);
        this.player.executeAction(attackAction, GameMap.getInstance());

        UnitAction moveAction = this.combatMech.getMove(moveDestination);

        // Assert
        assertThrows(IllegalStateException.class, () -> {
            this.player.executeAction(moveAction, GameMap.getInstance());
        }, "Expected an IllegalStateException when trying to move after attacking");
    }
}

