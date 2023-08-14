package it.vitalegi.imageviewer.media.service;

import it.vitalegi.imageviewer.media.model.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Log4j2
public class BulkFeedImportService {

    @Autowired
    FeedService feedService;

    public void importResources(List<Resource> resources) {
        int tot = resources.size();
        AtomicInteger done = new AtomicInteger(0);
        AtomicInteger errors = new AtomicInteger(0);
        ForkJoinPool customThreadPool = new ForkJoinPool(10);
        customThreadPool.submit(() -> resources.stream().parallel().forEach(r -> convert(r, tot, done, errors)));
    }

    protected void convert(Resource resource, int tot, AtomicInteger done, AtomicInteger errors) {
        try {
            feedService.convert(resource);
            done.incrementAndGet();
        } catch (RuntimeException e) {
            errors.incrementAndGet();
            log.error("Failed to process {}", resource, e);
        }
        log.info("Status: OK={}, KO={}, tot={}", done, errors, tot);
    }
}
