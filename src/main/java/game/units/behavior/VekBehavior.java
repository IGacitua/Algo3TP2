package game.units.behavior;

import game.map.GameMap;
import game.position.PositionOutOfBoundsException;
import game.units.Vek;
import game.units.actions.UnitAction;

import java.util.ArrayList;
import java.util.List;

public class VekBehavior implements Behavior {
    private final List<BehaviorRules> rules;
    private final Vek vek;

    public VekBehavior(Vek vek) {
        this.vek = vek;
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
            List<UnitAction> actions = rule.evaluate(vek, map);
            if (actions != null) {
                return actions;
            }
        }
        return null;
    }
}
	