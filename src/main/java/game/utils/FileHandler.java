package game.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {

    private static FileHandler instance;

    private FileHandler() {
        if (instance != null) {
            throw new DuplicateSingletonException(this.getClass());
        }
    }

    public static FileHandler getInstance() {
        if (instance == null) {
            instance = new FileHandler();
        }
        return instance;
    }

    /// Reads the content of a file at the specified path and returns it as a String.
    public String readFile(String filePath) throws IOException {
        Path path = Path.of(filePath);
        return Files.readString(path);
    }

    /// Writes the specified content to a file at the given path. </br>
    /// Overwrites the file if it already exists.
    public void writeFile(String filePath, String content) throws IOException {
        Path path = Path.of(filePath);
        Files.writeString(path, content);
    }
}
