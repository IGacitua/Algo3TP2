package game.units.behavior.enemy;

import game.map.GameMap;
import game.units.Unit;
import game.units.actions.UnitAction;

import java.util.List;

public interface BehaviorRules {
    // El método principal que implementará cada regla
    List<UnitAction> evaluate(Unit vek, GameMap map);

}
