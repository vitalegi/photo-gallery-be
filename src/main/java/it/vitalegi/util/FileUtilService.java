package it.vitalegi.util;

import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class FileUtilService {

    public void createDirectories(Path directory) {
        FileUtil.createDirectories(directory);
    }
}
