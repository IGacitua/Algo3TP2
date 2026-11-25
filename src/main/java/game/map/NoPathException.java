package game.map;

import game.position.Position;

public class NoPathException extends RuntimeException {

    private final Position start;
    private final Position destination;

    public NoPathException(Position start, Position destination) {
        super("No path from " + start + " to " + destination);
        this.start = start;
        this.destination = destination;
    }

    public Position getStart() {
        return start;
    }

    public Position getDestination() {
        return destination;
    }
}