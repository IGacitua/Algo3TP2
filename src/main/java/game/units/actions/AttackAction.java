package game.units.actions;

import game.map.GameMap;
import game.map.cell.Cell;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.behavior.player.PlayerBehavior;
import game.units.weapons.Weapon;

public class AttackAction implements UnitAction {
    private final Weapon weapon;
    private final Position origin;
    private final Position target;

    public AttackAction(Weapon weapon, Position origin, Position target) {
        this.weapon = weapon;
        this.origin = origin;
        this.target = target;
    }

    @Override
    public boolean tryExecute(GameMap map) throws PositionOutOfBoundsException {
        Cell originCell = map.getCellAt(origin);

        if (!originCell.hasUnitThatCanAttack()) {
            return false;
        }

        return weapon.attack(map, origin, target);
    }

    @Override
    public void executeWith(PlayerBehavior behavior, GameMap map) throws PositionOutOfBoundsException {
        behavior.executeAttack(this, map);
    }
}
