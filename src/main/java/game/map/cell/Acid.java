package game.map.cell;

import game.map.Position2D;
import game.units.Unit;

public class Acid extends GenericCell {

    public Acid(Position2D position) {
        super(position);
    }

    @Override
    protected void onUnitPlaced(Unit unit, boolean pushed) {
        unit.contaminate();
    }

}
