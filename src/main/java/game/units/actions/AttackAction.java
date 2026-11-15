package game.units.actions;

import game.map.GameMap;
import game.map.Position2D;
import game.units.weapons.Weapon;

public class AttackAction implements UnitAction {
    private final Weapon weapon;
    private final Position2D origin;
    private final Position2D target;

    public AttackAction(Weapon weapon, Position2D target) {
        this(weapon, null, target);
    }

    public AttackAction(Weapon weapon, Position2D target, Position2D origin) {
        this.weapon = weapon;
        this.target = target;
        this.origin = origin;
    }

    @Override
    public void execute(GameMap map) {
        if (origin != null) {
        	// Se le pasa el origen, el objetivo y el mapa al arma
            weapon.attack(origin, target, map);
        } else {
            weapon.attack(target, map);
        }
    }
}
