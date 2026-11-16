package game.units.behavior;

import game.map.GameMap;
import game.position.Position2D;
import game.position.PositionOutOfBoundsException;
import game.units.Vek;
import game.units.actions.UnitAction;

import java.util.ArrayList;
import java.util.List;

public class ApproachingBuildingRule implements BehaviorRules {
    @Override
    public List<UnitAction> evaluate(Vek vek, GameMap map) {
        List<UnitAction> bestPlan = new ArrayList<>();
        Position2D bestPos = null;
        double minDistance = Integer.MAX_VALUE;

        try {
            for (Position2D pos : map.getReachablePositions(vek.getPosition())) {
                // El mapa resuelve la distancia topológica
                double dist = map.distanceToClosestVekTarget(pos);

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
