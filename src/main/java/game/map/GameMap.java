package game.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import game.SingletonNotInitializedException;
import game.map.cell.Building;
import game.map.cell.Cell;
import game.map.creation.InvalidMapFileException;
import game.map.creation.MapImporter;
import game.map.direction.*;
import game.units.Unit;
import game.units.weapons.Weapon;

public class GameMap {
    private static final GameMap instance = new GameMap();

    private boolean initialized = false; // TODO: Temporary solution. Eventually I'd like to get rid of the singleton access.
    private CellGrid grid;

    private GameMap() {
    }

    public static GameMap getInstance() {
        return instance;
    }

    public void initialize(MapImporter importer, String mapFilePath) throws InvalidMapFileException {
        grid = new CellGrid(importer.importMap(mapFilePath));
        initialized = true;
    }

    public boolean tryPlaceUnit(Unit unit, Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return getCell(position).tryPlaceUnit(unit, false);
    }

    /// Temporary method for testing.
    public void placeBuilding(Position2D position) {
        ensureInitialized();
        grid.replaceCell(new Building(position), position);
    }

    public Cell getCell(Position2D position) throws PositionOutOfBoundsException {
        ensureInitialized();
        return grid.getCell(position);
    }

    public void damage(Position2D target) throws PositionOutOfBoundsException {
        ensureInitialized();
        getCell(target).tryDamage();
    }

    public void pushUnit(Position2D unit, OrthogonalDirection direction) throws PositionOutOfBoundsException {
        ensureInitialized();

        Cell fromCell = getCell(unit);
        Cell toCell = getCell(unit.getAdjacent(direction));

        Unit unitToPush = fromCell.popUnit();
        if (unitToPush != null) {
            toCell.tryPlaceUnit(unitToPush, true);
        }
    }

    public void pushAllAdjacent(Position2D target) {
        ensureInitialized();

        pushAway(target, new North());
        pushAway(target, new South());
        pushAway(target, new East());
        pushAway(target, new West());
    }

    private void pushAway(Position2D target, OrthogonalDirection direction) {
        try {
            Position2D adjacent = target.getAdjacent(direction);
            pushUnit(adjacent, direction);
        } catch (PositionOutOfBoundsException e) {
            // Skip cells that are out of bounds.
        }
    }

    private void ensureInitialized() {
        if (!initialized) {
            throw new SingletonNotInitializedException(GameMap.class);
        }
    }


    // CONSULTAS DE ESTADO (Para reglas de comportamiento y restricciones de armas)
    
    // Devuelve true si hay un Mech en la posición (usado por Vek Prioridad 2).
    public boolean hasMechAt(Position2D pos); 
    
    // Devuelve true si hay un Edificio en la posición (usado por Vek Prioridad 1).
    public boolean hasBuildingAt(Position2D pos); 
    
    // Devuelve true si la celda es Agua (usado para saber si un Mech puede atacar).
    public boolean isWaterAt(Position2D pos); 
    
    // Devuelve true si la celda está ocupada por cualquier unidad (usado para colisiones).
    public boolean hasUnitAt(Position2D pos); 

    
    // BÚSQUEDA Y DISTANCIAS (Para la Inteligencia Artificial de los Vek)

    // Ejecuta BFS y devuelve las coordenadas transitables según el movimiento de la unidad.
    public List<Position2D> getReachablePositions(Unit unit); 
    
    // Devuelve la coordenada de un Edificio si el arma lo alcanza disparando desde 'origin'.
    public Position2D findAttackableBuildingFrom(Position2D origin, Weapon weapon); 
    
    // Devuelve la coordenada de un Mech si el arma lo alcanza disparando desde 'origin'.
    public Position2D findAttackableMechFrom(Position2D origin, Weapon weapon); 
    
    // Calcula la distancia Manhattan desde 'pos' hasta el edificio más cercano del tablero.
    public int distanceToClosestBuildingFrom(Position2D pos); 


    // EJECUCIÓN DE ACCIONES (Para que MoveAction y AttackAction alteren el tablero)
    
    // Mueve la unidad y aplica efectos de terreno al llegar (ej. contaminación por Ácido).
    public void moveUnit(Position2D origin, Position2D destination); 
    
    // Aplica daño a la celda (resta vida si hay unidad, o baja Red Eléctrica si es Edificio).
    public void applyDamage(Position2D target, int damage); 
    
    // Empuja la unidad 1 celda. Si choca contra algo, NO la mueve y aplica 1 de daño a ambos.
    public void pushUnit(Position2D target, OrthogonalDirection direction);
}
