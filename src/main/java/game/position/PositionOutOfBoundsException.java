package game.position;

public class PositionOutOfBoundsException extends Exception {
    public PositionOutOfBoundsException(Position position) {
        super(position + "is out of bounds.");
    }

    public PositionOutOfBoundsException(int x, int y) {
        super(new Position(x, y) + "is out of bounds.");
    }
}
