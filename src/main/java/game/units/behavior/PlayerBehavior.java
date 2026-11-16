package game.units.behavior;

import game.map.GameMap;
import game.position.PositionOutOfBoundsException;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.actions.UnitAction;

public class PlayerBehavior implements Behavior {
    private MoveAction previousMovement;
    private boolean hasAttacked = false;
    private boolean hasMoved = false;
    private boolean hasUndone = false;

    @Override
    public void executePhase(GameMap map) {
        // Resetea el estado al inicio del turno
        this.hasAttacked = false;
        this.hasMoved = false;
        this.previousMovement = null;
        this.hasUndone = false;
    }

    public void executeAction(UnitAction action, GameMap map) throws IllegalStateException {
        try {
            if (action instanceof MoveAction) {
                if (hasMoved || hasAttacked) {
                    throw new IllegalStateException("Cannot move after attacking or move twice in the same turn.");
                }
                action.tryExecute(map);
                this.previousMovement = (MoveAction) action;
                this.hasMoved = true;
            } else if (action instanceof AttackAction) {
                if (hasAttacked) {
                    throw new IllegalStateException("Cannot attack twice in the same turn.");
                }
                action.tryExecute(map);
                this.hasAttacked = true;
            }
        } catch (PositionOutOfBoundsException e) {
            throw new IllegalStateException("Position out of bounds during action execution: " + e.getMessage());
        }
    }

    public void undoMovement(GameMap map) throws IllegalStateException {
        if (!canUndo()) {
            throw new IllegalStateException("Cannot undo movement after attacking or if no movement has been made.");
        }

        try {
            previousMovement.undone(map);
            hasMoved = false;
            previousMovement = null;
            hasUndone = true;
        } catch (PositionOutOfBoundsException e) {
            throw new IllegalStateException("Position out of bounds during undo movement: " + e.getMessage());
        }
    }

    /**
     * Checks if the player can undo the last movement action.
     * The player can undo if they have moved, have not attacked, have not undone already,
     * and there is a previous movement action to undo.
     *
     * @return true if the player can undo the last movement, false otherwise.
     */
    private boolean canUndo() {
        return hasMoved && !hasAttacked && !hasUndone && previousMovement != null;
    }
}
