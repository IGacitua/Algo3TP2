package game.units.weapons;

import game.map.GameMap;
import game.position.Position;
import game.position.PositionOutOfBoundsException;

public class Artillery extends Weapon {

    private static final int MIN_RANGE = 2;

    @Override
    public boolean attack(Position origin, Position target, GameMap map) {
        if (origin.getDiagonalDistance(target) < MIN_RANGE || !origin.getDirectionTo(target).isValidDirection()) {
            throw new IllegalArgumentException("Artillery can only target tiles in a straight line at distance 2 or more.");
        }

        return attack(target, map);
    }

    @Override
    public boolean attack(Position target, GameMap map) {
        try {
            map.damageCellAt(target, 1);
            map.pushAllAdjacent(target);
            return true;
        } catch (PositionOutOfBoundsException e) {
            throw new RuntimeException(e); // TODO: Catch corrctly.
        }
    }
}
