package game.map;

import game.map.direction.East;
import game.map.direction.NoDirection;
import game.map.direction.OrthogonalDirection;

public class Position2D {
    private int x;
    private int y;

    public Position2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Position2D that = (Position2D) obj;
        return that.getDistance(getX(), getY()) == 0;
    }

    @Override
    public String toString() {
        return "Position2D: (" + x + ", " + y + ")";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getDistance(Position2D other) {
        return other.getDistance(getX(), getY());
    }

    public double getDistance(int x, int y) {
        // hypot is a static helper that calculates the square root of the sum of squares
        // AKA distance.
        // This avoids large-number overflow and is more accurate
        return Math.hypot(((double) getX() - x), (double) getY() - y);
    }

    public double getOrthogonalDistance(Position2D other) {
        return other.getOrthogonalDistance(getX(), getY());
    }

    public double getOrthogonalDistance(int x, int y) {
        return Math.abs(getX() - x) + Math.abs(getY() - y);
    }

    public OrthogonalDirection getDirectionTo(Position2D other) {
        return other.getDirectionTo(getX(), getY()).invert();
    }

    public OrthogonalDirection getDirectionTo(int x, int y) {
        int dx = x - getX();
        int dy = y - getY();

        if (dx > 0 && dy == 0) {
            return new East();
        }
        if (dx < 0 && dy == 0) {
            return new game.map.direction.West();
        }

        if (dx == 0 && dy > 0) {
            return new game.map.direction.South();
        }

        if (dx == 0 && dy < 0) {
            return new game.map.direction.North();
        }

        return new NoDirection();
    }

    public Position2D getAdjacent(OrthogonalDirection direction) {
        return new Position2D(getX() + direction.getDeltaX(), getY() + direction.getDeltaY());
    }

    // Criterio de desempate para el Vek en caso de tener opciones validas con la misma prioridad en su fase
    public boolean isHigherPriorityThan(Position2D other) {
        if (this.y < other.y) return true; // Menor fila
        if (this.y == other.y) return this.x < other.x; // Menor columna
        return false;
    }
}
