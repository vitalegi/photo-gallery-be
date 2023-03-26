package it.vitalegi.imageviewer.media.service;


import it.vitalegi.imageviewer.media.service.image.AbstractImageStrategy;
import it.vitalegi.metrics.Performance;
import it.vitalegi.metrics.Type;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Performance(Type.SERVICE)
@Log4j2
@Service
public class ImageService {

    @Autowired
    List<AbstractImageStrategy> imageStrategies;

    public boolean accept(Path path) {
        return getImageStrategy(path) != null;
    }

    public AbstractImageStrategy getImageStrategy(Path path) {
        return imageStrategies.stream().filter(strategy -> strategy.accept(path)).findFirst().orElse(null);
    }

}
