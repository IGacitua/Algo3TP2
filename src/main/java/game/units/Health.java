package game.units;

public class Health {

    private final int maximum;
    private boolean isDamaged;
    private int current;

    public Health(int maximum) {
        if (maximum <= 0) {
            throw new IllegalArgumentException(
                    "Maximum health must be positive"
            );
        }

        this.maximum = maximum;
        this.current = maximum;
        this.isDamaged = false;
    }

    public void receiveDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }

        current = Math.max(0, current - amount);
        isDamaged = true;
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

    public void kill() {
        current = 0;
    }

    public boolean isDamaged() {
        return isDamaged;
    }
}


