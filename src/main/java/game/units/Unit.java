package game.units;

import game.map.GameMap;
import game.map.Position2D;
import game.map.PositionOutOfBoundsException;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.actions.UnitAction;
import game.units.movement.Grounded;
import game.units.movement.Movement;
import game.units.weapons.Weapon;

public class Unit {
    protected final Weapon weapon;
    private final Position2D position;
    private final Health health;
    private final Movement movement;
    private boolean isDamaged = false;
    private boolean contaminated = false;

    public Unit(Position2D position, Weapon weapon) {
        this(position, weapon, 3, new Grounded(1));
    }

    public Unit(Position2D position, Weapon weapon, int maximumHealth) {
        this(position, weapon, maximumHealth, new Grounded(1));
    }

    public Unit(Position2D position, Weapon weapon, int maximumHealth, Movement movement) {

        if (movement == null) {
            throw new IllegalArgumentException("Movement type cannot be null");
        }

        this.weapon = weapon;
        this.position = position;
        this.health = new Health(maximumHealth);
        this.movement = movement;

        try {
            if (!GameMap.getInstance().placeUnitAt(this, position)) {
                throw new IllegalArgumentException(
                        "Cannot place unit at the specified position: " + position
                );
            }
        } catch (PositionOutOfBoundsException e) {
            throw new RuntimeException(e);
        }
    }

    public UnitAction getAttack(Position2D target) {
        Position2D origin = new Position2D(this.position.getX(), this.position.getY());
        return new AttackAction(weapon, origin, target);
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
        return movement.canMoveToWater();
    }

    public void contaminate() {
        if (movement.canBeContaminated()) {
            contaminated = true;
        }
    }

    public void removeContamination() {
        contaminated = false;
    }

    public void kill() {
        health.kill();
    }

    public int getMovementDistance() {
        return movement.getMovementDistance();
    }

    public boolean isMech() {
        return false;
    }
}
