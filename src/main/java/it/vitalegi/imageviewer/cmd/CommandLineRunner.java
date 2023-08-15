package it.vitalegi.imageviewer.cmd;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Log4j2
@Service
public class CommandLineRunner {

    public ProcessOutput execute(String... command) {
        try {
            return doExecute(command);
        } catch (InterruptedException | IOException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    protected ProcessOutput doExecute(String... command) throws ExecutionException, InterruptedException, IOException {
        log.debug("Start process for {}", String.join(" ", command));
        Process process = new ProcessBuilder(command).start();
        var stdOut = new StreamRecorder(process.getInputStream());
        var stdErr = new StreamRecorder(process.getErrorStream());
        Future<?> futureOut = Executors.newSingleThreadExecutor().submit(stdOut);
        Future<?> futureErr = Executors.newSingleThreadExecutor().submit(stdErr);
        int exitCode = process.waitFor();
        log.debug("Process is completed");
        if (exitCode != 0) {
            log.error("Completed with exit code {}, command {}", exitCode, String.join(" ", command));
        }
        futureOut.get();
        if (!stdOut.getLines().isEmpty()) {
            stdOut.getLines().forEach(log::info);
        }
        futureErr.get();
        if (!stdErr.getLines().isEmpty()) {
            stdErr.getLines().forEach(log::error);
        }
        return new ProcessOutput(exitCode, stdOut.getLines(), stdErr.getLines());
    }
}
