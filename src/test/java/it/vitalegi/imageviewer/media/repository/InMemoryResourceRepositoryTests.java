package it.vitalegi.imageviewer.media.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
public class InMemoryResourceRepositoryTests {

    private static final Path SOURCE_1 = Path.of("a/b/c1.jpeg");
    private static final Path TARGET_1 = Path.of("out/b/c1.png");
    private static final Path SOURCE_2 = Path.of("a/b/c2.jpeg");
    private static final Path TARGET_2 = Path.of("out/b/c2.png");
    FileResourceRepository repository;

    @BeforeEach
    void init() {
        repository = new FileResourceRepository();
        repository.resources = new ArrayList<>();
    }

    @Test
    void test_addResource() {
        var resource = repository.addResource(SOURCE_1, TARGET_1);
        assertEquals(SOURCE_1, resource.getSourcePath());
        assertEquals(TARGET_1, resource.getTargetPath());
        assertThrows(IllegalArgumentException.class, () -> repository.addResource(SOURCE_1, TARGET_1));

        resource = repository.addResource(SOURCE_2, TARGET_2);
        assertEquals(SOURCE_2, resource.getSourcePath());
        assertEquals(TARGET_2, resource.getTargetPath());
    }

    @Test
    void test_hasResource() {
        var resource = repository.addResource(SOURCE_1, TARGET_1);
        assertEquals(SOURCE_1, resource.getSourcePath());
        assertEquals(TARGET_1, resource.getTargetPath());
        assertTrue(repository.hasResource(SOURCE_1));
        assertFalse(repository.hasResource(SOURCE_2));
    }


}
