package it.polimi.ingsw.parser;

import com.google.gson.Gson;

import java.util.Collection;

public final class JSONSerializer {
    private static final Gson gson = new Gson();

    private JSONSerializer() { }

    public static String toJson(RawObject<?> rawObject) {
        return gson.toJson(rawObject);
    }

    public static String toJson(Collection<?> collection) {
        return gson.toJson(collection);
    }
}
