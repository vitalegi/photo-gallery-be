package it.vitalegi;

import it.vitalegi.imageviewer.media.config.PathConfig;
import it.vitalegi.imageviewer.media.config.ViewerProperties;
import it.vitalegi.imageviewer.media.service.ImageService;
import it.vitalegi.imageviewer.media.service.MediaService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Component
@Profile("prod")
public class AppRunner implements CommandLineRunner {

    @Autowired
    ViewerProperties viewerProperties;
    @Autowired
    MediaService mediaService;
    @Autowired
    ImageService imageService;

    @Override
    public void run(String... args) throws Exception {
        viewerProperties.getPaths().forEach(this::process);
    }

    void process(PathConfig config) {
        log.info("Process {}", config.getName());
        mediaService.getImages(Paths.get(config.getPath())).forEach(this::process);
    }

    void process(Path image) {
        log.info("> {}", image);
        var strategy = imageService.getImageStrategy(image);
        strategy.scale(image, 300);
    }
}
