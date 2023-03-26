package it.vitalegi.imageviewer.cmd;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StreamRecorder implements Runnable {

    InputStream inputStream;
    @Getter
    List<String> lines;

    public StreamRecorder(InputStream inputStream) {
        this.inputStream = inputStream;
        lines = new ArrayList<>();
    }

    @Override
    public void run() {
        new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(lines::add);
    }
}
