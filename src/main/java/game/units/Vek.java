package game.units;

import game.map.GameMap;
import game.position.Position2D;
import game.units.actions.MoveAction;
import game.units.actions.UnitAction;
import game.units.movement.Grounded;
import game.units.movement.Movement;
import game.units.weapons.Weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vek extends Unit {

    public Vek(Position2D position, Weapon weapon) {
        this(position, weapon, 3, new Grounded(1));
    }

    public Vek(Position2D position, Weapon weapon, int maximumHealth) {
        this(position, weapon, maximumHealth, new Grounded(1));
    }

    public Vek(Position2D position, Weapon weapon, int maximumHealth, Movement movement) {
        super(position, weapon, maximumHealth, movement);
    }

    @Override
    public boolean isMech() {
        return false;
    }

    // PRIORIDAD 1: Atacar edificio
    public List<UnitAction> planBuildingAttackFrom(Position2D targetPos, GameMap map) {
        Position2D targetBuilding = map.findClosestVekTarget(targetPos);

        if (targetBuilding != null) {
            List<UnitAction> plan = new ArrayList<>();
            plan.add(new MoveAction(this, this.getPosition(), targetPos));
            plan.add(this.getAttack(targetBuilding)); // Usamos tu método de Unit!
            return plan;
        }
        return Collections.emptyList();
    }

    // PRIORIDAD 2: Atacar Mech
    public List<UnitAction> planMechAttackFrom(Position2D targetPos, GameMap map) {
        Position2D targetMech = map.findAttackableMechFrom(targetPos);

        if (targetMech != null) {
            List<UnitAction> plan = new ArrayList<>();
            plan.add(new MoveAction(this, this.getPosition(), targetPos));
            plan.add(this.getAttack(targetMech)); // Usamos tu método de Unit!
            return plan;
        }
        return Collections.emptyList();
    }

    // PRIORIDAD 3: Moverse / Acercarse
    public List<UnitAction> planMovementTo(Position2D targetPos) {
        List<UnitAction> plan = new ArrayList<>();
        plan.add(new MoveAction(this, this.getPosition(), targetPos));
        return plan;
    }
}
