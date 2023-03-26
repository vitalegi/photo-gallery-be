package it.vitalegi.imageviewer.media.service;

import it.vitalegi.imageviewer.media.config.ViewerProperties;
import it.vitalegi.metrics.Performance;
import it.vitalegi.metrics.Type;
import it.vitalegi.util.FileUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Performance(Type.SERVICE)
@Log4j2
@Service
public class MediaService {


    @Autowired
    ImageService imageService;

    public List<Path> getImages(Path path) {
        return FileUtil.listFiles(path).filter(imageService::accept).collect(Collectors.toList());
    }
}
