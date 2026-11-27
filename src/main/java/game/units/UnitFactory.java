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
    private final String VEK_STINGER;

    public UnitFactory() {
        MEK_COMBAT = "M_CO";
        MEK_CANNON = "M_CA";
        MEK_ARTILLERY = "M_AR";

        VEK_HOPPER = "V_HO";
        VEK_FIREFLY = "V_FI";
        VEK_STINGER = "V_ST";
    }

    public Unit createUnitAt(String unitID, GameMap map, Position position) {
        Unit unit = null;

        if (unitID.equals(MEK_COMBAT)) {
            unit = createCombatMek(map);
        }
        if (unitID.equals(MEK_CANNON)) {
            unit = createCannonMek(map);
        }
        if (unitID.equals(MEK_ARTILLERY)) {
            unit = createArtilleryMek(map);
        }
        if (unitID.equals(VEK_HOPPER)) {
            unit = createHopperVek(map);
        }
        if (unitID.equals(VEK_FIREFLY)) {
            unit = createFireflyVek(map);
        }
        if (unitID.equals(VEK_STINGER)) {
            unit = createStingerVek(map);
        }

        if (unit == null) {
            throw new InvalidMapFileException("Invalid unit ID: " + unitID);
        }

        map.tryPlaceUnit(unit, position);
        return unit;
    }

    // Mek

    private Unit createCombatMek(GameMap map) {
        Weapon weapon = new Punch(map);
        return new Unit(Team.Player, weapon, 3, new Hover(3));
    }

    private Unit createCannonMek(GameMap map) {
        Weapon weapon = new Cannon(map);
        return new Unit(Team.Player, weapon, 3, new Hover(3));
    }

    private Unit createArtilleryMek(GameMap map) {
        Weapon weapon = new Artillery(map);
        return new Unit(Team.Player, weapon, 2, new Hover(3));
    }

    // Vek

    private Unit createHopperVek(GameMap map) {
        Weapon weapon = null; // TODO: Bite Weapon!
        return new Unit(Team.Enemy, weapon, 2, new Grounded(3));
    }

    private Unit createFireflyVek(GameMap map) {
        Weapon weapon = new AcidShot(map);
        return new Unit(Team.Enemy, weapon, 3, new Grounded(2));
    }

    private Unit createStingerVek(GameMap map) {
        Weapon weapon = null; // TODO: Sting Weapon!
        return new Unit(Team.Enemy, weapon, 2, new Flying(4));
    }
}
