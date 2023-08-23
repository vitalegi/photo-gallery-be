package it.vitalegi.imageviewer.media.service;


import it.vitalegi.imageviewer.cmd.FfmpegBuilder;
import it.vitalegi.imageviewer.cmd.FfmpegService;
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
public class VideoMediaStrategy extends AbstractMediaStrategy {

    final static List<String> EXTENSIONS = Arrays.asList("avi", "mov", "mp4");

    @Autowired
    FfmpegService ffmpegService;

    @Override
    public String getFilename(Path path) {
        String filename = FileUtil.getFilename(path);
        int extensionStart = filename.lastIndexOf(".");
        if (extensionStart == -1) {
            throw new IllegalArgumentException("File doesn't have an extension: " + path);
        }
        return filename.substring(0, extensionStart) + ".mp4";
    }

    @Override
    public void scale(Path input, Path output) {
        var builder = new FfmpegBuilder().source(input).target(output).scaleWidth(500);
        ffmpegService.convert(builder);
    }

    @Override
    protected List<String> getSupportedExtensions() {
        return EXTENSIONS;
    }


}
