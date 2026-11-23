package game.units.behavior;

import game.map.GameMap;
import game.position.PositionOutOfBoundsException;
import game.units.Mech;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.actions.UnitAction;

public class PlayerBehavior implements Behavior {
    private final Mech mech; // TODO: Usar para validar condiciones de la unidad, como si sigue vivo, puede atacar, etc.
    private final TurnUndoTracker undoTracker;
    private MechTurnState state;

    public PlayerBehavior() {
        this(null, new TurnUndoTracker());
    }

    public PlayerBehavior(Mech mech) {
        this(mech, new TurnUndoTracker());
    }

    public PlayerBehavior(Mech mech, TurnUndoTracker undoTracker) throws IllegalArgumentException {
        if (undoTracker == null) {
            throw new IllegalArgumentException("Undo tracker cannot be null");
        }
        this.mech = mech;
        this.undoTracker = undoTracker;
        this.state = new ReadyState();
    }

    @Override
    public void executePhase(GameMap map) {
        // Resetea el estado al inicio del turno
        this.state = new ReadyState();
        this.undoTracker.reset();
    }

    public void executeAction(UnitAction action, GameMap map) throws IllegalStateException {
        try {
            action.executeWith(this, map);
        } catch (PositionOutOfBoundsException e) {
            throw new IllegalStateException("Position out of bounds during action execution: " + e.getMessage());
        }
    }

    public void executeMove(MoveAction action, GameMap map) throws PositionOutOfBoundsException {
        this.state = this.state.move(action, map);
    }

    public void executeAttack(AttackAction action, GameMap map) throws PositionOutOfBoundsException {
        this.state = this.state.attack(action, map);
    }

    public void undoMovement(GameMap map) throws IllegalStateException {
        try {
            this.state = this.state.undo(map, this.undoTracker);
        } catch (PositionOutOfBoundsException e) {
            throw new IllegalStateException("Position out of bounds during undo movement: " + e.getMessage());
        }
    }

}
