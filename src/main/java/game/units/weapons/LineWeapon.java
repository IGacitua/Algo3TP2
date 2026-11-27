package game.units.weapons;

import game.map.GameMap;
import game.position.Position;
import game.position.direction.OrthogonalDirection;

public abstract class LineWeapon implements Weapon {

    @Override
    public boolean attack(GameMap map, Position origin, Position target) {
        OrthogonalDirection fireDirection = origin.getDirectionTo(target);
        return fireAlongLine(map, origin, fireDirection);
    }

    protected boolean fireAlongLine(GameMap map, Position origin, OrthogonalDirection fireDirection) {
        Position current = origin.getAdjacent(fireDirection);
        Position found = map.findFirstObstacle(current, fireDirection);

        if (found == null) {
            return false; // TODO: Cambiar a que devuelva una excepción y hacer try/catch
        }

        onHit(map, found, fireDirection);
        return true;
    }

    protected abstract void onHit(GameMap map, Position hit, OrthogonalDirection fireDirection);
}