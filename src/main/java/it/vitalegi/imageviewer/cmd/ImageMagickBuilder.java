package it.vitalegi.imageviewer.cmd;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ImageMagickBuilder {

    Path source;
    Path target;
    String resize;
    Integer quality;
    boolean strip;

    public String[] build() {
        List<String> command = new ArrayList<>();
        command.add("magick");
        if (source == null) {
            throw new IllegalArgumentException("Source is mandatory");
        }
        command.add("\"" + source.toString() + "\"");
        if (resize != null) {
            command.add("-resize");
            command.add(resize);
        }
        if (quality != null) {
            command.add("-quality");
            command.add("" + quality);
        }
        if (strip) {
            command.add("-strip");
        }
        if (target != null) {
            command.add("\"" + target.toString() + "\"");
        }
        return command.toArray(new String[command.size()]);
    }

    public ImageMagickBuilder quality(int quality) {
        this.quality = quality;
        return this;
    }

    public ImageMagickBuilder resize(int w, int h) {
        this.resize = w + "x" + h;
        return this;
    }

    public ImageMagickBuilder source(Path source) {
        this.source = source;
        return this;
    }

    public ImageMagickBuilder strip() {
        this.strip = true;
        return this;
    }

    public ImageMagickBuilder target(Path target) {
        this.target = target;
        return this;
    }
    //%{convert $_.FullName -resize 1200x1200 -quality 75 -strip "$_"}
    //%{magick $_.FullName -resize 1200x1200 -quality 75 -strip "$_.jpg"}
    //%{magick $_.FullName -resize 1200x1200 -quality 75 -strip "$_.jpg"}

}
