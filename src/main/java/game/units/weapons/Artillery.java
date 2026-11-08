package game.units.weapons;

import game.map.GameMap;
import game.map.Position2D;
import game.units.Unit;

public class Artillery extends Weapon{

    @Override
    public void attack(Position2D target) {
        GameMap.getInstance().damage(target);

        Position2D north = new Position2D(target.getX(), target.getY() + 1);
        Position2D newNorth = new Position2D(target.getX(), target.getY() + 2);

        Position2D east = new Position2D(target.getX() + 1, target.getY());
        Position2D newEast = new Position2D(target.getX() + 2, target.getY());

        Position2D south = new Position2D(target.getX(), target.getY() - 1);
        Position2D newSouth = new Position2D(target.getX(), target.getY() - 2);

        Position2D west = new Position2D(target.getX() - 1, target.getY());
        Position2D newWest = new Position2D(target.getX() - 2, target.getY());

        GameMap.getInstance().push(north, newNorth);
        GameMap.getInstance().push(east, newEast);
        GameMap.getInstance().push(south, newSouth);
        GameMap.getInstance().push(west, newWest);
    }
}
