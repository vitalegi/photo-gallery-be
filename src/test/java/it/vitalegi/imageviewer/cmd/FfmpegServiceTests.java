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
public class FfmpegServiceTests {

    FfmpegService service;
    CommandLineRunner runner;

    Callable<Boolean> fakeThread() {
        return () -> {
            Path target = Files.createTempFile("test2", ".jpg");
            service.convert(new FfmpegBuilder().source(Path.of("src/test/resources/test2.heic")).target(target).scaleWidth(500));
            return true;
        };
    }

    @BeforeEach
    void init() {
        runner = new CommandLineRunner();
        service = new FfmpegService();
        service.runner = runner;
    }

    @Test
    void test_mp4() throws IOException {
        Path target = Files.createTempFile("test5", ".mp4");

        service.convert(new FfmpegBuilder().source(Path.of("src/test/resources/test5.mp4")).target(target).scaleWidth(500));
        log.info("Output file: {}", target);
    }

    @Test
    void test_mov() throws IOException {
        Path target = Files.createTempFile("test4", ".mp4");
        service.convert(new FfmpegBuilder().source(Path.of("src/test/resources/test4.MOV")).target(target).scaleWidth(500));
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
}
