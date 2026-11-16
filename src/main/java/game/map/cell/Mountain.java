package game.map.cell;

import game.position.Position2D;

public class Mountain extends Cell {

    public Mountain(Position2D position) {
        super(position);
    }

    protected UnitSlot createUnitSlot() {
        return new UnitSlot(0);
    }

    @Override
    public String cellTypeId() {
        return "M";
    }

    @Override
    public boolean canDamage() {
        return false;
    }
}
