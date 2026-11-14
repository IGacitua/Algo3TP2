package game.units.actions;

import game.map.GameMap;
import game.map.Position2D;
import game.units.Unit;

public class MoveAction extends UnitAction {
    private final Unit unit;
    private final Position2D origin;
    private final Position2D destination;

    public MoveAction(Unit unit, Position2D origin, Position2D destination) {
        this.unit = unit;
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Execute the move action by attempting to place the unit at the destination cell.
     * @throws IllegalStateException if the unit cannot be placed at the destination cell.
     */
    @Override
    public void execute() {
        if (!GameMap.getInstance().tryPlaceUnit(this.unit, this.destination)) { // TODO: probablemente hay que usar otro metodo para mover la unidad.
            throw new IllegalStateException("Unit cannot be placed at destination: " + this.destination);
        }
        this.unit.moveTo(this.destination);
    }
    
    /**
     * Undo the move action by placing the unit back at its original position.
     * @throws IllegalStateException if the unit cannot be placed back at the original position.
     */
    public void undo() {
        if (!GameMap.getInstance().tryPlaceUnit(this.unit, this.origin)) {
            throw new IllegalStateException("Unit cannot be placed at destination: " + this.origin);
        }

        this.unit.moveTo(this.origin);
    }
}
