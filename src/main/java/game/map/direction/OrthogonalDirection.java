package game.map.direction;

import game.map.Position2D;

public interface OrthogonalDirection {
    void movePosition(Position2D position);

    int deltaX();

    int deltaY();

    OrthogonalDirection invert();
}

