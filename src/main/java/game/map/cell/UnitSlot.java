package game.map.cell;

import game.units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class UnitSlot {
    private final int maximumUnits;
    private final List<Unit> containedUnits;

    public UnitSlot(int maximumUnits) {
        this.maximumUnits = maximumUnits;
        containedUnits = new ArrayList<>();
    }

    public boolean tryPlace(Unit unit) {
        if (isFull()) {
            return false;
        }
        containedUnits.add(unit);
        return true;
    }

    public Unit peekUnit() {
        if (containedUnits.isEmpty()) {
            return null;
        }
        return containedUnits.get(containedUnits.size() - 1);
    }

    public Unit popUnit() {
        if (containedUnits.isEmpty()) {
            return null;
        }
        return containedUnits.remove(containedUnits.size() - 1);
    }

    public boolean isFull() {
        return containedUnits.size() >= maximumUnits;
    }

    public void damage(int damage) {
        for (Unit unit : containedUnits) {
            unit.damage(damage);
        }
    }

    public boolean hasUnit() {
        return !containedUnits.isEmpty();
    }

    public boolean hasUnit(Predicate<Unit> predicate) {
        for (Unit unit : containedUnits) {
            if (predicate.test(unit)) {
                return true;
            }
        }
        return false;
    }
}
