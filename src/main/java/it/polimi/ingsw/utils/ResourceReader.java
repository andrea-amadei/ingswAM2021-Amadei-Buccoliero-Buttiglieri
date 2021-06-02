package it.polimi.ingsw.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

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
}
