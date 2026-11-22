package game.core;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.creation.CellFactory;
import game.map.creation.MapImporter;
import game.utils.FileHandler;

// Nota para el profesor: Debería simplemente dar acceso publico a GameManager/ElectricalNetwork?
// O debería hacer passthrough para todos los accesos y dejar los accesos privados?
// En el primer caso rompe ley de demeter, pero el segundo da demasiada responsabilidad a BattleManager.
public class BattleManager {
    private final static String MAP_FILE_PATH = "src/main/resources/maps/defaultMap.txt"; // TODO: File does NOT exist.
    private final static BattleManager instance = new BattleManager();

    private final static int INITIAL_POWER = 5;

    private final FileHandler fileHandler;
    private GameMap battleMap;
    private ElectricalNetwork electricalNetwork;

    private BattleManager() {
        fileHandler = FileHandler.getInstance(); // Convenient access
        this.initializeNetwork();
        this.initializeMap();
    }

    public static BattleManager getInstance() {
        return instance;
    }

    public static GameMap getMap() {
        return instance.battleMap;
    }

    public static ElectricalNetwork getNetwork() {
        return instance.electricalNetwork;
    }

    private void initializeMap() {
        CellFactory cellFactory = new CellFactory(electricalNetwork);
        MapImporter importer = new MapImporter(fileHandler, cellFactory);
        battleMap = new GameMap(importer, MAP_FILE_PATH);
    }

    private void initializeNetwork() {
        electricalNetwork = new ElectricalNetwork(INITIAL_POWER);
    }
}
