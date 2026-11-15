package game.units.weapons;

import game.map.GameMap;
import game.map.Position2D;


public abstract class Weapon {

    public void attack(Position2D origin, Position2D target, GameMap map) {
        attack(target, map);
    }
    
    public abstract void attack(Position2D target, GameMap map);
}
