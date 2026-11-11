package game.map.cell;

import game.map.Position2D;
import game.units.Unit;

public class Acid extends Cell {

    public Acid(Position2D position) {
        super(position);
    }

    @Override
    protected boolean canDamage() {
        return containedUnit != null;
    }


    @Override
    protected void onUnitPlaced(Unit unit, boolean pushed) {
        unit.contaminate();
    }

    @Override
    protected void onUnitRemoved(Unit unit) {
        // This is a safeguard, in case the unit is somehow *not* contaminated when placed.
        unit.contaminate();
    }
}
