package game.units.behavior.player.state;

import game.map.GameMap;
import game.position.PositionOutOfBoundsException;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.behavior.player.TurnUndoTracker;

public class ReadyState implements TurnState {

    @Override
    public TurnState move(MoveAction action, GameMap map) throws PositionOutOfBoundsException {
        if (!action.tryExecute(map)) {
            return this;
        }
        return new MovedState(action);
    }

    @Override
    public TurnState attack(AttackAction action, GameMap map) throws PositionOutOfBoundsException {
        if (!action.tryExecute(map)) {
            return this;
        }
        return new FinishedState();
    }

    @Override
    public TurnState undo(GameMap map, TurnUndoTracker undoTracker) {
        throw new IllegalStateException("Cannot undo movement if no movement has been made.");
    }

}
