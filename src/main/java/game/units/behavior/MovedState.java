package game.units.behavior;

import game.map.GameMap;
import game.position.PositionOutOfBoundsException;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;

public class MovedState implements MechTurnState {
    private final MoveAction previousMovement;

    public MovedState(MoveAction previousMovement) {
        this.previousMovement = previousMovement;
    }

    @Override
    public MechTurnState move(MoveAction action, GameMap map) {
        throw new IllegalStateException("Mech cannot move twice in the same turn.");
    }

    @Override
    public MechTurnState attack(AttackAction action, GameMap map) throws PositionOutOfBoundsException {
        if (!action.tryExecute(map)) {
            return this;
        }
        return new FinishedState();
    }

    @Override
    public MechTurnState undo(GameMap map, TurnUndoTracker undoTracker) throws PositionOutOfBoundsException {
        undoTracker.consumeUndo();
        previousMovement.undoExecution(map);
        return new ReadyState();
    }

}
