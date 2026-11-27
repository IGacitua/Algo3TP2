package game.core;

import java.util.ArrayList;
import java.util.List;

import game.map.GameMap;
import game.map.OccupiedCell;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Unit;

public class AttackPlanner {
    private final List<PlannedAttack> plannedAttacks = new ArrayList<>();

    public void announceAttack(PlannedAttack attack) {
        if (attack != null) {
            plannedAttacks.add(attack);
        }
    }

    public void resolveAll(GameMap map) throws PositionOutOfBoundsException {
        List<OccupiedCell> occupiedCells = map.getOccupiedCells();

        for (PlannedAttack attack : plannedAttacks) {
            Position currentPosition = findUnitPosition(attack.getAttacker(), occupiedCells);
            try {
                attack.executeFrom(currentPosition, map);
            } catch (PositionOutOfBoundsException e) {
                // Ignore attacks that go out of bounds.
            }
        }
        plannedAttacks.clear();
    }

    public List<PlannedAttack> getPlannedAttacks() {
        return List.copyOf(plannedAttacks);
    }

    private Position findUnitPosition(Unit targetUnit, List<OccupiedCell> occupiedCells) {
        for (OccupiedCell cell : occupiedCells) {
            if (cell.getUnit().equals(targetUnit)) {
                return cell.getPosition();
            }
        }
        return null;
    }
 
}
