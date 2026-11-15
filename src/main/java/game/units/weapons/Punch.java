package game.units.weapons;

import game.map.GameMap;
import game.map.Position2D;
import game.map.PositionOutOfBoundsException;
import game.map.cell.Cell;
import game.map.direction.East;
import game.map.direction.NoDirection;
import game.map.direction.North;
import game.map.direction.OrthogonalDirection;
import game.map.direction.South;
import game.map.direction.West;

public class Punch extends Weapon {

    @Override
    public void attack(Position2D target, GameMap map) {
        OrthogonalDirection pushDirection = getPushDirection(target);

        if (pushDirection instanceof NoDirection) {
            throw new IllegalArgumentException("Punch can only target an orthogonally adjacent tile.");
        }

        try {
            GameMap.getInstance().damage(target);
            GameMap.getInstance().damage(target);
        } catch (PositionOutOfBoundsException e) {
            throw new RuntimeException(e);
        }

        try {
            GameMap.getInstance().pushUnit(target, pushDirection);
        } catch (PositionOutOfBoundsException ignored) {
            // Si la casilla destino del empuje cae fuera del mapa, la unidad permanece en el borde
        }
    }

    private OrthogonalDirection getPushDirection(Position2D target) {
        Position2D north = target.getAdjacent(new North());
        Position2D east = target.getAdjacent(new East());
        Position2D south = target.getAdjacent(new South());
        Position2D west = target.getAdjacent(new West());

        if (hasUnitAt(north)) {
            return new South();
        }
        if (hasUnitAt(east)) {
            return new West();
        }
        if (hasUnitAt(south)) {
            return new North();
        }
        if (hasUnitAt(west)) {
            return new East();
        }

        return new NoDirection();
    }

    private boolean hasUnitAt(Position2D position) {
        try {
            Cell cell = GameMap.getInstance().getCell(position);
            return cell != null && cell.hasUnit();
        } catch (PositionOutOfBoundsException e) {
            return false;
        }
    }
}
