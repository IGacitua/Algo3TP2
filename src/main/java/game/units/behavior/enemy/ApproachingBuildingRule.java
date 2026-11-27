package game.units.behavior.enemy;

import game.map.GameMap;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Unit;
import game.units.actions.UnitAction;

import java.util.ArrayList;
import java.util.List;

public class ApproachingBuildingRule implements BehaviorRules {

    private List<UnitAction> getBestPlan(Unit unit, GameMap map, double minDistance, List<UnitAction> bestPlan, Position bestPos) throws PositionOutOfBoundsException {
        // TODO! (Vek no longer has position, need to be received as parameter.)
        /*
        for (Position pos : map.getReachablePositions(unit.getPosition())) {
            // El mapa resuelve la distancia topológica
            double dist = map.distanceToClosestVekTarget(pos);

            // Si encontramos una posición más cerca del edificio, la reemplazamos
            if (dist < minDistance) {
                minDistance = dist;
                bestPlan = unit.planMovementTo(, pos);
                bestPos = pos;
            }
            // Si están a la misma distancia, entra a jugar el desempate de la coordenada
            else if (dist == minDistance) {
                if (bestPos == null || pos.isCloserToOrigin(bestPos)) {
                    bestPlan = unit.planMovementTo(, pos);
                    bestPos = pos;
                }
            }
        }*/
        return bestPlan;
    }

    @Override
    public List<UnitAction> evaluate(Unit unit, Position origin, GameMap map) {
        List<UnitAction> bestPlan = new ArrayList<>();
        Position bestPos = null;
        double minDistance = Integer.MAX_VALUE;

        try {
            bestPlan = getBestPlan(unit, map, minDistance, bestPlan, bestPos);
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
