package game;

public class SingletonNotInitializedException extends RuntimeException {
    public SingletonNotInitializedException(Class<?> singletonClass) {
        super("Singleton " + singletonClass.getSimpleName() + " not initialized.");
    }
}
