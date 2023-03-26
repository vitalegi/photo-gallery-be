package it.vitalegi.imageviewer.media.service.image;

import it.vitalegi.util.FileUtil;
import lombok.extern.log4j.Log4j2;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.List;

@Log4j2
public abstract class AbstractImageStrategy {
    public boolean accept(Path path) {
        var extension = FileUtil.getExtension(path).toLowerCase();
        return getSupportedExtensions().contains(extension);
    }

    public BufferedImage scale(Path path, int width) {
        var image = getBufferedImage(path);
        var actualWidth = image.getWidth();
        var actualHeight = image.getHeight();
        double ratio = (1.0 * actualWidth) / actualHeight;
        int targetHeight = (int) (width * ratio);
        log.info("Scale image from {}x{} to {}x{}", actualWidth, actualHeight, width, targetHeight);
        return resizeImage(image, width, targetHeight);
    }

    protected abstract BufferedImage getBufferedImage(Path path);

    protected abstract List<String> getSupportedExtensions();

    protected abstract BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight);
}
