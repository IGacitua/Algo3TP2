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

    public void moveDelta(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public double getDistance(Position2D other) {
        return other.getDistance(getX(), getY());
    }

    public double getDistance(int x, int y) {
        int dx = getX() - x;
        int dy = getY() - y;
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
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
}
