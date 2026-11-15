package game.map;

import game.SingletonNotInitializedException;
import game.map.cell.Building;
import game.map.cell.Cell;
import game.map.cell.Water;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.map.direction.*;
import game.units.Unit;
import game.units.weapons.Weapon;

import java.util.List;
import java.util.function.Predicate;

public class GameMap {
    private static final GameMap instance = new GameMap();

    private boolean initialized = false; // TODO: Temporary solution. Eventually I'd like to get rid of the singleton access.
    private CellGrid grid;

    private GameMap() {
    }

    public static GameMap getInstance() {
        return instance;
    }

    public void initialize(MapImporter importer, String mapFilePath) throws InvalidMapFileException {
        grid = new CellGrid(importer.importMap(mapFilePath));
        initialized = true;
    }

    public boolean tryMoveUnit(Position2D origin, Position2D destination) throws PositionOutOfBoundsException {
        ensureInitialized();
        Cell originCell = getCell(origin);
        Cell destinationCell = getCell(destination);

        Unit unitToMove = originCell.popUnit();
        if (unitToMove != null) {
            return destinationCell.tryPlaceUnit(unitToMove, true);
        }
        return false;
    }

    public boolean tryPlaceUnit(Unit unit, Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return getCell(position).tryPlaceUnit(unit, false);
    }

    /// Temporary method for testing.
    public void placeBuilding(Position2D position) {
        ensureInitialized();
        grid.replaceCell(new Building(position), position);
    }

    public Cell getCell(Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return grid.getCell(position);
    }

    public void applyDamage(Position2D target, int damage) throws PositionOutOfBoundsException {
        ensureInitialized();
        getCell(target).tryDamage(damage);
    }

    public void pushUnit(Position2D unit, OrthogonalDirection direction) throws PositionOutOfBoundsException {
        ensureInitialized();

        Cell fromCell = getCell(unit);
        Cell toCell = getCell(unit.getAdjacent(direction));

        Unit unitToPush = fromCell.popUnit();
        if (unitToPush != null) {
            toCell.tryPlaceUnit(unitToPush, true);
        }
    }

    public void pushAllAdjacent(Position2D target) {
        ensureInitialized();

        pushAway(target, new North());
        pushAway(target, new South());
        pushAway(target, new East());
        pushAway(target, new West());
    }

    private void pushAway(Position2D target, OrthogonalDirection direction) {
        try {
            Position2D adjacent = target.getAdjacent(direction);
            pushUnit(adjacent, direction);
        } catch (PositionOutOfBoundsException e) {
            // Skip cells that are out of bounds.
        }
    }

    public boolean hasMech(Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return getCell(position).hasMech();
    }

    public boolean hasBuilding(Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return getCell(position).getClass() == Building.class; // TODO: Same as hasMech().
    }

    public boolean hasWater(Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return getCell(position).getClass() == Water.class; // TODO: Same as hasMech().
    }

    private void ensureInitialized() {
        if (!initialized) {
            throw new SingletonNotInitializedException(GameMap.class);
        }
    }

    public List<Position2D> getReachablePositions(Unit unit) {
        Position2D unitPosition = unit.getPosition();
        int movementDistance = unit.getMovementDistance();
        Predicate<Cell> canMoveTo = (c) -> c.canPlaceUnit(unit, false);

        return grid.getCellsInRadius(unitPosition, movementDistance, canMoveTo);
    }

    // Devuelve la coordenada de un Edificio si el arma lo alcanza disparando desde 'origin'.
    public Position2D findAttackableBuildingFrom(Position2D origin, Weapon weapon) {
        Predicate<Cell> isBuilding = (c) -> c.getClass() == Building.class; // TODO: Same as hasBuilding().
        Predicate<Cell> canAttack = Cell::canDamage;

        return grid.getNearestCell(origin, isBuilding.and(canAttack));
    }

    // Devuelve la coordenada de un Mech si el arma lo alcanza disparando desde 'origin'.
    public Position2D findAttackableMechFrom(Position2D origin, Weapon weapon) {
        Predicate<Cell> hasMech = Cell::hasMech;
        Predicate<Cell> canAttack = Cell::canDamage;

        return grid.getNearestCell(origin, hasMech.and(canAttack));
    }

    // Calcula la distancia Manhattan desde 'pos' hasta el edificio más cercano del tablero.
    public double distanceToClosestBuildingFrom(Position2D pos) {
        Predicate<Cell> isBuilding = (c) -> c.getClass() == Building.class; // TODO: Same as hasBuilding().
        return grid.getDistanceToNearestCell(pos, isBuilding);
    }

}
