package game.units;

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
}
