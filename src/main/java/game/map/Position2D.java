package game.map;

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

    public double getDistance(Position2D other) {
        return other.getDistance(this.x, this.y);
    }

    public double getDistance(int x, int y) {
        int dx = this.x - x;
        int dy = this.y - y;
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
}
