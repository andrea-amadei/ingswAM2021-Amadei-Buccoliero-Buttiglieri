package it.polimi.ingsw.common.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.server.model.basetypes.ResourceGroup;
import it.polimi.ingsw.server.model.basetypes.ResourceSingle;
import it.polimi.ingsw.common.parser.adapters.PayloadComponentAdapter;
import it.polimi.ingsw.common.parser.adapters.ResourceGroupAdapter;
import it.polimi.ingsw.common.parser.adapters.ResourceSingleAdapter;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

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

    public static String toJson(List<PayloadComponent> payloadComponents){
        Gson myGson = new GsonBuilder()
                .registerTypeAdapter(PayloadComponent.class, new PayloadComponentAdapter())
                .registerTypeAdapter(ResourceSingle.class, new ResourceSingleAdapter())
                .registerTypeAdapter(ResourceGroup.class, new ResourceGroupAdapter())
                .create();

        Type payloadListType = new TypeToken<List<PayloadComponent>>(){}.getType();

        return myGson.toJson(payloadComponents, payloadListType);
    }



    public static String toJson(Collection<?> collection) {
        return gson.toJson(collection);
    }
}
