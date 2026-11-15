package game.units.behavior;

import java.util.ArrayList;
import java.util.List;

import game.map.GameMap;
import game.units.Unit;
import game.units.Vek;
import game.units.actions.UnitAction;

public class VekBehavior implements Behavior {
	private List<BehaviorRules> rules;
	private Vek vek;
	
	public VekBehavior(Vek vek) {
		this.vek = vek;
		this.rules = new ArrayList<>();
		rules.add(new BuildingAttackRule());
		rules.add(new MechAttackRule());
		rules.add(new ApproachingBuildingRule());
	}
	
	@Override
	public void executePhase(GameMap map) {
		List<UnitAction> actionsToExecute = evaluatePriorityRules(map);
		if (actionsToExecute != null) {
            // VekBehavior asume la responsabilidad de ejecutar la secuencia
            for (UnitAction action : actionsToExecute) {
                action.execute(map); 
            }
        }
	}
	
	private List<UnitAction> evaluatePriorityRules(GameMap map) {
        for (BehaviorRules rule : rules) {
            List<UnitAction> actions = rule.evaluate(vek, map);
            if (actions != null) {
                return actions; 
            }
        }
        return null; 
    }
}
	