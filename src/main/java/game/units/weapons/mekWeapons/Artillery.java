package game.units.weapons.mekWeapons;

import game.map.GameMap;
import game.position.Position;
import game.units.weapons.Weapon;

public class Artillery implements Weapon {

    private static final int DAMAGE = 1;
    private static final int MIN_RANGE = 2;

    @Override
    public boolean attack(GameMap map, Position origin, Position target) {
        if (origin.getDiagonalDistance(target) < MIN_RANGE) {
            throw new IllegalArgumentException("Artillery can only target tiles in a straight line at distance 2 or more.");
        }

        map.attackCell(target, DAMAGE);
        map.pushAllAdjacent(target);
        return true;
    }

}
