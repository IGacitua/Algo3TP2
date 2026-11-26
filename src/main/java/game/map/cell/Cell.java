package game.map.cell;

import game.position.Position;
import game.units.Unit;

public class Cell {
    protected final Position position;
    protected UnitSlot unitSlot;

    public Cell(Position position) {
        this.position = position;
        unitSlot = createUnitSlot();
    }

    protected UnitSlot createUnitSlot() {
        return new UnitSlot(1);
    }

    @Override
    public String toString() {
        return "Ground " + position.toString();
    }

    public String cellTypeId() {
        return "G";
    }

    // Damage

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

    // Unit

    public boolean tryPlaceUnit(Unit unit, boolean pushed) {
        if (!canPlaceUnit(unit, pushed)) {
            return false;
        }

        if (unitSlot.tryPlace(unit)) {
            onUnitPlaced(unit, pushed);
            return true;
        }

        return false;
    }

    public boolean isFull() {
        return unitSlot.isFull();
    }

    public boolean canPlaceUnit(Unit unit, boolean pushed) {
        return !isFull();
    }

    protected void onUnitPlaced(Unit unit, boolean pushed) {
    }

    public boolean moveUnit(Cell destination, boolean pushed, double consumptionMultiplier) {
        if (destination.isFull()) {
            return false;
        }

        Unit unit = unitSlot.popUnit();
        if (unit == null) {
            return false;
        }

        if (!unit.canMoveTo(destination, pushed, consumptionMultiplier) || !destination.tryPlaceUnit(unit, pushed)) {
            unitSlot.tryPlace(unit); // Put it back without triggering onUnitPlaced.
            return false;
        }

        if (!pushed) { // Pushes are free.
            unit.consumeMovement(consumptionMultiplier);
        }

        return true;
    }

    public Unit peekUnit() {
        return unitSlot.peekUnit();
    }

    public Unit popUnit() {
        return unitSlot.popUnit();
    }

    public boolean hasPosition(Position position) {
        return this.position.getDiagonalDistance(position) == 0;
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

    public boolean blocksShot() {
        return hasUnit();
    }
}
