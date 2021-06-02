package it.polimi.ingsw.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.stream.Collectors;

public final class ResourceReader {
    private ResourceReader() { }

    public static File getFileFromResource(String fileName) {
        URL resource = ResourceReader.class.getProtectionDomain().getClassLoader().getResource(fileName);

        if (resource == null)
            throw new IllegalArgumentException("File not found! " + fileName);

        try {
            return new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to load file " + fileName + ": " + e.getMessage());
        }
    }

    public static Path getPathFromResource(String fileName) {
        URL resource = ResourceReader.class.getProtectionDomain().getClassLoader().getResource(fileName);

        if (resource == null)
            throw new IllegalArgumentException("File not found! " + fileName);

        try {
            return Path.of(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to load file " + fileName + ": " + e.getMessage());
        }
    }

    public static InputStream getStreamFromResource(String fileName) {
        InputStream inputStream = ResourceReader.class.getProtectionDomain().getClassLoader().getResourceAsStream(fileName);

        if(inputStream == null)
            throw new IllegalArgumentException("File not found! " + fileName);

        return inputStream;
    }

    public static String getStringFromResource(String fileName) {
        InputStream in = getStreamFromResource(fileName);

        return new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
    }
}
