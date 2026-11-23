package game.units.behavior;

import game.map.GameMap;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;

public class FinishedState implements MechTurnState {

    @Override
    public MechTurnState move(MoveAction action, GameMap map) {
        throw new IllegalStateException("Mech cannot move after attacking.");
    }

    @Override
    public MechTurnState attack(AttackAction action, GameMap map) {
        throw new IllegalStateException("Mech cannot attack twice in the same turn.");
    }

    @Override
    public MechTurnState undo(GameMap map, TurnUndoTracker undoTracker) {
        throw new IllegalStateException("Cannot undo movement after attacking.");
    }
    
}
