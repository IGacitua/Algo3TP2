package game.units.behavior;

import game.map.GameMap;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Vek;
import game.units.actions.UnitAction;

import java.util.ArrayList;
import java.util.List;

public class ApproachingBuildingRule implements BehaviorRules {

    private List<UnitAction> getBestPlan(Vek vek, GameMap map, double minDistance, List<UnitAction> bestPlan, Position bestPos) throws PositionOutOfBoundsException {
        // TODO! (Vek no longer has position, need to be received as parameter.)
        /*
        for (Position pos : map.getReachablePositions(vek.getPosition())) {
            // El mapa resuelve la distancia topológica
            double dist = map.distanceToClosestVekTarget(pos);

            // Si encontramos una posición más cerca del edificio, la reemplazamos
            if (dist < minDistance) {
                minDistance = dist;
                bestPlan = vek.planMovementTo(, pos);
                bestPos = pos;
            }
            // Si están a la misma distancia, entra a jugar el desempate de la coordenada
            else if (dist == minDistance) {
                if (bestPos == null || pos.isCloserToOrigin(bestPos)) {
                    bestPlan = vek.planMovementTo(, pos);
                    bestPos = pos;
                }
            }
        }*/
        return bestPlan;
    }

    @Override
    public List<UnitAction> evaluate(Vek vek, GameMap map) {
        List<UnitAction> bestPlan = new ArrayList<>();
        Position bestPos = null;
        double minDistance = Integer.MAX_VALUE;

        try {
            bestPlan = getBestPlan(vek, map, minDistance, bestPlan, bestPos);
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
