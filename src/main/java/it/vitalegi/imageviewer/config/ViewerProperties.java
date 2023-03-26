package it.vitalegi.imageviewer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "viewer")
public class ViewerProperties {
    String output;
    List<PathConfig> paths;
}
