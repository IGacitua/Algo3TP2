package game.units;

import game.map.GameMap;
import game.map.creation.InvalidMapFileException;
import game.position.Position;
import game.units.movement.Flying;
import game.units.movement.Grounded;
import game.units.movement.Hover;
import game.units.weapons.Weapon;
import game.units.weapons.mekWeapons.Artillery;
import game.units.weapons.mekWeapons.Cannon;
import game.units.weapons.mekWeapons.Punch;
import game.units.weapons.vekWeapons.AcidShot;

public class UnitFactory {

    private final String MEK_COMBAT;
    private final String MEK_CANNON;
    private final String MEK_ARTILLERY;

    private final String VEK_HOPPER;
    private final String VEK_FIREFLY;
    private final String VEK_HORNET;

    public UnitFactory() {
        MEK_COMBAT = "M_COM";
        MEK_CANNON = "M_CAN";
        MEK_ARTILLERY = "M_ART";

        VEK_HOPPER = "V_HOP";
        VEK_FIREFLY = "V_FIR";
        VEK_HORNET = "V_HOR";
    }

    // Mek

    public static Unit createCombatMek() {
        Weapon weapon = new Punch();
        return new Unit(Team.Player, weapon, 3, new Hover(3));
    }

    public static Unit createCannonMek() {
        Weapon weapon = new Cannon();
        return new Unit(Team.Player, weapon, 3, new Hover(3));
    }

    public static Unit createArtilleryMek() {
        Weapon weapon = new Artillery();
        return new Unit(Team.Player, weapon, 2, new Hover(3));
    }

    // Vek

    public static Unit createHopperVek() {
        Weapon weapon = null; // TODO: Bite Weapon!
        return new Unit(Team.Enemy, weapon, 2, new Grounded(3));
    }

    public static Unit createFireflyVek() {
        Weapon weapon = new AcidShot();
        return new Unit(Team.Enemy, weapon, 3, new Grounded(2));
    }

    public static Unit createHornetVek() {
        Weapon weapon = null; // TODO: Sting Weapon!
        return new Unit(Team.Enemy, weapon, 2, new Flying(4));
    }

    // Creation

    public Unit createUnitAt(String unitID, GameMap map, Position position) {
        Unit unit = null;

        if (unitID.equals(MEK_COMBAT)) {
            unit = createCombatMek();
        }
        if (unitID.equals(MEK_CANNON)) {
            unit = createCannonMek();
        }
        if (unitID.equals(MEK_ARTILLERY)) {
            unit = createArtilleryMek();
        }
        if (unitID.equals(VEK_HOPPER)) {
            unit = createHopperVek();
        }
        if (unitID.equals(VEK_FIREFLY)) {
            unit = createFireflyVek();
        }
        if (unitID.equals(VEK_HORNET)) {
            unit = createHornetVek();
        }

        if (unit == null) {
            throw new InvalidMapFileException("Invalid unit ID: " + unitID);
        }

        map.tryPlaceUnit(unit, position);
        return unit;
    }
}
