package game.core;

import game.electricalNetwork.ElectricalNetwork;
import game.map.GameMap;
import game.map.creation.CellFactory;
import game.map.creation.MapImporter;
import game.units.UnitFactory;
import game.utils.FileHandler;

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

    private void initializeMap() {
        CellFactory cellFactory = new CellFactory(electricalNetwork);
        UnitFactory unitFactory = new UnitFactory();
        MapImporter importer = new MapImporter(fileHandler, cellFactory, unitFactory);
        battleMap = new GameMap(importer, MAP_FILE_PATH);
    }

    private void initializeNetwork() {
        electricalNetwork = new ElectricalNetwork(INITIAL_POWER);
    }
}
