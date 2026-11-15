package game.units.movement;

public class Flying extends Movement {

    public Flying() {
        super(1);
    }

    public Flying(int movementDistance) {
        super(movementDistance);
    }

    @Override
    public boolean canMoveToWater() {
        return true;
    }

    @Override
    public boolean canBeContaminated() {
        return false;
    }

}