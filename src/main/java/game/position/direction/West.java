package game.position.direction;

import game.position.Position2D;

public class West implements OrthogonalDirection {

    public void movePosition(Position2D position) {
        position.moveDelta(getDeltaX(), getDeltaY());
    }

    public int getDeltaX() {
        return -1;
    }

    public int getDeltaY() {
        return 0;
    }

    public OrthogonalDirection invert() {
        return new East();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof West;
    }
}
