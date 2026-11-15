package game.units.behavior;

import java.util.ArrayList;
import java.util.List;

import game.map.GameMap;
import game.map.Position2D;
import game.units.Unit;
import game.units.Vek;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.actions.UnitAction;

public class BuildingAttackRule implements BehaviorRules {
	@Override
	public List<UnitAction> evaluate(Vek vek, GameMap map) {
        List<UnitAction> bestPlan = new ArrayList<>();
        Position2D bestPos = null;

        for (Position2D pos : map.getReachablePositions(vek)) {
            List<UnitAction> plan = vek.planBuildingAttackFrom(pos, map);
            
            if (!plan.isEmpty()) {
                // Si es el primer plan válido, o si gana en el desempate (menor Y, menor X)
                if (bestPos == null || pos.isHigherPriorityThan(bestPos)) {
                    bestPlan = plan;
                    bestPos = pos;
                }
            }
        }
        
        if (bestPlan.isEmpty()) {
            return null;
        } else {
            return bestPlan;
        }
    }
}
