package game.map.cell;

import game.map.Position2D;
import game.units.Unit;

public class Mountain extends Cell {

    public Mountain(Position2D position) {
        super(position);
    }

    @Override
    public String cellTypeId() {
        return "M";
    }

    @Override
    public boolean canDamage() {
        return false;
    }

    @Override
    public boolean canPlaceUnit(Unit unit, boolean pushed) {
        return false;
    }
}
