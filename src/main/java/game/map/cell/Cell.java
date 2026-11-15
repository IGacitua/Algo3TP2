package game.map.cell;

import game.map.Position2D;
import game.units.Unit;

public class Cell {
    protected final Position2D position;
    protected Unit containedUnit;

    public Cell(Position2D position) {
        containedUnit = null;
        this.position = position;
    }

    public String cellTypeId() {
        return "G";
    }

    public void tryDamage(int damage) {
        if (!canDamage()) {
            return;
        }

        this.onDamaged(damage);
    }

    public boolean canDamage() {
        return containedUnit != null;
    }

    protected void onDamaged(int damage) {
        if (containedUnit != null) {
            containedUnit.damage(damage);
        }
    }

    public boolean tryPlaceUnit(Unit unit, boolean pushed) {
        if (!canPlaceUnit(unit, pushed)) {
            return false;
        }

        unit.moveTo(position);
        containedUnit = unit;

        onUnitPlaced(unit, pushed);
        return true;
    }

    public boolean canPlaceUnit(Unit unit, boolean pushed) {
        return containedUnit == null;
    }

    protected void onUnitPlaced(Unit unit, boolean pushed) {
    }

    public Unit popUnit() {
        Unit unit = containedUnit;
        containedUnit = null;
        return unit;
    }

    public boolean hasPosition(Position2D position) {
        return this.position.getDistance(position) == 0;
    }

    public boolean hasUnit() {
        return containedUnit != null;
    }

    public boolean hasMech() {
        return hasUnit() && containedUnit.getClass() == game.units.Mech.class; // TODO: No deberiamos usar .getClass();
    }
}
