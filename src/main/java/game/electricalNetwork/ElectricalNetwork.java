package game.electricalNetwork;

public class ElectricalNetwork {
    private int power;

    public ElectricalNetwork(int startingPower) {
        setPower(startingPower);
    }

    public int getPower() {
        return power;
    }

    private void setPower(int value) {
        power = value;
        if (power < 0) {
            power = 0;
        }
    }

    public void modifyPower(int amount) {
        setPower(getPower() + amount);
    }
}
