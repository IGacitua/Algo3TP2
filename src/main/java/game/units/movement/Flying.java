package game.units.movement;

// Usado por: Vek Avispón
public class Flying extends Movement {

    public Flying(int maxDistance) {
        super(maxDistance);
    }

    @Override
    public boolean canBeContaminated() {
        return false;
    }

    @Override
    public boolean canMoveToWater(boolean pushed) {
        return true;
    }

    @Override
    public boolean diesOnWater() {
        return false;
    }

    @Override
    public boolean canAttackFromWater() {
        return true;
    }

}