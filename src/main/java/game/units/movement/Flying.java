package game.units.movement;

import game.map.cell.Cell;

public class Flying extends Movement {

    public Flying(int movementDistance) {
        super(movementDistance);
    }

    @Override
    protected boolean canMove(Cell destinationCell, boolean forced) {
        return true;
    }


    @Override
    public boolean canBeContaminated() {
        return false;
    }

    @Override
    public boolean canMoveToWater() {
        return true;
    }

}