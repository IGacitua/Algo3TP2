package game.position.direction;

public class East extends OrthogonalDirection {

    @Override
    public int getDeltaX() {
        return 1;
    }


    @Override
    public int getDeltaY() {
        return 0;
    }


    @Override
    public OrthogonalDirection invert() {
        return new West();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof East;
    }
}
