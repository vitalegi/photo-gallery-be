package it.vitalegi.imageviewer.media.service;


import it.vitalegi.imageviewer.cmd.ImageMagickBuilder;
import it.vitalegi.imageviewer.cmd.ImageMagickService;
import it.vitalegi.metrics.Performance;
import it.vitalegi.metrics.Type;
import it.vitalegi.util.FileUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Performance(Type.SERVICE)
@Log4j2
@Service
public class ImageMediaStrategy extends AbstractMediaStrategy {

    final static List<String> EXTENSIONS = Arrays.asList("jpeg", "jpg", "png", "heic");

    @Autowired
    ImageMagickService imageMagickService;

    @Override
    public String getFilename(Path path) {
        String filename = FileUtil.getFilename(path);
        int extensionStart = filename.lastIndexOf(".");
        if (extensionStart == -1) {
            throw new IllegalArgumentException("File doesn't have an extension: " + path);
        }
        return filename.substring(0, extensionStart) + ".jpg";
    }

    @Override
    public void scale(Path input, Path output) {
        var builder = new ImageMagickBuilder().source(input).target(output).quality(75).resize(1200, 1200).strip();
        imageMagickService.convert(builder);
    }

    @Override
    protected List<String> getSupportedExtensions() {
        return EXTENSIONS;
    }


}
