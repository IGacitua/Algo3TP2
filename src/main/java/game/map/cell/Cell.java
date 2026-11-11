package game.map.cell;

import game.map.Position2D;
import game.units.Unit;

public abstract class Cell {
    protected final Position2D position;
    protected Unit containedUnit;

    public Cell(Position2D position) {
        containedUnit = null;
        this.position = position;
    }

    public void tryDamage() {
        if (!canDamage()) {
            return;
        }

        this.onDamaged();
    }

    protected abstract boolean canDamage();

    protected void onDamaged() {
        if (containedUnit != null) {
            containedUnit.damage();
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

    protected boolean canPlaceUnit(Unit unit, boolean pushed) {
        return containedUnit == null;
    }

    protected abstract void onUnitPlaced(Unit unit, boolean pushed);

    public Unit popUnit() {
        Unit unit = containedUnit;
        containedUnit = null;
        onUnitRemoved(unit);
        return unit;
    }

    protected abstract void onUnitRemoved(Unit unit);

    public boolean hasPosition(Position2D position) {
        return this.position.getDistance(position) == 0;
    }
}
