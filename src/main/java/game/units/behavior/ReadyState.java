package game.units.behavior;

import game.map.GameMap;
import game.position.PositionOutOfBoundsException;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;

public class ReadyState implements MechTurnState {

    @Override
    public MechTurnState move(MoveAction action, GameMap map) throws PositionOutOfBoundsException {
        if (!action.tryExecute(map)) {
            return this;
        }
        return new MovedState(action);
    }

    @Override
    public MechTurnState attack(AttackAction action, GameMap map) throws PositionOutOfBoundsException {
        if (!action.tryExecute(map)) {
            return this;
        }
        return new FinishedState();
    }

    @Override 
    public MechTurnState undo(GameMap map, TurnUndoTracker undoTracker) {
        throw new IllegalStateException("Cannot undo movement if no movement has been made.");
    }
    
}
