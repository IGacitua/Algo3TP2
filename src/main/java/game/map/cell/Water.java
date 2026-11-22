package game.map.cell;

import game.position.Position;
import game.units.Unit;

public class Water extends Cell {

    public Water(Position position) {
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

    @Override
    public boolean isWater() {
        return true;
    }
}
