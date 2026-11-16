package game.units.actions;

import game.map.GameMap;
import game.position.PositionOutOfBoundsException;

public interface UnitAction {
    boolean tryExecute(GameMap map) throws PositionOutOfBoundsException;
}
