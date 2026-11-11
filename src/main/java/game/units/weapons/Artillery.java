package game.units.weapons;

import game.map.GameMap;
import game.map.Position2D;
import game.map.direction.East;
import game.map.direction.North;
import game.map.direction.South;
import game.map.direction.West;

public class Artillery extends Weapon {

    @Override
    public void attack(Position2D target) {

        GameMap.getInstance().damage(target);

        // TODO: Everything here could be delegated to GameMap.
        Position2D north = target.getAdjacent(new North());
        Position2D east = target.getAdjacent(new East());
        Position2D south = target.getAdjacent(new South());
        Position2D west = target.getAdjacent(new West());

        GameMap.getInstance().pushUnit(north, new North());
        GameMap.getInstance().pushUnit(east, new East());
        GameMap.getInstance().pushUnit(south, new South());
        GameMap.getInstance().pushUnit(west, new West());
    }
}
