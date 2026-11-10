package game.map.direction;

import game.map.Position2D;

public class East implements OrthogonalDirection {
    public void movePosition(Position2D position) {
        position.moveDelta(deltaX(), deltaY());
    }

    public int deltaX() {
        return 1;
    }

    public int deltaY() {
        return 0;
    }

    public OrthogonalDirection invert() {
        return new West();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof East;
    }
}
