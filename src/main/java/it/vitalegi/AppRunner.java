package it.vitalegi;

import it.vitalegi.imageviewer.config.Feed;
import it.vitalegi.imageviewer.media.service.FeedService;
import it.vitalegi.util.YamlUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Log4j2
@Component
@Profile("prod")
public class AppRunner implements CommandLineRunner {

    @Autowired
    FeedService feedService;

    @Override
    public void run(String... args) throws Exception {
        var feeds = YamlUtil.loadConfig(Path.of("config.yml"));
        feeds.getFeeds().forEach(this::process);
    }

    void process(Feed feed) {
        log.info("Create index for {}", feed);
        feedService.createIndex(feed);
        log.info("Index created");
        var resources = feedService.getResources();
        feedService.importResources(resources);
    }

}
