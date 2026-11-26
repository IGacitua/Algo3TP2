package game.units.behavior.player.state;

import game.map.GameMap;
import game.position.PositionOutOfBoundsException;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.behavior.player.TurnUndoTracker;

public class MovedState implements TurnState {
    private final MoveAction previousMovement;

    public MovedState(MoveAction previousMovement) {
        this.previousMovement = previousMovement;
    }

    @Override
    public TurnState move(MoveAction action, GameMap map) {
        throw new IllegalStateException("Mech cannot move twice in the same turn.");
    }

    @Override
    public TurnState attack(AttackAction action, GameMap map) throws PositionOutOfBoundsException {
        if (!action.tryExecute(map)) {
            return this;
        }
        return new FinishedState();
    }

    @Override
    public TurnState undo(GameMap map, TurnUndoTracker undoTracker) throws PositionOutOfBoundsException {
        undoTracker.consumeUndo();
        previousMovement.undoExecution(map);
        return new ReadyState();
    }

}
