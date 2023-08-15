package it.vitalegi.imageviewer.media.service;

import it.vitalegi.imageviewer.config.Feed;
import it.vitalegi.imageviewer.media.model.ImportStatus;
import it.vitalegi.imageviewer.media.model.Resource;
import it.vitalegi.imageviewer.media.repository.FileResourceRepository;
import it.vitalegi.util.FileUtilService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Log4j2
@Service
public class FeedService {

    @Autowired
    MediaService mediaService;

    @Autowired
    FileUtilService fileUtilService;
    @Autowired
    FileResourceRepository resourceRepository;

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

    public void importResources(List<Resource> resources) {
        log.info("Start import {}", resources.size());
        int tot = resources.size();
        AtomicInteger done = new AtomicInteger(0);
        AtomicInteger errors = new AtomicInteger(0);
        ForkJoinPool customThreadPool = new ForkJoinPool(20);
        customThreadPool.submit(() -> resources.parallelStream() //
                                               .forEach(r -> convert(r, tot, done, errors)));
    }

    protected boolean isAlreadyImported(Resource resource) {
        var target = resource.getTargetPath().toFile();
        if (!target.exists()) {
            log.info("{} doesn't exist", target);
            return false;
        }
        if (!target.isFile()) {
            log.info("{} isn't a file", target);
            return false;
        }
        if (!Files.isRegularFile(resource.getTargetPath())) {
            log.info("{} isn't a regular file", target);
            return false;
        }
        try {
            long size = Files.size(resource.getTargetPath());
            if (size <= 0) {
                log.info("{} size is {}", target, size);
                return false;
            }
        } catch (IOException e) {
            log.error("Error while processing size of {}", target, e);
            return false;
        }
        return true;
    }

    protected void convert(Resource resource, int tot, AtomicInteger done, AtomicInteger errors) {
        try {
            if (isAlreadyImported(resource)) {
                log.info("Already imported {}, skip", resource.getSourcePath());
                done.incrementAndGet();
                return;
            }
            convert(resource);
            done.incrementAndGet();
        } catch (RuntimeException e) {
            errors.incrementAndGet();
            log.error("Failed to process {}", resource, e);
        } finally {
            log.info("Status: OK={}, KO={}, tot={}", done, errors, tot);
        }
    }

    public void convert(Resource resource) {
        try {
            resourceRepository.updateStatus(resource, ImportStatus.ONGOING);
            fileUtilService.createDirectories(resource.getTargetPath());
            mediaService.scale(resource.getSourcePath(), resource.getTargetPath());
            resourceRepository.updateStatus(resource, ImportStatus.DONE);
        } catch (Exception e) {
            resourceRepository.updateStatus(resource, ImportStatus.ERROR);
            throw new RuntimeException(e);
        }
    }
}
