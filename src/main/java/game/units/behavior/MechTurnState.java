package game.units.behavior;

import game.map.GameMap;
import game.position.PositionOutOfBoundsException;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;

public interface MechTurnState {
    MechTurnState move(MoveAction action, GameMap map) throws PositionOutOfBoundsException;

    MechTurnState attack(AttackAction action, GameMap map) throws PositionOutOfBoundsException;
    
    MechTurnState undo(GameMap map, TurnUndoTracker undoTracker) throws PositionOutOfBoundsException;
}
