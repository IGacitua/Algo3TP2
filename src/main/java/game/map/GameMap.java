package game.map;

import game.units.Unit;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private static final GameMap instance = new GameMap();

    private List<Cell> cells;

    public static GameMap getInstance(){
        return instance;
    }

    private GameMap(){
        Initialize();
    }

    private void Initialize(){
        cells = new ArrayList<>();

        cells.add(new Cell(new Position2D(0,0)));
        cells.add(new Cell(new Position2D(0,1)));
        cells.add(new Cell(new Position2D(0,2)));
        cells.add(new Cell(new Position2D(1,0)));
        cells.add(new Cell(new Position2D(2,0)));
    }

    public boolean placeUnit(Unit unit, Position2D position){
        Cell cell = getCell(position);
        if (cell != null){
            return cell.placeUnit(unit, false);
        }
        return false;
    }

    public void placeBuilding(Position2D position){
        Cell cell = getCell(position);
        cell.makeBuilding();
    }

    public boolean isDamaged(Position2D position){
        Cell cell = getCell(position);
        if (cell != null){
            return cell.isDamaged();
        }
        return false;
    }

    public Cell getCell(Position2D position) {
        for(Cell cell : cells){
            if (cell.position.getDistance(position) == 0){
                return cell;
            }
        }
        return null;
    }

    public void damage(Position2D target) {
        Cell cell = getCell(target);
        if (cell != null) {
            cell.damage();
        }
    }

    public void push(Position2D from, Position2D to) {
        Cell fromCell = getCell(from);
        Cell toCell = getCell(to);

        if (fromCell != null && toCell != null) {
            Unit unitToPush = fromCell.popUnit();
            if (unitToPush != null) {
                toCell.placeUnit(unitToPush, true);
            }
        }
    }
}
