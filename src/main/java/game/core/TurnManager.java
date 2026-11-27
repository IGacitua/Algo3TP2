package game.core;

import game.map.GameMap;
import game.map.OccupiedCell;
import game.position.PositionOutOfBoundsException;
import game.units.Team;
import game.units.Unit;

public class TurnManager {
    private final GameMap map;
    private final AttackPlanner attackPlanner;

    public TurnManager(GameMap map, AttackPlanner attackPlanner) {
        this.map = map;
        this.attackPlanner = attackPlanner;
    }

    public void executePlayerPhase() throws PositionOutOfBoundsException {
        executeTeamPhase(Team.Player);
    }

    public void executeEnemyPhase() throws PositionOutOfBoundsException {
        executeTeamPhase(Team.Enemy);
    }

    public void executeResolutionPhase() throws PositionOutOfBoundsException {
        attackPlanner.resolveAll(map);
    }

    private void executeTeamPhase(Team team) throws PositionOutOfBoundsException {
        for (OccupiedCell cell : map.getOccupiedCells()) {
            Unit unit = cell.getUnit();
            if (unit.isInTeam(team)) {
                unit.resetMovementDistance();
                unit.executePhase(cell.getPosition(), map, attackPlanner);
            }
        }
    }
}
