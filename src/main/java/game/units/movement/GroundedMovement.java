package game.units.movement;

public class GroundedMovement implements MovementType {

    @Override
    public boolean canMoveToWater() {
        return false;
    }

    @Override
    public boolean canBeContaminated() {
        return true;
    }
}
