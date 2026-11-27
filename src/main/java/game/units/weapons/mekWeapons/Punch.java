package game.units.weapons.mekWeapons;

import game.map.GameMap;
import game.position.Position;
import game.position.direction.OrthogonalDirection;
import game.units.weapons.Weapon;

public class Punch implements Weapon {

    private static final int DAMAGE = 2;

    @Override
    public boolean attack(GameMap map, Position origin, Position target) {
        if (origin.getOrthogonalDistance(target) != 1.0) {
            throw new IllegalArgumentException("Punch can only target an orthogonally adjacent tile.");
        }

        OrthogonalDirection pushDirection = origin.getDirectionTo(target);
        map.attackCell(target, DAMAGE);
        map.pushUnit(target, pushDirection);
        return true;
    }

}
