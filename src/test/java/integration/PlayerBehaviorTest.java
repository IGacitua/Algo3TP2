package integration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.Position2D;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.weapons.Punch;

public class PlayerBehaviorTest {
    private PlayerBehavior player; // TODO: crear la clase PlayerBehavior. Usar Mockito?
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

        this.origin = new Position2D(0, 0);
        this.combatMech = new Unit(this.origin, new Punch());
    }

    @Test
    public void mechCannotMoveTwiceInSameTurn() {
        // Arrange
        Position2D firstMoveDestination = new Position2D(0, 1);
        Position2D secondMoveDestination = new Position2D(0, 2);

        // Act
        UnitAction firstMoveAction = this.combatMech.getMove(firstMoveDestination);
        UnitAction secondMoveAction = this.combatMech.getMove(secondMoveDestination);
        this.player.executeAction(firstMoveAction);

        // Assert
        assertThrows(IllegalStateException.class, () -> {
            this.player.executeAction(secondMoveAction);
        }, "Expected an IllegalStateException when trying to move the mech twice in the same turn");
    }

    

    }

