package game.map.cell;

import game.map.Position2D;
import game.units.Unit;

public class Ground extends Cell {

    public Ground(Position2D position) {
        super(position);
    }

    @Override
    protected boolean canDamage() {
        return containedUnit != null;
    }

    @Override
    protected void onUnitPlaced(Unit unit, boolean pushed) {
    }

    @Override
    protected void onUnitRemoved(Unit unit) {
    }
}
