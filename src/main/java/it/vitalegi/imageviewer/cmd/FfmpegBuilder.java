package it.vitalegi.imageviewer.cmd;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FfmpegBuilder {

    Path source;
    Path target;
    String scale;

    public String[] build() {
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-y");
        if (source == null) {
            throw new IllegalArgumentException("Source is mandatory");
        }
        command.add("-i");
        command.add("\"" + source.toString() + "\"");
        if (scale != null) {
            command.add("-vf");
            command.add(scale);
        }
        if (target != null) {
            command.add("\"" + target.toString() + "\"");
        }
        return command.toArray(new String[command.size()]);
    }


    public FfmpegBuilder scaleWidth(int w) {
        this.scale = "scale=" + w + ":-2";
        return this;
    }

    public FfmpegBuilder source(Path source) {
        this.source = source;
        return this;
    }


    public FfmpegBuilder target(Path target) {
        this.target = target;
        return this;
    }
}
