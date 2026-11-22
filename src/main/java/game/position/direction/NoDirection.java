package game.position.direction;

public class NoDirection extends OrthogonalDirection {

    @Override
    public boolean isValidDirection() {
        return false;
    }

    @Override
    public int getDeltaX() {
        return 0;
    }

    @Override
    public int getDeltaY() {
        return 0;
    }
    
    @Override
    public OrthogonalDirection invert() {
        return new NoDirection();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NoDirection;
    }
}
