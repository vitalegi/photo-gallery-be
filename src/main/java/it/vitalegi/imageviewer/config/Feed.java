package it.vitalegi.imageviewer.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Feed {
    Path source;
    Path target;
}
