package game.units.weapons;

import game.map.GameMap;
import game.map.Position2D;
import game.map.PositionOutOfBoundsException;
import game.map.direction.NoDirection;

public class Artillery extends Weapon {

    private static final int MIN_RANGE = 2;

    @Override
    public void attack(Position2D origin, Position2D target) {
        if (origin.getDistance(target) < MIN_RANGE || origin.getDirectionTo(target) instanceof NoDirection) {
            throw new IllegalArgumentException("Artillery can only target tiles in a straight line at distance 2 or more.");
        }

        attack(target);
    }

    @Override
    public void attack(Position2D target) {

        try {
            GameMap.getInstance().damage(target);
        } catch (PositionOutOfBoundsException e) {
            throw new RuntimeException(e); // TODO: Catch corrctly.
        }

        GameMap.getInstance().pushAllAdjacent(target);
    }
}
