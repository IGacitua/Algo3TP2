package game.map;

import game.map.cell.Building;
import game.map.cell.GenericCell;
import game.units.Unit;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private static final GameMap instance = new GameMap();

    private List<GenericCell> cells;

    private GameMap() {
        Initialize();
    }

    public static GameMap getInstance() {
        return instance;
    }

    private void Initialize() {
        cells = new ArrayList<>();

        cells.add(new GenericCell(new Position2D(0, 0)));
        cells.add(new GenericCell(new Position2D(0, 1)));
        cells.add(new GenericCell(new Position2D(0, 2)));
        cells.add(new GenericCell(new Position2D(1, 0)));
        cells.add(new GenericCell(new Position2D(2, 0)));
    }

    public boolean placeUnit(Unit unit, Position2D position) {
        GenericCell cell = getCell(position);
        if (cell != null) {
            return cell.tryPlaceUnit(unit, false);
        }
        return false;
    }

    public void placeBuilding(Position2D position) {
        GenericCell cell = getCell(position);
        if (cell != null) {
            cells.remove(cell);
        }
        cells.add(new Building(position));
    }

    public GenericCell getCell(Position2D position) {
        for (GenericCell cell : cells) {
            if (cell.hasPosition(position)) {
                return cell;
            }
        }
        return null;
    }

    public void damage(Position2D target) {
        GenericCell cell = getCell(target);
        if (cell != null) {
            cell.tryDamage();
        }
    }

    public void push(Position2D from, Position2D to) {
        GenericCell fromCell = getCell(from);
        GenericCell toCell = getCell(to);

        if (fromCell != null && toCell != null) {
            Unit unitToPush = fromCell.popUnit();
            if (unitToPush != null) {
                toCell.tryPlaceUnit(unitToPush, true);
            }
        }
    }
}
