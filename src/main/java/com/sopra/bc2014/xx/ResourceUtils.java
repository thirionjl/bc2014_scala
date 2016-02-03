package com.sopra.bc2014.xx;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ResourceUtils {

    public static Stream<String> getLineStream(String classpathLocation,
        Charset cs) throws IOException {
        return Files.lines(getPath(classpathLocation), cs);
    }

    public static Stream<String> getLineStream(String classpathLocation)
        throws IOException {
        return getLineStream(classpathLocation, Charset.forName("ISO-8859-1"));
    }

    public static Path getPath(String classpathLocation) {
        try {
            return Paths.get(CodeBreaker2.class.getResource(classpathLocation)
                .toURI());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}