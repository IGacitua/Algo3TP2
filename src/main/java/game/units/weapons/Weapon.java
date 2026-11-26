package game.units.weapons;

import game.map.GameMap;
import game.position.Position;


public abstract class Weapon {

    protected GameMap map;
    protected int damage;

    public Weapon(GameMap map, int damage) {
        this.map = map;
        this.damage = damage;
    }

    // TODO: En lugar de recibir mapa como parametro, recibirlo en el constructor.

    public abstract boolean attack(Position target);

    public abstract boolean attack(Position origin, Position target);
}
