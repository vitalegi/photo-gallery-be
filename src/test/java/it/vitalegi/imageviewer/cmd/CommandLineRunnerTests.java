package it.vitalegi.imageviewer.cmd;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
public class CommandLineRunnerTests {

    CommandLineRunner runner;

    @BeforeEach
    void init() {
        runner = new CommandLineRunner();
    }

    @Test
    void test_convertHeicFile() throws IOException {
        Path target = Files.createTempFile("test", "heic.jpg");
        log.info("Target file: {}", target);
        var out = runner.execute("magick", "src/test/resources/test2.heic", "-resize", "1200x1200", "-quality", "75",
                "-strip", target.toString());
        assertTrue(out.getErr().isEmpty());
        assertEquals(0, out.getExitCode());
        assertTrue(Files.size(target) > 0);
    }

    @Test
    void test_convertHeicFile_badParams() throws IOException {
        Path target = Files.createTempFile("test", "heic.jpg");
        log.info("Target file: {}", target);
        var out = runner.execute("magick", "src/test/resources/test2.heic", "-unknownParam", target.toString());
        assertFalse(out.getErr().isEmpty());
        assertNotEquals(0, out.getExitCode());
        assertEquals(0, Files.size(target));
    }

}
