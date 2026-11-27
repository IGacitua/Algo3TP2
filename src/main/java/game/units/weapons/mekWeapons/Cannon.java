package game.units.weapons.mekWeapons;

import game.map.GameMap;
import game.position.Position;
import game.position.direction.OrthogonalDirection;
import game.units.weapons.LineWeapon;

public class Cannon extends LineWeapon {

    private static final int DAMAGE = 1;

    public Cannon(GameMap map) {
        super(map, DAMAGE);
    }

    @Override
    protected void onHit(Position hit, OrthogonalDirection fireDirection) {
        map.pushUnit(hit, fireDirection);
    }
}