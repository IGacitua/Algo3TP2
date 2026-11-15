package game.units;

import game.map.GameMap;
import game.map.Position2D;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.movement.MovementType;
import game.units.weapons.Weapon;

public class Mech extends Unit{
	
	// Usamos la misma firma que tu constructor principal de Unit
    public Mech(Position2D position, Weapon weapon, int maximumHealth, MovementType movementType) {
        super(position, weapon, maximumHealth, movementType);
    }

    public MoveAction createMoveCommand(Position2D destination) {
        return new MoveAction(this, this.getPosition(), destination);
    }

    // Regla de que un Mech en el agua no puede atacar
    public boolean canAttack(GameMap map) {
        return !map.isWaterAt(this.getPosition());
    }
    
}
