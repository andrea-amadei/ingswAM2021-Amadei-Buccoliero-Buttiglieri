package it.polimi.ingsw;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.gamematerials.ResourceGroup;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.parser.adapters.ResourceGroupAdapter;
import it.polimi.ingsw.parser.adapters.ResourceSingleAdapter;
import it.polimi.ingsw.parser.adapters.ResourceTypeDeserializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceAdapterTest {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(ResourceSingle.class, new ResourceSingleAdapter())
            .registerTypeAdapter(ResourceGroup.class, new ResourceGroupAdapter())
            .registerTypeAdapter(ResourceType.class, new ResourceTypeDeserializer())
            .create();

    private static final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();


    @Test
    public void serializeResourceSingle(){
        String serializedGold = gson.toJson(gold);
        assertEquals("\"gold\"", serializedGold);
    }

    @Test
    public void deserializeResourceSingleAsResourceType(){
        ResourceType myGold = gson.fromJson("\"gold\"", ResourceType.class);
        assertEquals("gold", myGold.getId());
    }
}
