package game.map.cell;

import game.map.Position2D;
import game.units.Unit;

public class Mountain extends Cell {

    public Mountain(Position2D position) {
        super(position);
    }

    @Override
    protected boolean canDamage() {
        return false;
    }

    @Override
    protected boolean canPlaceUnit(Unit unit, boolean pushed) {
        return false;
    }

    @Override
    protected void onUnitPlaced(Unit unit, boolean pushed) {
    }

    @Override
    protected void onUnitRemoved(Unit unit) {
    }
}
