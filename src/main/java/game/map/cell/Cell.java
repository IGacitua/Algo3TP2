package game.map.cell;

import game.map.Position2D;
import game.units.Unit;

public class Cell {
    protected final Position2D position;
    protected UnitSlot unitSlot;

    public Cell(Position2D position) {
        this.position = position;
        unitSlot = createUnitSlot();
    }

    protected UnitSlot createUnitSlot() {
        return new UnitSlot(1);
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
        return unitSlot.hasUnit();
    }

    protected void onDamaged(int damage) {
        unitSlot.damage(damage);
    }

    public boolean tryPlaceUnit(Unit unit, boolean pushed) {
        if (!canPlaceUnit(unit, pushed)) {
            return false;
        }

        unit.moveTo(position);
        unitSlot.tryPlace(unit);

        onUnitPlaced(unit, pushed);
        return true;
    }

    public boolean canPlaceUnit(Unit unit, boolean pushed) {
        return !unitSlot.isFull();
    }

    protected void onUnitPlaced(Unit unit, boolean pushed) {
    }

    public Unit popUnit() {
        return unitSlot.popUnit();
    }

    public boolean hasPosition(Position2D position) {
        return this.position.getDistance(position) == 0;
    }

    public boolean hasUnit() {
        return unitSlot.hasUnit();
    }

    public boolean hasMech() {
        return unitSlot.hasUnit(Unit::isMech);
    }

    public boolean isVekTarget() {
        return false;
    }

    public boolean isWater() {
        return false;
    }
}
