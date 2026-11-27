package game.map.cell;

import game.position.Position;
import game.units.Unit;

import java.util.function.Predicate;

public class Cell {
    protected final Position position;
    protected final UnitSlot unitSlot;

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

    public void contaminateUnit() {
        Unit unit = unitSlot.peekUnit();

        if (unit != null) {
            unit.contaminate();
        }
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

        // Separated from if for easier debugging
        boolean canMoveTo = unit.canMove(pushed, consumptionMultiplier);

        if (!canMoveTo || !destination.tryPlaceUnit(unit, pushed)) {
            unitSlot.tryPlace(unit); // Put it back without triggering onUnitPlaced.
            return false;
        }

        if (!pushed) { // Pushes are free.
            unit.reduceMovement(consumptionMultiplier);
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

    public boolean hasUnit(Predicate<Unit> condition) {
        return unitSlot.hasUnit(condition);
    }

    public boolean isVekTarget() {
        return false;
    }

    public boolean hasUnitThatCanAttack() {
        return hasUnit();
    }
}
