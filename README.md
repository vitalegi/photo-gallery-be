# Image Viewer

Utility to simplify the presentation of image galleries.

## Roadmap

### Phase 1

Create a copy of all images, resizing to a web format and renaming them in order to have natural alphabetical sort.

Collect metadata from images.

### Phase 2

Resize video.

### Phase 3

Create website to present images in a nicer way, add sync / configuration options.

## Prerequisites

- JDK 17
- Maven
- Imagemagick

```
$env:M2_HOME = 'C:\a\software\apache-maven-3.8.7-java18'
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-17.0.5'
$env:PATH = $env:M2_HOME + '\bin;' + $env:JAVA_HOME + '\bin;' + $env:PATH
```

```
magick -version
Version: ImageMagick 7.1.0-19 Q16-HDRI x64 2021-12-22 https://imagemagick.org
Copyright: (C) 1999-2021 ImageMagick Studio LLC
License: https://imagemagick.org/script/license.php
Features: Cipher DPC HDRI Modules OpenCL OpenMP(2.0)
Delegates (built-in): bzlib cairo flif freetype gslib heic jng jp2 jpeg jxl lcms lqr lzma openexr pangocairo png ps raqm raw rsvg tiff webp xml zip zlib
```

## Compile

```bash
mvn clean package
```

## Run

```bash
$env:DATASOURCE_URL = 'jdbc:postgresql://localhost:5432/viewer'
$env:DATASOURCE_USERNAME = 'postgres'
$env:DATASOURCE_PASSWORD = 'postgres'
java -jar "-Dspring.profiles.active=prod" ./target/image-viewer-*.jar

./mvnw 

```

## Generate Database Schema

<https://docs.liquibase.com/start/install/liquibase-windows.html>

```
liquibase "--url=jdbc:h2:./liquibase;DB_CLOSE_ON_EXIT=FALSE" "--username=sa" "--password=" "--driver=org.h2.Driver" "--changelog-file=dbchangelog.xml" generate-changelog
```

## Check differences between 2 databases

```
$oldDb="./db/00002/liquibase"
liquibase "--changelog-file=dbchangelog.xml" "--url=jdbc:h2:${oldDb};DB_CLOSE_ON_EXIT=FALSE" "--username=sa" "--password=" "--referenceUrl=jdbc:h2:./liquibase;DB_CLOSE_ON_EXIT=FALSE" "--referenceUsername=sa" "--referencePassword=" diff-changelog
```

## OpenApi definition

| Description  | Local                                                       |
|--------------|-------------------------------------------------------------|
| OpenApi WEB  | [/swagger-ui/](http://localhost:8080/swagger-ui/index.html) |
| OpenApi JSON | [/v3/api-docs](http://localhost:8080/v3/api-docs)           |
| OpenApi YAML | [/v3/api-docs.yaml](http://localhost:8080/v3/api-docs.yaml) |
