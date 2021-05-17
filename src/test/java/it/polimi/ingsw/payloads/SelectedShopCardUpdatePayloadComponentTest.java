package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.parser.ClientDeserializer;
import it.polimi.ingsw.client.updates.SelectedShopCardUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectedShopCardUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.selectedShopCard("Ernestino", 1, 1);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"selected_shop_card\",\"group\":\"update\",\"x\":1,\"y\":1,\"player\":" +
                "\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"selected_shop_card\",\"group\":\"update\",\"x\":1,\"y\":1,\"player\":" +
                "\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = ClientDeserializer.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof SelectedShopCardUpdate);

        SelectedShopCardUpdate update = ((SelectedShopCardUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals("Ernestino", update.getPlayer());
        assertEquals(1, update.getX());
        assertEquals(1, update.getY());
    }

}
