package game.utils;

public class DuplicateSingletonException extends RuntimeException {
    public DuplicateSingletonException(Class<?> type) {
        super("Duplicate singleton instance of type: " + type.getName());
    }
}
