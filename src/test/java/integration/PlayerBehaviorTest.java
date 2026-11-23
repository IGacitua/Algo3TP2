package integration;

import game.map.GameMap;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Mech;
import game.units.actions.UnitAction;
import game.units.behavior.PlayerBehavior;
import game.units.behavior.TurnUndoTracker;
import game.units.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PlayerBehaviorTest {
    private GameMap gameMap;
    private TurnUndoTracker undoTracker;
    private PlayerBehavior player;
    private Mech combatMech;
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

        this.gameMap = new GameMap(mockMapImporter, "");
        this.undoTracker = new TurnUndoTracker();
        
        Weapon mockPunch = Mockito.mock(Weapon.class);
        Mockito.when(mockPunch.attack(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);

        this.origin = new Position(0, 0);
        this.combatMech = new Mech(mockPunch);
        this.gameMap.placeUnitAt(this.combatMech, this.origin);
        
        this.player = new PlayerBehavior(this.combatMech, this.undoTracker);
    }

    @Test
    public void mechCannotMoveTwiceInSameTurn() throws IllegalStateException {
        // Arrange
        Position firstMoveDestination = new Position(0, 1);
        Position secondMoveDestination = new Position(0, 2);

        // Act
        UnitAction firstMoveAction = this.combatMech.getMove(this.origin, firstMoveDestination);
        UnitAction secondMoveAction = this.combatMech.getMove(firstMoveDestination, secondMoveDestination);
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
    public void mechCanUndoMovementBeforeAttacking() throws PositionOutOfBoundsException {
        // Arrange
        Position moveDestination = new Position(0, 1);
        UnitAction moveAction = this.combatMech.getMove(this.origin, moveDestination);

        // Act
        this.player.executeAction(moveAction, gameMap);       
        assertTrue(this.gameMap.hasMechAt(moveDestination)); 
        this.player.undoMovement(gameMap);

        // Assert
        assertTrue(this.gameMap.hasMechAt(this.origin), 
            "Expected the mech to be back at its original position after undoing movement");    
        assertFalse(this.gameMap.hasUnitAt(moveDestination), 
            "Expected the destination cell to be empty after undoing movement");    
    }

    @Test
    public void mechCannotUndoMovementAfterAttacking() throws IllegalStateException {
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
    public void mechCannotUndoMovementTwiceInSameTurn() throws IllegalStateException {
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
    public void mechCannotMoveAfterAttacking() throws IllegalStateException {
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

    @Test
    public void twoMechsShareSingleUndoPerTurn() throws PositionOutOfBoundsException {
        // Arrange
        Position secondMechOrigin = new Position(1, 0);
        Mech secondMech = new Mech(Mockito.mock(Weapon.class));
        this.gameMap.placeUnitAt(secondMech, secondMechOrigin);

        PlayerBehavior secondPlayer = new PlayerBehavior(secondMech, this.undoTracker);

        Position firstMechMoveDestination = new Position(0, 1);
        Position secondMechMoveDestination = new Position(1, 1);

        // Act
        UnitAction firstMechMoveAction = this.combatMech.getMove(this.origin, firstMechMoveDestination);
        UnitAction secondMechMoveAction = secondMech.getMove(secondMechOrigin, secondMechMoveDestination);

        this.player.executeAction(firstMechMoveAction, gameMap);
        secondPlayer.executeAction(secondMechMoveAction, gameMap);

        assertTrue(this.gameMap.hasMechAt(firstMechMoveDestination));
        assertTrue(this.gameMap.hasMechAt(secondMechMoveDestination));

        this.player.undoMovement(gameMap);
        assertTrue(this.gameMap.hasMechAt(this.origin));

        // Assert
        assertThrows(
                IllegalStateException.class,
                () -> secondPlayer.undoMovement(gameMap),
                "Expected an IllegalStateException when trying to undo movement for the second mech after the first " +
                    "mech has already undone its movement in the same turn"
        );
    }

}

