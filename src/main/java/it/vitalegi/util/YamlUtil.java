package it.vitalegi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import it.vitalegi.imageviewer.config.Feeds;

import java.io.IOException;
import java.nio.file.Path;

public class YamlUtil {
    public static Feeds loadConfig(Path path) {
        var om = new ObjectMapper(new YAMLFactory());
        om.findAndRegisterModules();

        try {
            return om.readValue(path.toFile(), Feeds.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
