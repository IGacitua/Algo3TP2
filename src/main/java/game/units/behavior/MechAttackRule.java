package game.units.behavior;

import game.map.GameMap;
import game.position.Position2D;
import game.position.PositionOutOfBoundsException;
import game.units.Vek;
import game.units.actions.UnitAction;

import java.util.ArrayList;
import java.util.List;

public class MechAttackRule implements BehaviorRules {
    @Override
    public List<UnitAction> evaluate(Vek vek, GameMap map) {
        List<UnitAction> bestPlan = new ArrayList<>();
        Position2D bestPos = null;

        try {
            for (Position2D pos : map.getReachablePositions(vek.getPosition())) {
                // El Vek intenta armar el plan de ataque a Mech
                List<UnitAction> plan = vek.planMechAttackFrom(pos, map);

                if (!plan.isEmpty()) {
                    if (bestPos == null || pos.isHigherPriorityThan(bestPos)) {
                        bestPlan = plan;
                        bestPos = pos;
                    }
                }
            }
        } catch (PositionOutOfBoundsException e) {
            throw new RuntimeException(e); // TODO: Handle properly
        }
        if (bestPlan.isEmpty()) {
            return null;
        } else {
            return bestPlan;
        }
    }
}
