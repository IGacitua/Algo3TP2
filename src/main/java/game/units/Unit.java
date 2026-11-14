package game.units;

import game.map.GameMap;
import game.map.Position2D;
import game.map.PositionOutOfBoundsException;
import game.units.actions.AttackAction;
import game.units.actions.UnitAction;
import game.units.weapons.Weapon;

public class Unit {
    private final Weapon weapon;
    private final Position2D position;
    private final Health health;
    private boolean isDamaged = false;

    public Unit(Position2D position, Weapon weapon) {
        this(position, weapon, 3);
    }

    public Unit(Position2D position, Weapon weapon, int maximumHealth) {
        this.weapon = weapon;
        this.position = position;
        this.health = new Health(maximumHealth);

        try {
            if (!GameMap.getInstance().tryPlaceUnit(this, position)) {
                throw new IllegalArgumentException(
                        "Cannot place unit at the specified position: " + position
                );
            }
        } catch (PositionOutOfBoundsException e) {
            throw new RuntimeException(e);
        }
    }

    public UnitAction getAttack(Position2D target) {
        return new AttackAction(weapon, target);
    }

    public Position2D getPosition() {
        return position;
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public int getCurrentHealth() {
        return health.getCurrent();
    }

    public void damage() {
        health.receiveDamage(1);
        isDamaged = true;
    }

    public void moveTo(Position2D newPosition) {
        position.moveTo(newPosition.getX(), newPosition.getY());
    }

    public boolean canMoveToWater() {
        throw new UnsupportedOperationException("Not yet implemented"); // TODO
    }

    public void contaminate() {
        throw new UnsupportedOperationException("Not yet implemented"); // TODO
    }
}
