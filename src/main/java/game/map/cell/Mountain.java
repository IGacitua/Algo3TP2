package game.map.cell;

import game.position.Position;

public class Mountain extends Cell {

    public Mountain(Position position) {
        super(position);
    }

    protected UnitSlot createUnitSlot() {
        return new UnitSlot(0);
    }

    @Override
    public String toString() {
        return "Mountain " + position.toString();
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
