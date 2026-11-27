package game.units.actions;

import game.core.AttackPlanner;
import game.map.GameMap;
import game.position.PositionOutOfBoundsException;
import game.units.behavior.enemy.EnemyBehavior;
import game.units.behavior.player.PlayerBehavior;

public interface UnitAction {
    boolean tryExecute(GameMap map) throws PositionOutOfBoundsException;

    void executeWith(PlayerBehavior behavior, GameMap map) throws PositionOutOfBoundsException;

    void executeWith(EnemyBehavior behavior, GameMap map, AttackPlanner planner) throws PositionOutOfBoundsException;
}
