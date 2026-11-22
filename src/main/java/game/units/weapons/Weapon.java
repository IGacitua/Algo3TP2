package game.units.weapons;

import game.map.GameMap;
import game.position.Position;


public abstract class Weapon {

    // TODO: En lugar de recibir mapa como parametro, recibirlo en el constructor.

    public boolean attack(Position origin, Position target, GameMap map) {
        return attack(target, map);
    }

    public abstract boolean attack(Position target, GameMap map);
}
