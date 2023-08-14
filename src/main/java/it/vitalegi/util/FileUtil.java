package it.vitalegi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileUtil {

    public static Stream<Path> list(Path path) {
        try {
            return Files.list(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<Path> listFiles(Path path) {
        return list(path).flatMap(path1 -> {
            if (Files.isDirectory(path1)) {
                return listFiles(path1);
            }
            return Stream.of(path1);
        });
    }

    public static String getExtension(Path path) {
        var filename = getFilename(path);
        var lastIndex = filename.lastIndexOf(".");
        if (lastIndex == -1) {
            return "";
        }
        return filename.substring(lastIndex + 1);
    }

    public static String getFilename(Path path) {
        return path.getFileName().toString();
    }

    public static Path getRelativePath(Path parent, Path child) {
        String absRoot = parent.toFile().getAbsolutePath();
        String absChild = child.toFile().getAbsolutePath();
        if (absChild.startsWith(absRoot)) {
            return Path.of(absChild.substring(absRoot.length() + 1));
        }
        throw new IllegalArgumentException("Elements are not parent/child. " + parent + ", " + child);
    }

    public static void createDirectories(Path directory) {
        try {
            Files.createDirectories(directory.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
