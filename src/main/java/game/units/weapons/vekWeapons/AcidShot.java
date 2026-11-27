package game.units.weapons.vekWeapons;

import game.map.GameMap;
import game.position.Position;
import game.position.direction.OrthogonalDirection;
import game.units.weapons.LineWeapon;

public class AcidShot extends LineWeapon {

    private static final int DAMAGE = 1;

    public AcidShot(GameMap map) {
        super(map, DAMAGE);
    }

    @Override
    protected void onHit(Position hit, OrthogonalDirection fireDirection) {
        map.contaminateCell(hit);
    }
}