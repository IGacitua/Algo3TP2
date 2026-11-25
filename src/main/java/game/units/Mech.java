package game.units;

import game.map.GameMap;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.actions.MoveAction;
import game.units.movement.Movement;
import game.units.weapons.Weapon;

public class Mech extends Unit {
    public Mech(Weapon weapon, int maximumHealth, Movement movement) {
        super(weapon, maximumHealth, movement);
    }

    @Override
    public boolean isMech() {
        return true;
    }

    public MoveAction createMoveAction(Position origin, Position destination) {
        return new MoveAction(origin, destination);
    }

    // Regla de que un Mech en el agua no puede atacar
    public boolean canAttack(Position origin, GameMap map) throws PositionOutOfBoundsException {
        return !map.hasWaterAt(origin);
    }

}
