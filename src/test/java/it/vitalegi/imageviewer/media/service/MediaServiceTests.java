package it.vitalegi.imageviewer.media.service;

import it.vitalegi.exception.NoSuchStrategyException;
import it.vitalegi.imageviewer.config.Feed;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Log4j2
public class MediaServiceTests {

    MediaService ms;
    AbstractMediaStrategy s1;
    AbstractMediaStrategy s2;

    @BeforeEach
    void init() {
        ms = new MediaService();
        s1 = mock(AbstractMediaStrategy.class);
        s2 = mock(AbstractMediaStrategy.class);
        ms.strategies = Arrays.asList(s1, s2);
    }

    @Test
    void test_accept() {
        var p = Path.of("a", "b", "img.jpeg");
        when(s1.accept(p)).thenReturn(false);
        when(s2.accept(p)).thenReturn(true);
        assertTrue(ms.accept(p));
    }

    @Test
    void test_accept_noStrategy() {
        var p = Path.of("a", "b", "img.jpeg");
        when(s1.accept(p)).thenReturn(false);
        when(s2.accept(p)).thenReturn(false);
        assertFalse(ms.accept(p));
    }

    @Test
    void test_getFilename() {
        var p = Path.of("a", "b", "img.jpeg");
        when(s1.accept(p)).thenReturn(false);
        when(s2.accept(p)).thenReturn(true);
        when(s2.getFilename(p)).thenReturn("img2.jpeg");
        var name = ms.getFilename(p);
        assertEquals("img2.jpeg", name);
    }

    @Test
    void test_getFilename_noStrategy() {
        var p = Path.of("a", "b", "img.jpeg");
        when(s1.accept(p)).thenReturn(false);
        when(s2.accept(p)).thenReturn(false);
        assertThrows(NoSuchStrategyException.class, () -> ms.getFilename(p));
    }

    @Test
    void test_getOutputPath_absolutePath() {
        var feed = new Feed(Path.of("C:/a/b"), Path.of("C:/a/out"));
        var path = ms.getOutputPath(feed, Path.of("C:/a/b/c/f.png"), "f.jpeg");
        assertEquals(Path.of("C:/a/out/c/f.jpeg"), path);
        path = ms.getOutputPath(feed, Path.of("C:/a/b/f.png"), "f.jpeg");
        assertEquals(Path.of("C:/a/out/f.jpeg"), path);
    }

    @Test
    void test_getOutputPath_relativePath() {
        var feed = new Feed(Path.of("../a/b"), Path.of("../../b/out"));
        var path = ms.getOutputPath(feed, Path.of("../a/b/c/f.png"), "f.jpeg");
        assertEquals(Path.of("../../b/out/c/f.jpeg"), path);
        path = ms.getOutputPath(feed, Path.of("../a/b/f.png"), "f.jpeg");
        assertEquals(Path.of("../../b/out/f.jpeg"), path);
    }

    @Test
    void test_getStrategy() {
        var p = Path.of("a", "b", "img.jpeg");
        when(s1.accept(p)).thenReturn(false);
        when(s2.accept(p)).thenReturn(true);
        var s = ms.getStrategy(p);
        assertEquals(s2, s);
    }

    @Test
    void test_getStrategy_noStrategy() {
        var p = Path.of("a", "b", "img.jpeg");
        when(s1.accept(p)).thenReturn(false);
        when(s2.accept(p)).thenReturn(false);
        var s = ms.getStrategy(p);
        assertNull(s);
    }

    @Test
    void test_scale() {
        var p = Path.of("a", "b", "img.jpeg");
        when(s1.accept(p)).thenReturn(false);
        when(s2.accept(p)).thenReturn(true);
        var out = Path.of("out", "img.jpeg");
        doNothing().when(s2).scale(p, out);
        ms.scale(p, out);
        verify(s2, times(1)).scale(p, out);
    }

    @Test
    void test_scale_noStrategy() {
        var p = Path.of("a", "b", "img.jpeg");
        when(s1.accept(p)).thenReturn(false);
        when(s2.accept(p)).thenReturn(false);
        var out = Path.of("out", "img.jpeg");
        var e = assertThrows(NoSuchStrategyException.class, () -> ms.scale(p, out));
    }

}
