package it.vitalegi.imageviewer.media.model;

import lombok.Data;

import java.nio.file.Path;
import java.util.List;

@Data
public class Resource {
    Path sourcePath;
    Path targetPath;
    ImportStatus status;
    List<String> tags;
}
