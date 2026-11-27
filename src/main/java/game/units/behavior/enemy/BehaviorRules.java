package game.units.behavior.enemy;

import game.map.GameMap;
import game.position.Position;
import game.units.Unit;
import game.units.actions.UnitAction;

import java.util.List;

public interface BehaviorRules {
    List<UnitAction> evaluate(Unit vek, Position origin, GameMap map);
}
