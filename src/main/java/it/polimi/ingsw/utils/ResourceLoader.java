package it.polimi.ingsw.utils;

import it.polimi.ingsw.JFXTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.stream.Collectors;

public final class ResourceLoader {
    private ResourceLoader() { }

    public static Path getPathFromResource(String fileName) {
        URL resource = ResourceLoader.class.getProtectionDomain().getClassLoader().getResource(fileName);

        if (resource == null)
            throw new IllegalArgumentException("File not found! " + fileName);

        try {
            return Path.of(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to load file " + fileName + ": " + e.getMessage());
        }
    }

    public static InputStream getStreamFromResource(String fileName) {
        InputStream inputStream = ResourceLoader.class.getProtectionDomain().getClassLoader().getResourceAsStream(fileName);

        if(inputStream == null)
            throw new IllegalArgumentException("File not found! " + fileName);

        return inputStream;
    }

    public static String loadFile(String fileName) {
        InputStream in = getStreamFromResource(fileName);

        return new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
    }

    public static Parent loadFXML(String fileName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ResourceLoader.class.getProtectionDomain().getClassLoader().getResource(fileName));
            return fxmlLoader.load();
        } catch (Exception e) {
            throw new IllegalArgumentException("File not found! " + fileName);
        }
    }


}
