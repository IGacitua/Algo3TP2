package game.position.direction;

public abstract class OrthogonalDirection {

    public abstract int getDeltaX();

    public abstract int getDeltaY();

    public abstract OrthogonalDirection invert();
}

