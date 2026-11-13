package game.units.actions;

import game.map.GameMap;
import game.map.Position2D;
import game.units.Unit;

public class MoveAction implements UnitAction {
	private Unit unit;
	private Position2D origin;
	private Position2D destination;
	
	public MoveAction(Unit unit, Position2D origin, Position2D destination) {
		this.unit = unit;
		this.origin = origin;
		this.destination = destination;
	}
	
	@Override
	public void execute(GameMap map) {
		// map.moveUnit(origin, destination);
	}
	
	public void undone(GameMap map) {
		// map.moveUnit(destination, origin);
	}
}

