package game.units.weapons;

import game.map.GameMap;
import game.map.Position2D;
import game.map.PositionOutOfBoundsException;
import game.map.direction.NoDirection;

public class Artillery extends Weapon {

    private static final int MIN_RANGE = 2;

    @Override
    public void attack(Position2D origin, Position2D target, GameMap map) {
        if (origin.getDistance(target) < MIN_RANGE || origin.getDirectionTo(target) instanceof NoDirection) {
            throw new IllegalArgumentException("Artillery can only target tiles in a straight line at distance 2 or more.");
        }

        attack(target, map);
    }

    @Override
    public void attack(Position2D target, GameMap map) {
        try {
            GameMap.getInstance().applyDamage(target, 1);
        } catch (PositionOutOfBoundsException e) {
            throw new RuntimeException(e); // TODO: Catch corrctly.
        }

        GameMap.getInstance().pushAllAdjacent(target);
    }
}
