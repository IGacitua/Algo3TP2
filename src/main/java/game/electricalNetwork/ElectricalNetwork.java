package game.electricalNetwork;

public class ElectricalNetwork {
    private final static ElectricalNetwork instance = new ElectricalNetwork();

    private int power;

    private ElectricalNetwork() {
        power = 5;
    }

    public static ElectricalNetwork getInstance() {
        return instance;
    }

    public void reset() {
        power = 5;
    }

    public int getPower() {
        return power;
    }

    public void reducePower(int amount) {
        power -= amount;
        if (power < 0) {
            power = 0;
        }
    }
}
