package game.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {

    private static final Path ROOT = Path.of("").toAbsolutePath();
    private static FileHandler instance;

    private FileHandler() {
    }

    public static FileHandler getInstance() {
        if (instance == null) {
            instance = new FileHandler();
        }
        return instance;
    }

    /// Reads the content of a file at the specified path and returns it as a String.
    public String readFile(String filePath) throws IOException {
        return Files.readString(ROOT.resolve(filePath));
    }

    /// Writes the specified content to a file at the given path. </br>
    /// Overwrites the file if it already exists.
    public void writeFile(String filePath, String content) throws IOException {
        Files.writeString(ROOT.resolve(filePath), content);
    }
}
