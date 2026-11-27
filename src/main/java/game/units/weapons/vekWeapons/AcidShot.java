package game.units.weapons.vekWeapons;

import game.map.GameMap;
import game.position.Position;
import game.position.direction.OrthogonalDirection;
import game.units.weapons.LineWeapon;

public class AcidShot extends LineWeapon {

    private static final int DAMAGE = 1;

    @Override
    protected void onHit(GameMap map, Position hit, OrthogonalDirection fireDirection) {
        map.attackCell(hit, DAMAGE);
        map.contaminateCell(hit);
    }
}