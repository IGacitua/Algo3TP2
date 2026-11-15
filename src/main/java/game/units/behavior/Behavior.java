package game.units.behavior;

import game.map.GameMap;
import game.map.PositionOutOfBoundsException;

public interface Behavior {
    void executePhase(GameMap map) throws PositionOutOfBoundsException;
}
