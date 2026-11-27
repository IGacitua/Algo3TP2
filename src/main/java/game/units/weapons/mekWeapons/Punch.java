package game.units.weapons.mekWeapons;

import game.map.GameMap;
import game.position.Position;
import game.position.direction.OrthogonalDirection;
import game.units.weapons.Weapon;

public class Punch extends Weapon {

    private static final int DAMAGE = 2;

    public Punch(GameMap map) {
        super(map, DAMAGE);
    }

    @Override
    public boolean attack(Position origin, Position target) {
        if (origin.getOrthogonalDistance(target) != 1.0) {
            throw new IllegalArgumentException("Punch can only target an orthogonally adjacent tile.");
        }

        OrthogonalDirection pushDirection = origin.getDirectionTo(target);
        return applyPunch(target, pushDirection);
    }

    @Override
    public boolean attack(Position target) {
        throw new UnsupportedOperationException("Punch requires an origin position to determine the hit direction.");
    }

    private boolean applyPunch(Position target, OrthogonalDirection pushDirection) {
        map.attackCell(target, DAMAGE);
        map.pushUnit(target, pushDirection);
        return true;
    }
}
