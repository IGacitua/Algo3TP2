package game.units;

import game.map.GameMap;
import game.position.Position;
import game.units.actions.MoveAction;
import game.units.actions.UnitAction;
import game.units.movement.Grounded;
import game.units.movement.Movement;
import game.units.weapons.Weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vek extends Unit {

    public Vek(Weapon weapon) {
        this(weapon, 3, new Grounded(1));
    }

    public Vek(Weapon weapon, int maximumHealth) {
        this(weapon, maximumHealth, new Grounded(1));
    }

    public Vek(Weapon weapon, int maximumHealth, Movement movement) {
        super(weapon, maximumHealth, movement);
    }

    // PRIORIDAD 1: Atacar edificio
    public List<UnitAction> planBuildingAttackFrom(Position origin, Position target, GameMap map) {
        Position targetBuilding = map.findClosestVekTarget(target);

        if (targetBuilding == null) {
            return Collections.emptyList();
        }

        List<UnitAction> plan = new ArrayList<>();
        plan.add(new MoveAction(origin, target));
        plan.add(this.getAttack(origin, targetBuilding)); // Usamos tu método de Unit!
        return plan;
    }

    // PRIORIDAD 2: Atacar Mech
    public List<UnitAction> planMechAttackFrom(Position origin, Position target, GameMap map) {
        Position targetMech = map.findClosestPlayerUnit(target);

        if (targetMech == null) {
            return Collections.emptyList();
        }

        List<UnitAction> plan = new ArrayList<>();
        plan.add(new MoveAction(origin, target));
        plan.add(this.getAttack(origin, targetMech)); // Usamos tu método de Unit!
        return plan;
    }

    // PRIORIDAD 3: Moverse / Acercarse
    public List<UnitAction> planMovementTo(Position origin, Position targetPos) {
        List<UnitAction> plan = new ArrayList<>();
        plan.add(new MoveAction(origin, targetPos));
        return plan;
    }
}
