package game.map.creation;

public class NoMapFileException extends RuntimeException {
    public NoMapFileException() {
        super("No map file found!");
    }
}
