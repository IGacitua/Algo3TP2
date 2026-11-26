package game.units.actions;

import game.map.GameMap;
import game.position.PositionOutOfBoundsException;
import game.units.behavior.player.PlayerBehavior;

public interface UnitAction {
    boolean tryExecute(GameMap map) throws PositionOutOfBoundsException;

    void executeWith(PlayerBehavior behavior, GameMap map) throws PositionOutOfBoundsException;
}
