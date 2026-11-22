package game.units.weapons;

import game.map.GameMap;
import game.position.Position;


public abstract class Weapon {

    // TODO: En lugar de recibir mapa como parametro, recibirlo en el constructor.

    public void attack(Position origin, Position target, GameMap map) {
        attack(target, map);
    }

    public abstract void attack(Position target, GameMap map);
}
