package game.map;

import game.electricalNetwork.ElectricalNetwork;
import game.units.Unit;

public class Cell {
    private Unit containedUnit;

    Position2D position;
    boolean isBuilding;
    boolean isDamaged;

    public Cell(Position2D position) {
        containedUnit = null;
        isBuilding = false;
        isDamaged = false;
        this.position = position;
    }

    public void damage(){
        isDamaged = true;
        if (isBuilding){
            ElectricalNetwork.getInstance().reducePower();
        }
        containedUnit.damage();
    }

    public boolean isDamaged(){
        return isDamaged;
    }

    public void makeBuilding() {
        isBuilding = true;
    }

    public boolean placeUnit(Unit unit, boolean pushed) {
        if (containedUnit == null) {
            unit.moveTo(position);
            containedUnit = unit;
            if (isBuilding && pushed){
                damage();
            }
            return true;
        }
        return false;
    }

    public Unit popUnit(){
        Unit unit = containedUnit;
        unit.moveTo(new Position2D(-404, -404));
        containedUnit = null;
        return unit;
    }
}
