package game.units.behavior.enemy;

import game.core.AttackPlanner;
import game.core.PlannedAttack;
import game.map.GameMap;
import game.position.Position;
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
    public void executePhase(Position origin, GameMap map, AttackPlanner planner) throws PositionOutOfBoundsException {
        List<UnitAction> actionsToExecute = evaluatePriorityRules(origin, map);
        if (actionsToExecute != null) {
            for (UnitAction action : actionsToExecute) {
                action.executeWith(this, map, planner);
            }
        }
    }

    public void announceAttack(Position origin, Position target, AttackPlanner planner) {
        planner.announceAttack(new PlannedAttack(unit, origin, target));
    }

    private List<UnitAction> evaluatePriorityRules(Position origin, GameMap map) {
        for (BehaviorRules rule : rules) {
            List<UnitAction> actions = rule.evaluate(unit, origin, map);
            if (actions != null) {
                return actions;
            }
        }
        return null;
    }
}
	