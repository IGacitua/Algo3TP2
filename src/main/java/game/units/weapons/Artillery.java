package game.units.weapons;

import game.map.GameMap;
import game.position.Position;

public class Artillery extends Weapon {

    private static final int DAMAGE = 1;
    private static final int MIN_RANGE = 2;

    public Artillery(GameMap map) {
        super(map, DAMAGE);
    }

    @Override
    public boolean attack(Position target) {
        map.attackCell(target, DAMAGE);
        map.pushAllAdjacent(target);
        return true;
    }

    @Override
    public boolean attack(Position origin, Position target) {
        if (origin.getDiagonalDistance(target) < MIN_RANGE) {
            throw new IllegalArgumentException("Artillery can only target tiles in a straight line at distance 2 or more.");
        }

        return attack(target);
    }

}
