package game.units.movement;

// Usado por: Todos los Mech
public class Hover extends Movement {

    public Hover(int maxDistance) {
        super(maxDistance);
    }

    @Override
    public boolean canBeContaminated() {
        return true;
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
        return false;
    }
}
