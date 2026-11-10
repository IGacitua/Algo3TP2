package game.map.direction;

import game.map.Position2D;

public class South implements OrthogonalDirection {
    public void movePosition(Position2D position) {
        position.moveDelta(deltaX(), deltaY());
    }

    public int deltaX() {
        return 0;
    }

    public int deltaY() {
        return 1;
    }

    public OrthogonalDirection invert() {
        return new North();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof South;
    }
}
