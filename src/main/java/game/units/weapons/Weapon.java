package game.units.weapons;

import game.map.Position2D;
import game.units.Unit;

public abstract class Weapon {
    public abstract void attack(Position2D target);
}
