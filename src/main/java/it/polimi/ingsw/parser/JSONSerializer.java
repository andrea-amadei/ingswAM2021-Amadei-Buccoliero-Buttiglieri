package it.polimi.ingsw.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.gamematerials.ResourceGroup;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.parser.adapters.PayloadComponentAdapter;
import it.polimi.ingsw.parser.adapters.ResourceGroupAdapter;
import it.polimi.ingsw.parser.adapters.ResourceSingleAdapter;

import java.util.Collection;

public final class JSONSerializer {
    private static final Gson gson = new Gson();

    private JSONSerializer() { }

    public static String toJson(RawObject<?> rawObject) {
        return gson.toJson(rawObject);
    }

    public static String toJson(PayloadComponent payloadComponent) {
        Gson myGson = new GsonBuilder()
                                .registerTypeAdapter(PayloadComponent.class, new PayloadComponentAdapter())
                                .registerTypeAdapter(ResourceSingle.class, new ResourceSingleAdapter())
                                .registerTypeAdapter(ResourceGroup.class, new ResourceGroupAdapter())
                                .create();
        return myGson.toJson(payloadComponent, PayloadComponent.class);
    }



    public static String toJson(Collection<?> collection) {
        return gson.toJson(collection);
    }
}
