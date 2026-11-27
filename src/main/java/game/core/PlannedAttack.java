package game.core;

import game.map.GameMap;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.Unit;

public class PlannedAttack {
    private final Unit attacker;
    private final int dx;
    private final int dy;
    
    public PlannedAttack(Unit attacker, Position announcedTarget, Position announcedOrigin) {
        this.attacker = attacker;
        this.dx = announcedTarget.getX() - announcedOrigin.getX();
        this.dy = announcedTarget.getY() - announcedOrigin.getY();
    }

    public Unit getAttacker() {
        return attacker;
    }

    public void executeFrom(Position currentPosition, GameMap map) throws PositionOutOfBoundsException {
        if (!attacker.isAlive() || currentPosition == null) {
            return;
        }
        Position currentTarget = currentPosition.getMoved(dx, dy);
        attacker.getAttackAction(currentPosition, currentTarget).tryExecute(map);
    }
}
