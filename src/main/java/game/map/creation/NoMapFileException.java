package game.map.creation;

public class NoMapFileException extends RuntimeException {
    public NoMapFileException(String path) {
        super("No map file found at " + path + ".");
    }
}
