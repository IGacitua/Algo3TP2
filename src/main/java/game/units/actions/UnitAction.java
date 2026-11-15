package game.units.actions;

import game.map.GameMap;
import game.map.PositionOutOfBoundsException;

public interface UnitAction {
    void execute(GameMap map) throws PositionOutOfBoundsException;
}
