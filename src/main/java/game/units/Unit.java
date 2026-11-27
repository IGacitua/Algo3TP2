package game.units;

import game.position.Position;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.actions.UnitAction;
import game.units.movement.Movement;
import game.units.weapons.Weapon;

public class Unit {
    private final Health health;
    private final Movement movement;
    private final Weapon weapon;

    private final Team team;

    private boolean contaminated = false;

    public Unit(Team team, Weapon weapon, int maximumHealth, Movement movement) {
        this.team = team;
        this.health = new Health(maximumHealth);
        this.movement = movement;
        this.weapon = weapon;
    }

    // Team

    public boolean isInTeam(Team team) {
        return this.team.equals(team);
    }

    // Actions

    public UnitAction getAttackAction(Position origin, Position target) {
        return new AttackAction(weapon, origin, target);
    }

    public UnitAction getMovementAction(Position origin, Position destination) {
        return new MoveAction(this, origin, destination);
    }

    // HP

    public int getHealth() {
        return health.getCurrent();
    }

    public void dealDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }

        int effectiveDamage = !contaminated
                ? amount
                : amount * 2;

        health.receiveDamage(effectiveDamage);
    }

    public boolean isDamaged() {
        return health.isDamaged();
    }

    public boolean isAlive() {
        return health.isAlive();
    }

    public void kill() {
        health.kill();
    }

    // Status

    public void contaminate() {
        if (movement.canBeContaminated()) {
            contaminated = true;
        }
    }

    public boolean isContaminated() {
        return contaminated;
    }

    public void removeContamination() {
        contaminated = false;
    }

    // Movement

    // TODO: Call on turn start!
    public void resetMovementDistance() {
        this.movement.resetRemainingMovement();
    }

    public boolean canMoveToWater(boolean forced) {
        return movement.canMoveToWater(forced);
    }

    public void reduceMovement(double value) {
        this.movement.reduceMovement(value);
    }

    public boolean canMove(boolean forced, double movementCost) {
        return this.movement.canMove(forced, movementCost);
    }

    public boolean diesOnWater() {
        return this.movement.diesOnWater();
    }

    public boolean canAttackFromWater() {
        return this.movement.canAttackFromWater();
    }
}
