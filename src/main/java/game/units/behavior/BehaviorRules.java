package game.units.behavior;

import java.util.List;
import game.map.GameMap;
import game.units.Vek;
import game.units.actions.UnitAction;

public interface BehaviorRules {
	// El método principal que implementará cada regla
    List<UnitAction> evaluate(Vek vek, GameMap map);

}
