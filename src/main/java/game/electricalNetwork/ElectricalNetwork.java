package game.electricalNetwork;

public class ElectricalNetwork {
    private final static ElectricalNetwork instance = new ElectricalNetwork();

    private int power;

    public static ElectricalNetwork getInstance() {
        return instance;
    }

    private ElectricalNetwork() {
        power = 5;
    }

    public int getPower(){
        return power;
    }

    public void reducePower() {
        if (power > 0){
            power--;
        }
    }
}
