package it.vitalegi.imageviewer.cmd;

import lombok.Getter;

import java.util.List;

@Getter
public class ProcessOutput {
    List<String> out;
    List<String> err;
    int exitCode;

    public ProcessOutput(int exitCode, List<String> out, List<String> err) {
        this.exitCode = exitCode;
        this.out = out;
        this.err = err;
    }
}
