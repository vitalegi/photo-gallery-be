package it.vitalegi.imageviewer.media.service.image;


import it.vitalegi.metrics.Performance;
import it.vitalegi.metrics.Type;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Performance(Type.SERVICE)
@Log4j2
@Service
public class HeicStrategy extends AbstractImageStrategy {

    final static List<String> EXTENSIONS = Arrays.asList("heic");

    protected BufferedImage getBufferedImage(Path path) {
        try {
            return ImageIO.read(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<String> getSupportedExtensions() {
        return EXTENSIONS;
    }

    protected BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

}
