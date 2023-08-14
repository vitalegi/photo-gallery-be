package it.vitalegi.imageviewer.media.service;

import it.vitalegi.exception.NoSuchStrategyException;
import it.vitalegi.imageviewer.config.Feed;
import it.vitalegi.metrics.Performance;
import it.vitalegi.metrics.Type;
import it.vitalegi.util.FileUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Log4j2
@Service
public class MediaService {

    @Autowired
    List<AbstractMediaStrategy> strategies;

    public Stream<Path> getAllMedia(Path path) {
        return FileUtil.listFiles(path).filter(this::accept);
    }

    public boolean accept(Path path) {
        return getStrategy(path) != null;
    }

    protected AbstractMediaStrategy getStrategy(Path path) {
        return strategies.stream().filter(strategy -> strategy.accept(path)).findFirst().orElse(null);
    }

    public String getFilename(Path path) {
        return checkStrategy(path).getFilename(path);
    }

    protected AbstractMediaStrategy checkStrategy(Path path) {
        var s = getStrategy(path);
        if (s == null) {
            throw new NoSuchStrategyException();
        }
        return s;
    }

    public Path getOutputPath(Feed feed, Path input, String filename) {
        Path relativePath = FileUtil.getRelativePath(feed.getSource(), input);
        if (relativePath.getParent() != null) {
            return feed.getTarget().resolve(relativePath.getParent()).resolve(filename);
        }
        return feed.getTarget().resolve(filename);
    }

    public void scale(Path input, Path output) {
        checkStrategy(input).scale(input, output);
    }

}
