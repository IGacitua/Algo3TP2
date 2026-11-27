package game.units;

import game.core.AttackPlanner;
import game.map.GameMap;
import game.map.cell.Cell;
import game.position.Position;
import game.position.PositionOutOfBoundsException;
import game.units.actions.AttackAction;
import game.units.actions.MoveAction;
import game.units.actions.UnitAction;
import game.units.behavior.Behavior;
import game.units.movement.Movement;
import game.units.weapons.Weapon;

public class Unit {
    private final Health health;
    private final Movement movement;
    private final Weapon weapon;
    private final Team team;

    private Behavior behavior;
    private boolean contaminated = false;

    public Unit(Team team, Weapon weapon, int maximumHealth, Movement movement) {
        this(team, weapon, maximumHealth, movement, null);
    }
    
    public Unit(Team team, Weapon weapon, int maximumHealth, Movement movement, Behavior behavior) {
        if (movement == null) {
            throw new IllegalArgumentException("Movement type cannot be null");
        }

        this.team = team;
        this.health = new Health(maximumHealth);
        this.movement = movement;
        this.weapon = weapon;
        this.behavior = behavior;
    }

    public void executePhase(Position origin, GameMap map, AttackPlanner planner) throws PositionOutOfBoundsException {
        if (behavior != null) {
            behavior.executePhase(origin, map, planner);
        }
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
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

    public boolean canMoveToWater() {
        return movement.canMoveToWater();
    }

    public void resetMovementDistance() {
        this.movement.resetMovementDistance();
    }

    public void consumeMovement(double multiplier) {
        this.movement.consumeMovement(multiplier);
    }

    public boolean canMoveTo(Cell destinationCell, boolean forced, double consumptionMultiplier) {
        return this.movement.isMovementAllowed(destinationCell, forced, consumptionMultiplier);
    }
}
