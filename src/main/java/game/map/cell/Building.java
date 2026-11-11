package game.map.cell;

import game.electricalNetwork.ElectricalNetwork;
import game.map.Position2D;
import game.units.Unit;

public class Building extends Cell {

    private static final int DAMAGE_POWER_REDUCTION = 1;

    protected boolean isDestroyed;

    public Building(Position2D position) {
        super(position);
    }

    @Override
    protected boolean canDamage() {
        return containedUnit != null || !isDestroyed;
    }

    @Override
    protected void onDamaged() {
        super.onDamaged();
        destroyBuilding();
    }

    protected void destroyBuilding() {
        if (isDestroyed) {
            return;
        }

        isDestroyed = true;
        ElectricalNetwork.getInstance().reducePower(DAMAGE_POWER_REDUCTION);
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    protected void onUnitPlaced(Unit unit, boolean pushed) {
        destroyBuilding();
    }

    @Override
    protected void onUnitRemoved(Unit unit) {
    }
}
