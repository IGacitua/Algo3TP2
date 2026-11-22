package game.units.movement;

public class Grounded extends Movement {

    public Grounded() {
        super(1);
    }

    public Grounded(int movementDistance) {
        super(movementDistance);
    }

    @Override
    public boolean canMoveToWater() {
        return false;
    }

    @Override
    public boolean canBeContaminated() {
        return true;
    }

}
