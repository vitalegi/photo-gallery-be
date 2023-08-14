package it.vitalegi.imageviewer.media.service;

import it.vitalegi.imageviewer.config.Feed;
import it.vitalegi.imageviewer.media.model.ImportStatus;
import it.vitalegi.imageviewer.media.model.Resource;
import it.vitalegi.imageviewer.media.repository.InMemoryResourceRepository;
import it.vitalegi.metrics.Performance;
import it.vitalegi.metrics.Type;
import it.vitalegi.util.FileUtilService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class FeedService {

    @Autowired
    MediaService mediaService;

    @Autowired
    FileUtilService fileUtilService;
    @Autowired
    InMemoryResourceRepository resourceRepository;

    public void convert(Resource resource) {
        try {
            resourceRepository.updateStatus(resource, ImportStatus.ONGOING);
            log.info("Process {} to {}", resource.getSourcePath(), resource.getTargetPath());
            fileUtilService.createDirectories(resource.getTargetPath());
            mediaService.scale(resource.getSourcePath(), resource.getTargetPath());
            resourceRepository.updateStatus(resource, ImportStatus.DONE);
        } catch (Exception e) {
            resourceRepository.updateStatus(resource, ImportStatus.ERROR);
            throw new RuntimeException(e);
        }
    }

    public void createIndex(Feed feed) {
        log.info("Create index for {} => {}", feed.getSource(), feed.getTarget());
        var medias = mediaService.getAllMedia(feed.getSource()).collect(Collectors.toList());
        log.info("Found {} media", medias.size());
        medias.forEach(source -> createIndex(feed, source));
    }

    protected Resource createIndex(Feed feed, Path source) {
        String targetFilename = mediaService.getFilename(source);
        Path targetPath = mediaService.getOutputPath(feed, source, targetFilename);
        return resourceRepository.addResource(source, targetPath);
    }

    public List<Resource> getResources() {
        return resourceRepository.stream().collect(Collectors.toList());
    }
}
