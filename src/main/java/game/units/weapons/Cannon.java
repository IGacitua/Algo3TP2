package game.units.weapons;

import game.map.GameMap;
import game.position.Position;
import game.position.direction.OrthogonalDirection;

public class Cannon extends Weapon {

    private static final int DAMAGE = 1;

    public Cannon(GameMap map) {
        super(map, DAMAGE);
    }

    @Override
    public boolean attack(Position origin, Position target) {
        OrthogonalDirection fireDirection = origin.getDirectionTo(target);
        return fireAlongLine(origin, fireDirection);
    }

    @Override
    public boolean attack(Position target) {
        throw new UnsupportedOperationException("Cannon requires an origin position to determine the firing direction.");
    }

    private boolean fireAlongLine(Position origin, OrthogonalDirection fireDirection) {
        Position current = origin.getAdjacent(fireDirection);
        Position found = map.findFirstObstacle(current, fireDirection);

        if (found == null) {
            return false; // TODO: Cambiar a que devuelva una excepción y hacer try/catch
        }

        map.attackCell(found, DAMAGE);
        map.pushUnit(found, fireDirection);

        return true;
    }

}
