package game.units.movement;

import game.map.cell.Cell;

public abstract class Movement {

    protected double originalMovementDistance;
    protected double movementDistance;

    public Movement(int movementDistance) {
        this.originalMovementDistance = movementDistance;
        this.movementDistance = movementDistance;
    }

    public void resetMovementDistance() {
        this.movementDistance = originalMovementDistance;
    }

    public boolean canMoveTo(Cell destinationCell, boolean forced) {
        if (!destinationCell.isFull()) {
            return false;
        }
        if (!forced && movementDistance == 0) {
            return false; // No movement left for another step.
        }
        return canMove(destinationCell, forced);
    }

    // TODO: Better name distinction for canMove / canMoveTo
    protected abstract boolean canMove(Cell destinationCell, boolean forced);

    public void onMove() {
        movementDistance--;
    }

    public abstract boolean canBeContaminated();

    public abstract boolean canMoveToWater();
}
