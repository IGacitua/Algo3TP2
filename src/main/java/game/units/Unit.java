package game.units;

import game.map.GameMap;
import game.map.Position2D;
import game.map.PositionOutOfBoundsException;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.actions.UnitAction;
import game.units.weapons.Weapon;
import game.units.movement.MovementType;
import game.units.movement.GroundedMovement;

public class Unit {
    private final Weapon weapon;
    private final Position2D position;
    private final Health health;
    private boolean isDamaged = false;
    private boolean contaminated = false;
    private final MovementType movementType;

    public Unit(Position2D position, Weapon weapon) {
        this(position, weapon, 3, new GroundedMovement());
    }

    public Unit(Position2D position, Weapon weapon, int maximumHealth) {
        this(position, weapon, maximumHealth, new GroundedMovement());
    }

    public Unit(Position2D position, Weapon weapon, int maximumHealth,
                MovementType movementType) {

        if (movementType == null) {
            throw new IllegalArgumentException("Movement type cannot be null");
        }

        this.weapon = weapon;
        this.position = position;
        this.health = new Health(maximumHealth);
        this.movementType = movementType;

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

    public UnitAction getMove(Position2D destination) {
        Position2D origin = new Position2D(this.position.getX(), this.position.getY());
        return new MoveAction(this, origin, destination);
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
        damage(1);
    }

    public void damage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }

        int effectiveDamage = contaminated ? amount * 2 : amount;

        health.receiveDamage(effectiveDamage);
        isDamaged = true;
    }

    public boolean isAlive() {
        return health.isAlive();
    }

    public void moveTo(Position2D newPosition) {
        position.moveTo(newPosition.getX(), newPosition.getY());
    }

    public boolean canMoveToWater() {
        return movementType.canMoveToWater();
    }

    public void contaminate() {
        if (movementType.canBeContaminated()) {
            contaminated = true;
        }
    }

    public void removeContamination() {
        contaminated = false;
    }

    public void kill() {
        health.kill();
    }
}
