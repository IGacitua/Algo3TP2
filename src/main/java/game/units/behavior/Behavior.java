package game.units.behavior;

import game.core.AttackPlanner;
import game.map.GameMap;
import game.position.Position;
import game.position.PositionOutOfBoundsException;

public interface Behavior {
    void executePhase(Position origin, GameMap map, AttackPlanner planner) throws PositionOutOfBoundsException;
}
