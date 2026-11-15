package game.map.cell;

import game.map.Position2D;
import game.units.Unit;

public class Water extends Cell {

    public Water(Position2D position) {
        super(position);
    }

    @Override
    public String cellTypeId() {
        return "W";
    }

    @Override
    public boolean canPlaceUnit(Unit unit, boolean pushed) {
        return super.canPlaceUnit(unit, pushed) && (unit.canMoveToWater() || pushed);
    }

    @Override
    protected void onUnitPlaced(Unit unit, boolean pushed) {
        if (pushed && !unit.canMoveToWater()) {
            unit.kill();
        }
    }
}
