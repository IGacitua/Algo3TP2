package game.units.behavior.player.state;

import game.map.GameMap;
import game.position.PositionOutOfBoundsException;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.behavior.player.TurnUndoTracker;

public interface TurnState {
    TurnState move(MoveAction action, GameMap map) throws PositionOutOfBoundsException;

    TurnState attack(AttackAction action, GameMap map) throws PositionOutOfBoundsException;

    TurnState undo(GameMap map, TurnUndoTracker undoTracker) throws PositionOutOfBoundsException;
}
