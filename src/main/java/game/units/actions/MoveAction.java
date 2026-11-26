package game.units.actions;

import game.map.GameMap;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Unit;
import game.units.behavior.player.PlayerBehavior;


public class MoveAction implements UnitAction {
    private final Unit unit;
    private final Position origin;
    private final Position destination;
    private boolean wasCleanBeforeMove = false;

    public MoveAction(Position origin, Position destination) {
        this(null, origin, destination);
    }

    public MoveAction(Unit unit, Position origin, Position destination) {
        this.unit = unit;
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    public boolean tryExecute(GameMap map) throws PositionOutOfBoundsException {
        if (unit != null) {
            this.wasCleanBeforeMove = !this.unit.isContaminated();
        }
        return map.moveUnit(this.origin, this.destination, 1.0);
    }

    @Override
    public void executeWith(PlayerBehavior behavior, GameMap map) throws PositionOutOfBoundsException {
        behavior.executeMove(this, map);
    }

    /**
     * Undo the move action by placing the unit back at its original position.
     *
     * @param map The game map where the action is undone.
     */
    public void undoExecution(GameMap map) throws PositionOutOfBoundsException {
        // Mover de destino a origen, devolviendo consumo de movimiento.
        map.moveUnit(this.destination, this.origin, -1.0);

        if (this.unit != null && this.wasCleanBeforeMove) {
            this.unit.removeContamination();
        }
    }

    /**
     *
     * @return The destination position of the move action.
     */
    public Position getDestination() {
        return this.destination;
    }

}


    


