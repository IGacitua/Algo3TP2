package game.units.behavior.player.state;

import game.map.GameMap;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.behavior.player.TurnUndoTracker;

public class FinishedState implements TurnState {

    @Override
    public TurnState move(MoveAction action, GameMap map) {
        throw new IllegalStateException("Mech cannot move after attacking.");
    }

    @Override
    public TurnState attack(AttackAction action, GameMap map) {
        throw new IllegalStateException("Mech cannot attack twice in the same turn.");
    }

    @Override
    public TurnState undo(GameMap map, TurnUndoTracker undoTracker) {
        throw new IllegalStateException("Cannot undo movement after attacking.");
    }

}
