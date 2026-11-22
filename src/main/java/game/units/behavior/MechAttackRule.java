package game.units.behavior;

import game.map.GameMap;
import game.position.Position;
import game.units.Vek;
import game.units.actions.UnitAction;

import java.util.ArrayList;
import java.util.List;

public class MechAttackRule implements BehaviorRules {
    @Override
    public List<UnitAction> evaluate(Vek vek, GameMap map) {
        List<UnitAction> bestPlan = new ArrayList<>();
        Position bestPos = null;

        try {
            // TODO! (See ApproachingBuildingRule)

            /*
            for (Position pos : map.getReachablePositions(vek.getPosition())) {
                // El Vek intenta armar el plan de ataque a Mech
                List<UnitAction> plan = vek.planMechAttackFrom(, pos, map);

                if (!plan.isEmpty()) {
                    if (bestPos == null || pos.isCloserToOrigin(bestPos)) {
                        bestPlan = plan;
                        bestPos = pos;
                    }
                }
            }*/
        } catch (Exception e) {
            throw new RuntimeException(e); // TODO: Delete once we un-comment below.
        }
        //catch (PositionOutOfBoundsException e) {
        //    throw new RuntimeException(e); // TODO: Handle properly
        //}
        if (bestPlan.isEmpty()) {
            return null;
        } else {
            return bestPlan;
        }
    }
}
