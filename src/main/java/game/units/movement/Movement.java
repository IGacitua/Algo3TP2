package game.units.movement;

public abstract class Movement {

    protected final double maximum;
    protected double current;

    public Movement(int maxDistance) {
        this.maximum = maxDistance;
        this.current = maxDistance;
    }

    public void resetRemainingMovement() {
        this.current = maximum;
    }

    public boolean canMove(boolean forced, double movementCost) {
        if (forced) {
            return true;
        }

        return (current - movementCost) >= 0;
    }

    public void reduceMovement(double value) {
        current -= value;
    }

    public abstract boolean canBeContaminated();

    public abstract boolean canMoveToWater(boolean pushed);

    public abstract boolean diesOnWater();

    public abstract boolean canAttackFromWater();
}
