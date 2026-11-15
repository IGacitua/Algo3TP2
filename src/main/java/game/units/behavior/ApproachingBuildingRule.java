package game.units.behavior;

import java.util.ArrayList;
import java.util.List;

import game.map.GameMap;
import game.map.Position2D;
import game.units.Unit;
import game.units.Vek;
import game.units.actions.MoveAction;
import game.units.actions.UnitAction;

public class ApproachingBuildingRule implements BehaviorRules {
	@Override
	public List<UnitAction> evaluate(Vek vek, GameMap map) {
        List<UnitAction> bestPlan = new ArrayList<>();
        Position2D bestPos = null;
        int minDistance = Integer.MAX_VALUE;

        for (Position2D pos : map.getReachablePositions(vek)) {
            // El mapa resuelve la distancia topológica
            int dist = map.distanceToClosestBuildingFrom(pos);
            
            // Si encontramos una posición más cerca del edificio, la reemplazamos
            if (dist < minDistance) {
                minDistance = dist;
                bestPlan = vek.planMovementTo(pos);
                bestPos = pos;
            } 
            // Si están a la misma distancia, entra a jugar el desempate de la coordenada
            else if (dist == minDistance) {
                if (bestPos == null || pos.isHigherPriorityThan(bestPos)) {
                    bestPlan = vek.planMovementTo(pos);
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
