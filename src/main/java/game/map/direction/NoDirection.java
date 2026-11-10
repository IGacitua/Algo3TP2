package game.map.direction;

import game.map.Position2D;

public class NoDirection implements OrthogonalDirection {

    public void movePosition(Position2D position) {
    }

    public int deltaX() {
        return 0;
    }

    public int deltaY() {
        return 0;
    }

    public OrthogonalDirection invert() {
        return new NoDirection();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NoDirection;
    }
}
