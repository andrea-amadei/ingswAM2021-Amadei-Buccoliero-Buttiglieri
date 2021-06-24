package it.polimi.ingsw.client.gui;

import javafx.fxml.FXMLLoader;

import java.util.HashMap;
import java.util.Map;

public class FXMLCachedLoaders {
    private Map<String, FXMLLoader> cachedLoaders = new HashMap<>();
    private static final FXMLCachedLoaders instance = new FXMLCachedLoaders();

    private FXMLCachedLoaders(){
    }

    public static FXMLCachedLoaders getInstance(){
        return instance;
    }

    public void addLoader(String name, FXMLLoader loader){
        cachedLoaders.put(name, loader);
    }

    public boolean isLoaderContained(String name){
        return cachedLoaders.containsKey(name);
    }

    public FXMLLoader getLoader(String name){
        return cachedLoaders.get(name);
    }
}
