package game.units;

public class Health {

    private int current;
    private final int maximum;

    public Health(int maximum) {
        if (maximum <= 0) {
            throw new IllegalArgumentException(
                    "Maximum health must be positive"
            );
        }

        this.maximum = maximum;
        this.current = maximum;
    }

    public void receiveDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException(
                    "Damage cannot be negative"
            );
        }

        current = Math.max(0, current - amount);
    }

    public boolean isAlive() {
        return current > 0;
    }

    public int getCurrent() {
        return current;
    }

    public int getMaximum() {
        return maximum;
    }
}

