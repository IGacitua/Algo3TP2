package game.units.weapons;

import game.map.GameMap;
import game.position.Position;
import game.position.direction.OrthogonalDirection;

public class AcidShot extends Weapon {

    private static final int DAMAGE = 1;

    public AcidShot(GameMap map) {
        super(map, DAMAGE);
    }

    @Override
    public boolean attack(Position origin, Position target) {
        OrthogonalDirection fireDirection = origin.getDirectionTo(target);
        Position current = origin.getAdjacent(fireDirection);
        Position found = map.findFirstObstacle(current, fireDirection);

        if (found == null) {
            return false;
        }

        map.attackCell(found, DAMAGE);
        map.getCellAt(found).contaminateUnit();

        return true;
    }

    @Override
    public boolean attack(Position target) {
        throw new UnsupportedOperationException(
                "AcidShot requires an origin position.");
    }
}