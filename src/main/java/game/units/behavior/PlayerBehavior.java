package game.units.behavior;

import game.map.GameMap;
import game.map.PositionOutOfBoundsException;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.actions.UnitAction;

public class PlayerBehavior implements Behavior {
    private MoveAction previousMovement;
    private boolean hasAttacked = false;
    private boolean hasMoved = false;

    @Override
    public void executePhase(GameMap map) {
        // Resetea el estado al inicio del turno
        this.hasAttacked = false;
        this.hasMoved = false;
        this.previousMovement = null;
    }

    public void executeAction(UnitAction action, GameMap map) throws PositionOutOfBoundsException {
        if (action instanceof MoveAction && !hasMoved) {
            action.execute(map);
            this.previousMovement = (MoveAction) action;
            this.hasMoved = true;
        } else if (action instanceof AttackAction && !hasAttacked) {
            action.execute(map);
            this.hasAttacked = true;
        }
    }

    public void undoMovement(GameMap map) throws PositionOutOfBoundsException {
        // Solo se puede deshacer si se movió y NO atacó
        if (hasMoved && !hasAttacked && previousMovement != null) {
            previousMovement.undone(map);
            hasMoved = false;
            previousMovement = null;
        }
    }
}
