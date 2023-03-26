package it.vitalegi.imageviewer.cmd;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@Service
public class ImageMagickService {

    @Autowired
    CommandLineRunner runner;

    public void convert(ImageMagickBuilder command) {
        var target = command.target;
        if (target == null) {
            throw new IllegalArgumentException("target is mandatory");
        }
        var out = runner.execute(command.build());
        if (out.getExitCode() != 0) {
            throw new RuntimeException("Failed processing command with error " + out.getExitCode() + ": " + printable(command));
        }
        if (!Files.exists(target)) {
            throw new RuntimeException("File was not created. File: " + target);
        }
        if (!Files.isRegularFile(target)) {
            throw new RuntimeException("Target is not a file. " + target);
        }
        try {
            long size = Files.size(target);
            if (size <= 0) {
                throw new RuntimeException("File size is invalid. File: " + target + ". Size: " + size);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String printable(ImageMagickBuilder command) {
        return String.join(" ", command.build());
    }
}
