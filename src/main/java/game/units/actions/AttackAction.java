package game.units.actions;

import game.map.Position2D;
import game.units.weapons.Weapon;

public class AttackAction extends UnitAction {
    private final Weapon weapon;
    private final Position2D target;

    public AttackAction(Weapon weapon, Position2D target) {
        this.weapon = weapon;
        this.target = target;
    }

    @Override
    public void execute() {
        weapon.attack(target);
    }
}
