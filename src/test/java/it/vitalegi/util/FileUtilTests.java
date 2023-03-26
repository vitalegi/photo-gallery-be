package it.vitalegi.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Log4j2
public class FileUtilTests {

    private Path path(String path) {
        return Path.of(path);
    }

    @Test
    void test_getExtension() {
        assertEquals("txt", FileUtil.getExtension(Path.of("c.txt")));
        assertEquals("txt", FileUtil.getExtension(Path.of("a", "b", "c.txt")));
        assertEquals("txt", FileUtil.getExtension(Path.of("a", "b", "c.d.e.txt")));
        assertEquals("", FileUtil.getExtension(Path.of("c")));
        assertEquals("", FileUtil.getExtension(Path.of("c.")));
        assertEquals("c", FileUtil.getExtension(Path.of(".c")));
    }

    @Test
    void test_getRelativePath() throws IOException {
        assertEquals(path("c/d/file.txt"), FileUtil.getRelativePath(path("C:/a/b"), path("C:/a/b/c/d/file.txt")));
        assertThrows(IllegalArgumentException.class, () -> FileUtil.getRelativePath(path("D:/a"), path("C:/b")));
    }
}
