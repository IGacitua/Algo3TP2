package game.units.weapons;

import game.map.GameMap;
import game.position.Position;
import game.position.direction.OrthogonalDirection;

public abstract class LineWeapon extends Weapon {

    protected LineWeapon(GameMap map, int damage) {
        super(map, damage);
    }

    @Override
    public boolean attack(Position origin, Position target) {
        OrthogonalDirection fireDirection = origin.getDirectionTo(target);
        return fireAlongLine(origin, fireDirection);
    }

    @Override
    public boolean attack(Position target) {
        throw new UnsupportedOperationException(
                getClass().getSimpleName() + " requires an origin position to determine the firing direction.");
    }

    protected boolean fireAlongLine(Position origin, OrthogonalDirection fireDirection) {
        Position current = origin.getAdjacent(fireDirection);
        Position found = map.findFirstObstacle(current, fireDirection);

        if (found == null) {
            return false; // TODO: Cambiar a que devuelva una excepción y hacer try/catch
        }

        map.attackCell(found, damage);
        onHit(found, fireDirection);

        return true;
    }

    protected abstract void onHit(Position hit, OrthogonalDirection fireDirection);
}