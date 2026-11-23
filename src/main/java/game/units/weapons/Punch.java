package game.units.weapons;

import game.map.GameMap;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.position.direction.OrthogonalDirection;

public class Punch extends Weapon {

    private static final int DAMAGE = 2;

    @Override
    public boolean attack(Position origin, Position target, GameMap map) {
        if (origin.getOrthogonalDistance(target) != 1.0) {
            throw new IllegalArgumentException("Punch can only target an orthogonally adjacent tile.");
        }

        OrthogonalDirection pushDirection = origin.getDirectionTo(target);
        if (!pushDirection.isValidDirection()) {
            throw new IllegalArgumentException("Punch can only target an orthogonally adjacent tile.");
        }

        return applyPunch(target, pushDirection, map);
    }

    @Override
    public boolean attack(Position target, GameMap map) {
        throw new UnsupportedOperationException("Punch requires an origin position to determine the hit direction.");
    }

    private boolean applyPunch(Position target, OrthogonalDirection pushDirection, GameMap map) {
        try {
            map.damageCellAt(target, DAMAGE);
        } catch (PositionOutOfBoundsException e) {
            throw new RuntimeException(e);
        }

        try {
            map.pushUnitTowards(target, pushDirection);
        } catch (PositionOutOfBoundsException ignored) {
            // Si la casilla destino del empuje cae fuera del mapa, la unidad permanece en el borde
        }

        return true;
    }
}
