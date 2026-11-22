package game.map.cell;

import game.electricalNetwork.ElectricalNetwork;
import game.position.Position;
import game.units.Unit;

public class Building extends Cell {

    public static final int DAMAGE_POWER_MODIFICATION = -1;

    private final ElectricalNetwork network;
    protected boolean isDestroyed;

    public Building(Position position, ElectricalNetwork network) {
        super(position);

        this.network = network;
        this.isDestroyed = false;
    }

    @Override
    public String cellTypeId() {
        return "B";
    }

    @Override
    public boolean canDamage() {
        return unitSlot.hasUnit() || !isDestroyed;
    }

    @Override
    protected void onDamaged(int damage) {
        super.onDamaged(damage);
        destroyBuilding();
    }

    protected void destroyBuilding() {
        if (isDestroyed) {
            return;
        }

        isDestroyed = true;
        network.modifyPower(DAMAGE_POWER_MODIFICATION);
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    protected void onUnitPlaced(Unit unit, boolean pushed) {
        destroyBuilding();
    }

    @Override
    public boolean isVekTarget() {
        return !isDestroyed;
    }

    @Override
    public boolean canPlaceUnit(Unit unit, boolean pushed) {
        if (pushed && !isDestroyed) {
            return false;
        }

        return super.canPlaceUnit(unit, pushed);
    }

}
