package game.position.direction;

import game.position.Position2D;

public interface OrthogonalDirection {
    void movePosition(Position2D position);

    int getDeltaX();

    int getDeltaY();

    OrthogonalDirection invert();
}

