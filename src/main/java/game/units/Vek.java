package game.units;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.map.GameMap;
import game.map.Position2D;
import game.units.actions.MoveAction;
import game.units.actions.UnitAction;
import game.units.movement.MovementType;
import game.units.weapons.Weapon;

public class Vek extends Unit {
	
	public Vek(Position2D position, Weapon weapon, int maximumHealth, MovementType movementType) {
        super(position, weapon, maximumHealth, movementType);
    }

    // PRIORIDAD 1: Atacar edificio
    public List<UnitAction> planBuildingAttackFrom(Position2D targetPos, GameMap map) {
        Position2D targetBuilding = map.findAttackableBuildingFrom(targetPos, this.weapon);
        
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
        Position2D targetMech = map.findAttackableMechFrom(targetPos, this.weapon);
        
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
