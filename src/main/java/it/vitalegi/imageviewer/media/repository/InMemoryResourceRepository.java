package it.vitalegi.imageviewer.media.repository;

import it.vitalegi.imageviewer.media.model.ImportStatus;
import it.vitalegi.imageviewer.media.model.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@Log4j2
@Service
public class InMemoryResourceRepository {

    List<Resource> resources = new ArrayList<>();

    public Resource addResource(Path source, Path target) {
        if (hasResource(source)) {
            throw new IllegalArgumentException("Path " + source + " already indexed");
        }
        return doAddResource(source, target);
    }

    public boolean hasResource(Path sourcePath) {
        return resources.stream().anyMatch(r -> r.getSourcePath().equals(sourcePath));
    }

    protected Resource doAddResource(Path source, Path target) {
        var resource = new Resource();
        resource.setSourcePath(source);
        resource.setTargetPath(target);
        resource.setTags(new ArrayList<>());
        resource.setStatus(ImportStatus.PENDING);
        resources.add(resource);
        return resource;
    }

    public Stream<Resource> stream() {
        return resources.stream();
    }

    public void updateStatus(Resource resource, ImportStatus status) {
        resource.setStatus(status);
    }
}
