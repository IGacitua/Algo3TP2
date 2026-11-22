package game.units.behavior;

import game.map.GameMap;
import game.position.Position;
import game.units.Vek;
import game.units.actions.UnitAction;

import java.util.ArrayList;
import java.util.List;

public class BuildingAttackRule implements BehaviorRules {
    @Override
    public List<UnitAction> evaluate(Vek vek, GameMap map) {
        List<UnitAction> bestPlan = new ArrayList<>();
        Position bestPos = null;

        try {
            // TODO! (See ApproachingBuildingRule)
            /*
            for (Position pos : map.getReachablePositions(vek.getPosition())) {
                List<UnitAction> plan = vek.planBuildingAttackFrom(, pos, map);

                if (!plan.isEmpty()) {
                    // Si es el primer plan válido, o si gana en el desempate (menor Y, menor X)
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
        //    throw new RuntimeException(e); // TODO: handle properly
        //}

        if (bestPlan.isEmpty()) {
            return null;
        } else {
            return bestPlan;
        }
    }
}
