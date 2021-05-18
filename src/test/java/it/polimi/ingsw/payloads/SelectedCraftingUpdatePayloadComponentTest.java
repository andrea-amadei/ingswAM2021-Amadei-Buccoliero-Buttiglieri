package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.updates.SelectedCraftingUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectedCraftingUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.selectedCrafting("Ernestino", Production.CraftingType.BASE, 0);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"selected_crafting\",\"group\":\"update\",\"crafting_type\":\"BASE\",\"index\"" +
                ":0,\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"selected_crafting\",\"group\":\"update\",\"crafting_type\":\"BASE\",\"index\"" +
                ":0,\"player\":\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = JSONParser.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof SelectedCraftingUpdate);

        SelectedCraftingUpdate update = ((SelectedCraftingUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals("Ernestino", update.getPlayer());
        assertEquals(Production.CraftingType.BASE, update.getCraftingType());
        assertEquals(0, update.getIndex());
    }

}
