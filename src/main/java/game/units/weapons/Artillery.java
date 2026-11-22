package game.units.weapons;

import game.map.GameMap;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.NoDirection;

public class Artillery extends Weapon {

    private static final int MIN_RANGE = 2;

    @Override
    public void attack(Position origin, Position target, GameMap map) {
        if (origin.getDiagonalDistance(target) < MIN_RANGE || origin.getDirectionTo(target) instanceof NoDirection) {
            throw new IllegalArgumentException("Artillery can only target tiles in a straight line at distance 2 or more.");
        }

        attack(target, map);
    }

    @Override
    public void attack(Position target, GameMap map) {
        try {
            map.damageCellAt(target, 1);
            map.pushAllAdjacent(target);
        } catch (PositionOutOfBoundsException e) {
            throw new RuntimeException(e); // TODO: Catch corrctly.
        }
    }
}
