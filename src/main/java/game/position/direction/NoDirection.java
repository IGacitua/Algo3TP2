package game.position.direction;

import game.position.Position2D;

public class NoDirection implements OrthogonalDirection {

    public void movePosition(Position2D position) {
    }

    public int getDeltaX() {
        return 0;
    }

    public int getDeltaY() {
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
