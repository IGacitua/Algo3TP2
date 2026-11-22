package game.units.actions;

import game.map.GameMap;
import game.position.Position;
import game.units.weapons.Weapon;

public class AttackAction implements UnitAction {
    private final Weapon weapon;
    private final Position origin;
    private final Position target;

    public AttackAction(Weapon weapon, Position origin, Position target) {
        this.weapon = weapon;
        this.origin = origin;
        this.target = target;
    }

    @Override
    public boolean tryExecute(GameMap map) {
        weapon.attack(origin, target, map); // Se le pasa el origen, el objetivo y el mapa al arma
        return true; // TODO: Cambiar Weapon::Attack para que devuelva si el ataque se logró o no.
    }
}
