package game.units.movement;

import game.map.cell.Cell;

public class Grounded extends Movement {

    public Grounded(int movementDistance) {
        super(movementDistance);
    }

    @Override
    protected boolean canMoveTo(Cell destinationCell, boolean forced) {
        return forced || !destinationCell.isWater();
    }

    @Override
    public boolean canBeContaminated() {
        return true;
    }

    @Override
    public boolean canMoveToWater() {
        return false;
    }
}
