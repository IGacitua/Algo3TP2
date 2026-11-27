package game.map.cell;

import game.position.Position;
import game.units.Unit;

public class Water extends Cell {

    public Water(Position position) {
        super(position);
    }

    @Override
    public String toString() {
        return "Water " + position.toString();
    }

    @Override
    public String cellTypeId() {
        return "W";
    }

    @Override
    public boolean canPlaceUnit(Unit unit, boolean pushed) {
        if (!unit.canMoveToWater(pushed)) {
            return false;
        }

        return super.canPlaceUnit(unit, pushed);
    }

    @Override
    protected void onUnitPlaced(Unit unit, boolean pushed) {
        if (unit.diesOnWater()) {
            unit.kill();
        }
    }

    @Override
    public boolean hasUnitThatCanAttack() {
        return hasUnit(Unit::canAttackFromWater);
    }

}
