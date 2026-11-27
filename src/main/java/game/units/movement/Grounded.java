package game.units.movement;

// Usado por: Vek Saltador, Vek Luciernaga
public class Grounded extends Movement {

    public Grounded(int maxDistance) {
        super(maxDistance);
    }

    @Override
    public boolean canBeContaminated() {
        return true;
    }

    @Override
    public boolean canMoveToWater(boolean pushed) {
        return pushed;
    }

    @Override
    public boolean diesOnWater() {
        return true;
    }

    @Override
    public boolean canAttackFromWater() {
        return false;
    }
}
