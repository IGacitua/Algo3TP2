package game.units.weapons;

import game.map.GameMap;
import game.map.Position2D;
import game.map.PositionOutOfBoundsException;

public class Artillery extends Weapon {

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
