package game.map;

public class PositionOutOfBoundsException extends Exception {
    public PositionOutOfBoundsException(Position2D position) {
        super("Position " + position.getX() + ", " + position.getY() + "is out of bounds.");
    }

    public PositionOutOfBoundsException(double x, double y) {
        super("Position " + x + ", " + y + "is out of bounds.");
    }
}
