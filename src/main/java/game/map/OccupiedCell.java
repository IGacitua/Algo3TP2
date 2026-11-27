package game.map;

import game.position.Position;
import game.units.Unit;

public class OccupiedCell {
    private final Position position;
    private final Unit unit;
    
    public OccupiedCell(Position position, Unit unit) {
        this.position = position;
        this.unit = unit;
    }

    public Position getPosition() {
        return position;
    }

    public Unit getUnit() {
        return unit;
    }
    
}
