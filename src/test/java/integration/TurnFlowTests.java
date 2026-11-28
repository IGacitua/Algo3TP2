package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InOrder;
import org.mockito.Mockito;

import game.core.AttackPlanner;
import game.core.PlannedAttack;
import game.core.TurnManager;
import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.cell.Building;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.South;
import game.units.Team;
import game.units.Unit;
import game.units.behavior.enemy.EnemyBehavior;
import game.units.movement.Grounded;
import game.units.weapons.AcidShot;
import game.units.weapons.Weapon;

public class TurnFlowTests {
    private static final Position BUILDING_POS = new Position(3, 2);
    private static final Position VEK1_POS = new Position(2, 2);
    private static final Position VEK2_POS = new Position(4, 2);

    private GameMap gameMap;
    private ElectricalNetwork mockNetwork;
    private AttackPlanner attackPlanner;
    private TurnManager turnManager;

    private Weapon firstWeapon;
    private Weapon secondWeapon;
    private Unit vek1;
    private Unit vek2;
    
    @BeforeEach 
    public void setUp()throws InvalidMapFileException, PositionOutOfBoundsException {
        mockNetwork = Mockito.mock(ElectricalNetwork.class);

        Cell[][] mockCells = new Cell[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                mockCells[x][y] = new Cell(new Position(x, y));
            }
        }
        mockCells[3][2] = new Building(BUILDING_POS, mockNetwork);

        MapImporter mockMapImporter = Mockito.mock(MapImporter.class);
        Mockito.when(mockMapImporter.importMap(Mockito.anyString())).thenReturn(mockCells);

        this.gameMap = new GameMap(mockMapImporter, "");
        this.attackPlanner = new AttackPlanner();
        this.turnManager = new TurnManager(this.gameMap, this.attackPlanner);

        this.firstWeapon = Mockito.spy(new AcidShot(this.gameMap));
        this.secondWeapon = Mockito.spy(new AcidShot(this.gameMap));

        this.vek1 = new Unit(Team.Enemy, this.firstWeapon, 3, new Grounded(2));
        this.vek1.setBehavior(new EnemyBehavior(this.vek1));
        this.vek2 = new Unit(Team.Enemy, this.secondWeapon, 3, new Grounded(2));
        this.vek2.setBehavior(new EnemyBehavior(this.vek2));
    }

    @Test 
    public void veksAnnounceAttackInVekPhaseWithoutExecutingThem() throws PositionOutOfBoundsException {
        // Arrange
        this.gameMap.tryPlaceUnit(vek1, VEK1_POS);
        this.gameMap.tryPlaceUnit(vek2, VEK2_POS);

        // Act
        this.turnManager.executeEnemyPhase();

        // Assert
        assertEquals(2, this.attackPlanner.getPlannedAttacks().size(), "Both Vek units should have announced their attacks during the enemy phase.");
        Mockito.verifyNoInteractions(this.firstWeapon, this.secondWeapon, this.mockNetwork);
    }

    @Test 
    public void announcedAttacksExecuteInAnnouncedOrderInResolutionPhase() throws PositionOutOfBoundsException {
        // Arrange
        this.gameMap.tryPlaceUnit(vek1, VEK1_POS);
        this.gameMap.tryPlaceUnit(vek2, VEK2_POS);

        this.attackPlanner.announceAttack(new PlannedAttack(vek1, VEK1_POS, BUILDING_POS));
        this.attackPlanner.announceAttack(new PlannedAttack(vek2, VEK2_POS, BUILDING_POS));
        
        // Act
        this.turnManager.executeResolutionPhase();
        InOrder inOrder = Mockito.inOrder(this.firstWeapon, this.secondWeapon);
        
        // Assert
        inOrder.verify(this.firstWeapon).attack(VEK1_POS, BUILDING_POS);
        inOrder.verify(this.secondWeapon).attack(VEK2_POS, BUILDING_POS);
        assertTrue(this.attackPlanner.getPlannedAttacks().isEmpty(), "All planned attacks should have been executed and cleared from the planner.");
    }

    @Test 
    public void pushedVekShiftsAnnouncedAttackToNewPosition() throws PositionOutOfBoundsException {
        // Arrange
        this.gameMap.tryPlaceUnit(vek1, VEK1_POS);

        // Act
        this.turnManager.executeEnemyPhase();
        this.gameMap.pushUnit(VEK1_POS, new South());
        this.turnManager.executeResolutionPhase();

        // Assert
        assertTrue(this.gameMap.hasUnitAt(new Position(2, 3), Team.Enemy));
        Mockito.verifyNoInteractions(this.mockNetwork);
    }

    @Test 
    public void pushedVekDamagesAnotherVekWhenShiftedAttackPointsAtIt() throws PositionOutOfBoundsException {
        // Arrange
        this.gameMap.tryPlaceUnit(vek1, VEK1_POS);
        this.gameMap.tryPlaceUnit(vek2, new Position(3, 3));

        // Act
        this.turnManager.executeEnemyPhase();
        this.gameMap.pushUnit(VEK1_POS, new South());
        this.turnManager.executeResolutionPhase();

        // Assert
        assertEquals(2, this.vek2.getHealth(), "The second Vek should have taken damage from the shifted attack.");
    }
}
