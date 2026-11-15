package game.units.actions;

import game.map.Position2D;
import game.units.weapons.Weapon;

public class AttackAction extends UnitAction {
    private final Weapon weapon;
    private final Position2D origin;
    private final Position2D target;

    public AttackAction(Weapon weapon, Position2D target) {
        this(weapon, null, target);
    }

    public AttackAction(Weapon weapon, Position2D origin, Position2D target) {
        this.weapon = weapon;
        this.origin = origin;
        this.target = target;
    }

    @Override
    public void execute() {
        if (origin != null) {
            weapon.attack(origin, target);
            return;
        }
        weapon.attack(target);
    }
}
