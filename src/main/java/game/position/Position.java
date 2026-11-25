package game.position;

import game.position.direction.*;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Access

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Movement

    public Position getMoved(int x, int y) {
        return new Position(getX() + x, getY() + y);
    }

    public Position getAdjacent(OrthogonalDirection direction) {
        return new Position(getX() + direction.getDeltaX(), getY() + direction.getDeltaY());
    }

    // Distance

    public double getDiagonalDistance(Position other) {
        return other.getDiagonalDistance(getX(), getY());
    }

    public double getDiagonalDistance(int x, int y) {
        // hypot is a static helper that calculates the square root of the sum of squares
        // AKA distance.
        // This avoids large-number overflow and is more accurate
        return Math.hypot(((double) getX() - x), (double) getY() - y);
    }

    public double getOrthogonalDistance(Position other) {
        return other.getOrthogonalDistance(getX(), getY());
    }

    public double getOrthogonalDistance(int x, int y) {
        return Math.abs(getX() - x) + Math.abs(getY() - y);
    }

    // Direction

    public OrthogonalDirection getDirectionTo(Position other) {
        return other.getDirectionTo(getX(), getY()).invert();
    }

    public OrthogonalDirection getDirectionTo(int x, int y) {
        int dx = x - getX();
        int dy = y - getY();

        if (dx > 0 && dy == 0) {
            return new East();
        }
        if (dx < 0 && dy == 0) {
            return new West();
        }

        if (dx == 0 && dy > 0) {
            return new South();
        }

        if (dx == 0 && dy < 0) {
            return new North();
        }

        return new NoDirection();
    }

    // Ordering

    // Criterio de desempate para el Vek en caso de tener opciones validas con la misma prioridad en su fase
    public boolean isCloserToOrigin(Position other) {
        return !other.isCloserToOrigin(getX(), getY());
    }

    public boolean isCloserToOrigin(int x, int y) {
        if (this.y < y)
            return true; // Menor fila

        if (this.y == y)
            return this.x < x; // Menor columna

        return false;
    }

    // Convenience

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position other = (Position) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "Position: (" + x + ", " + y + ")";
    }
}