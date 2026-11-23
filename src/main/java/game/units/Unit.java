package game.units;

import game.position.Position;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.actions.UnitAction;
import game.units.movement.Grounded;
import game.units.movement.Movement;
import game.units.weapons.Weapon;

public class Unit {
    protected final Weapon weapon;
    private final Health health;
    private final Movement movement;
    private boolean contaminated = false;

    public Unit(Weapon weapon) {
        this(weapon, 3, new Grounded(1));
    }

    public Unit(Weapon weapon, int maximumHealth) {
        this(weapon, maximumHealth, new Grounded(1));
    }

    public Unit(Weapon weapon, int maximumHealth, Movement movement) {

        if (movement == null) {
            throw new IllegalArgumentException("Movement type cannot be null");
        }

        this.weapon = weapon;
        this.health = new Health(maximumHealth);
        this.movement = movement;
    }

    public UnitAction getAttack(Position origin, Position target) {
        return new AttackAction(weapon, origin, target);
    }

    public UnitAction getMove(Position origin, Position destination) {
        return new MoveAction(this, origin, destination);
    }

    public boolean isDamaged() {
        return health.isDamaged();
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

        int effectiveDamage = !contaminated
                ? amount
                : amount * 2;

        health.receiveDamage(effectiveDamage);
    }

    public boolean isAlive() {
        return health.isAlive();
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

    public boolean isContaminated() {
        return contaminated;
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
