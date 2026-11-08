package game.units;

import game.map.GameMap;
import game.map.Position2D;
import game.units.actions.AttackAction;
import game.units.actions.UnitAction;
import game.units.weapons.Weapon;

public class Unit {
    private final Weapon weapon;
    private boolean isDamaged = false;
    private final Position2D position;

    public Unit(Position2D position, Weapon weapon) {
        this.weapon = weapon;
        this.position = position;
        GameMap.getInstance().placeUnit(this,position);
    }

    public UnitAction getAttack(Position2D target){
        return new AttackAction(weapon, target);
    }

    public Position2D getPosition() {
        return position;
    }

    public boolean isDamaged(){
        return isDamaged;
    }

    public void damage() {
        isDamaged = true;
    }

    public void moveTo(Position2D newPosition) {
        position.moveTo(newPosition.getX(), newPosition.getY());
    }
}
