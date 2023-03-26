package it.vitalegi.imageviewer.media.service;

import it.vitalegi.util.FileUtil;
import lombok.extern.log4j.Log4j2;

import java.nio.file.Path;
import java.util.List;

@Log4j2
public abstract class AbstractMediaStrategy {
    public boolean accept(Path path) {
        var extension = FileUtil.getExtension(path).toLowerCase();
        return getSupportedExtensions().contains(extension);
    }

    public abstract String getFilename(Path path);

    public abstract void scale(Path input, Path output);

    protected abstract List<String> getSupportedExtensions();

}
