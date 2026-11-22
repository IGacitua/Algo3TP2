package game.position.direction;

public class South extends OrthogonalDirection {


    @Override
    public int getDeltaX() {
        return 0;
    }


    @Override
    public int getDeltaY() {
        return 1;
    }


    @Override
    public OrthogonalDirection invert() {
        return new North();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof South;
    }
}
