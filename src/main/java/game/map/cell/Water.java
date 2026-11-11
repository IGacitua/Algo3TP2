package game.map.cell;

import game.map.Position2D;
import game.units.Unit;

public class Water extends Cell {

    public Water(Position2D position) {
        super(position);
    }

    @Override
    protected boolean canDamage() {
        return containedUnit != null;
    }

    @Override
    protected boolean canPlaceUnit(Unit unit, boolean pushed) {
        return super.canPlaceUnit(unit, pushed) && unit.canMoveToWater() || pushed;
    }

    @Override
    protected void onUnitPlaced(Unit unit, boolean pushed) {
    }

    @Override
    protected void onUnitRemoved(Unit unit) {
    }
}
