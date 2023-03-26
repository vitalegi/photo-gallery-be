package it.vitalegi.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileUtilTests {

    @Test
    void test_getExtension() {
        assertEquals("txt", FileUtil.getExtension(Path.of("c.txt")));
        assertEquals("txt", FileUtil.getExtension(Path.of("a", "b", "c.txt")));
        assertEquals("txt", FileUtil.getExtension(Path.of("a", "b", "c.d.e.txt")));
        assertEquals("", FileUtil.getExtension(Path.of("c")));
        assertEquals("", FileUtil.getExtension(Path.of("c.")));
        assertEquals("c", FileUtil.getExtension(Path.of(".c")));
    }

}
