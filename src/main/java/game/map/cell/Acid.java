package game.map.cell;

import game.position.Position;
import game.units.Unit;

public class Acid extends Cell {

    public Acid(Position position) {
        super(position);
    }

    @Override
    public String cellTypeId() {
        return "A";
    }

    @Override
    protected void onUnitPlaced(Unit unit, boolean pushed) {
        unit.contaminate();
    }

}
