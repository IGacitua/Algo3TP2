package game.position.direction;

import game.position.Position;

public abstract class OrthogonalDirection {
    public Position getMoved(Position position) {
        return position.getMoved(getDeltaX(), getDeltaY());
    }

    public boolean isValidDirection() {
        return true;
    }

    public abstract int getDeltaX();

    public abstract int getDeltaY();

    public abstract OrthogonalDirection invert();
}

