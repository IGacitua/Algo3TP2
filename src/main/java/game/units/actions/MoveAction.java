package game.units.actions;

import game.map.GameMap;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Unit;


public class MoveAction implements UnitAction {
    private final Unit unit;
    private final Position origin;
    private final Position destination;

    public MoveAction(Position origin, Position destination) {
        this(null, origin, destination);
    }
    
    public MoveAction(Unit unit, Position origin, Position destination) {
        this.unit = unit;
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Execute the move action by moving the unit to the destination cell.
     *
     * @param map The game map where the action is executed.
     */
    @Override
    public boolean tryExecute(GameMap map) throws PositionOutOfBoundsException {
        return map.moveUnitTo(this.origin, this.destination);
    }

    /**
     * Undo the move action by placing the unit back at its original position.
     *
     * @param map The game map where the action is undone.
     */
    public void undone(GameMap map) throws PositionOutOfBoundsException {
        // Deshacer es simplemente mover en reversa: del destino al origen
        map.moveUnitTo(this.destination, this.origin);

        // Se devuelve la coordenada interna de la unidad a como estaba
        // TODO: Undo movement (Unit can no longer move, MoveAction must store what was done)
    }

    public Position getDestination() {
        return this.destination;
    }

}


    


