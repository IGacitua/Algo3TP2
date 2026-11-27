package game.units.weapons;

import game.map.GameMap;
import game.position.Position;

public interface Weapon {
    boolean attack(GameMap map, Position origin, Position target);
}
