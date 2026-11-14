package game.units.movement;

public class FlyingMovement implements MovementType {

    @Override
    public boolean canMoveToWater() {
        return true;
    }

    @Override
    public boolean canBeContaminated() {
        return false;
    }
}