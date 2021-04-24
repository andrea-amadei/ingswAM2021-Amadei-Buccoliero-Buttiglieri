package it.polimi.ingsw.parser;

import com.google.gson.Gson;

public final class JSONSerializer {
    private static final Gson gson = new Gson();

    private JSONSerializer() { }

    public static String toJson(RawObject<?> rawObject) {
        return gson.toJson(rawObject);
    }
}
