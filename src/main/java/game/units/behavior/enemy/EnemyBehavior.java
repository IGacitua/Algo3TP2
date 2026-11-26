package game.units.behavior.enemy;

import game.map.GameMap;
import game.position.PositionOutOfBoundsException;
import game.units.Unit;
import game.units.actions.UnitAction;
import game.units.behavior.Behavior;

import java.util.ArrayList;
import java.util.List;

public class EnemyBehavior implements Behavior {
    private final List<BehaviorRules> rules;
    private final Unit unit;

    public EnemyBehavior(Unit unit) {
        this.unit = unit;
        this.rules = new ArrayList<>();
        rules.add(new BuildingAttackRule());
        rules.add(new MechAttackRule());
        rules.add(new ApproachingBuildingRule());
    }

    @Override
    public void executePhase(GameMap map) throws PositionOutOfBoundsException {
        List<UnitAction> actionsToExecute = evaluatePriorityRules(map);
        if (actionsToExecute != null) {
            // VekBehavior asume la responsabilidad de ejecutar la secuencia
            for (UnitAction action : actionsToExecute) {
                action.tryExecute(map);
            }
        }
    }

    private List<UnitAction> evaluatePriorityRules(GameMap map) {
        for (BehaviorRules rule : rules) {
            List<UnitAction> actions = rule.evaluate(unit, map);
            if (actions != null) {
                return actions;
            }
        }
        return null;
    }
}
	