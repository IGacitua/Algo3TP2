package game.units.movement;

public abstract class Movement {

    protected int movementDistance;

    public Movement(int movementDistance) {
        this.movementDistance = movementDistance;
    }

    public abstract boolean canMoveToWater();

    public abstract boolean canBeContaminated();

    public int getMovementDistance() {
        return movementDistance;
    }
}
