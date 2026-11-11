package game.map.direction;

import game.map.Position2D;

public class North implements OrthogonalDirection {
    public void movePosition(Position2D position) {
        position.moveTo(getDeltaX(), getDeltaY());
    }

    public int getDeltaX() {
        return 0;
    }

    public int getDeltaY() {
        return -1;
    }

    public OrthogonalDirection invert() {
        return new South();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof North;
    }
}
