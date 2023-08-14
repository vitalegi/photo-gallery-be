package it.vitalegi.imageviewer.media.service;

import it.vitalegi.imageviewer.media.model.ImportStatus;
import it.vitalegi.imageviewer.media.model.Resource;
import it.vitalegi.imageviewer.media.repository.FileResourceRepository;
import it.vitalegi.util.FileUtilService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class FeedServiceTests {
    private static final Path SOURCE_1_DIR = Path.of("a/b");
    private static final Path SOURCE_1 = SOURCE_1_DIR.resolve("c1.jpeg");
    private static final Path TARGET_1_DIR = Path.of("out/b");
    private static final Path TARGET_1 = TARGET_1_DIR.resolve("c1.png");

    @InjectMocks
    FeedService service;
    @Mock
    MediaService mediaService;
    @Mock
    FileResourceRepository resourceRepository;
    @Mock
    FileUtilService fileUtilService;


    @Test
    void test_convert_error() {
        var r = new Resource();

        r.setSourcePath(SOURCE_1);
        r.setTargetPath(TARGET_1);
        Mockito.doThrow(RuntimeException.class).when(mediaService).scale(any(), any());
        var e = assertThrows(RuntimeException.class, () -> service.convert(r));
        verify(resourceRepository, times(1)).updateStatus(r, ImportStatus.ONGOING);
        verify(fileUtilService, times(1)).createDirectories(TARGET_1);
        verify(mediaService, times(1)).scale(SOURCE_1, TARGET_1);
        verify(resourceRepository, times(1)).updateStatus(r, ImportStatus.ERROR);
    }

    @Test
    void test_convert_success() {
        var r = new Resource();

        r.setSourcePath(SOURCE_1);
        r.setTargetPath(TARGET_1);
        service.convert(r);
        verify(resourceRepository, times(1)).updateStatus(r, ImportStatus.ONGOING);
        verify(fileUtilService, times(1)).createDirectories(TARGET_1);
        verify(mediaService, times(1)).scale(SOURCE_1, TARGET_1);
        verify(resourceRepository, times(1)).updateStatus(r, ImportStatus.DONE);
    }
}