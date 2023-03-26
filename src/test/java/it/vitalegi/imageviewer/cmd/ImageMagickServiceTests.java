package it.vitalegi.imageviewer.cmd;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

@Slf4j
public class ImageMagickServiceTests {

    ImageMagickService service;
    CommandLineRunner runner;

    Callable<Boolean> fakeThread() {
        return () -> {
            Path target = Files.createTempFile("test2", ".jpg");
            service.convert(new ImageMagickBuilder().source(Path.of("src/test/resources/test2.heic")).target(target)
                                                    .quality(75).resize(1200, 1200).strip());
            return true;
        };
    }

    @BeforeEach
    void init() {
        runner = new CommandLineRunner();
        service = new ImageMagickService();
        service.runner = runner;
    }

    @Test
    void test_heic() throws IOException {
        Path target = Files.createTempFile("test2", ".jpg");

        service.convert(new ImageMagickBuilder().source(Path.of("src/test/resources/test2.heic")).target(target)
                                                .quality(75).resize(1200, 1200).strip());
        log.info("Output file: {}", target);
    }

    @Test
    void test_jpeg() throws IOException {
        Path target = Files.createTempFile("test1", ".jpg");

        service.convert(new ImageMagickBuilder().source(Path.of("src/test/resources/test1.jpg")).target(target)
                                                .quality(75).resize(1200, 1200).strip());
        log.info("Output file: {}", target);
    }

    @Test
    void test_parallelProcessing() throws IOException, InterruptedException {
        List<Callable<Boolean>> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(fakeThread());
        }
        Executors.newFixedThreadPool(5).invokeAll(threads);
    }

    @Test
    void test_png() throws IOException {
        Path target = Files.createTempFile("test3", ".jpg");

        service.convert(new ImageMagickBuilder().source(Path.of("src/test/resources/test3.png")).target(target)
                                                .quality(75).resize(1200, 1200).strip());
        log.info("Output file: {}", target);
    }

}
