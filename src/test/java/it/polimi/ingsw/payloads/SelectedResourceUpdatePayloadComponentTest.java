package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.updates.SelectedResourceUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectedResourceUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.selectedResource("Ernestino", "Chest",
                "gold", 2);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"selected_resource\",\"group\":\"update\",\"container_id\":\"Chest\",\"resource\"" +
                ":\"gold\",\"amount\":2,\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"selected_resource\",\"group\":\"update\",\"container_id\":\"Chest\",\"resource\"" +
                ":\"gold\",\"amount\":2,\"player\":\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = JSONParser.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof SelectedResourceUpdate);

        SelectedResourceUpdate update = ((SelectedResourceUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals("Ernestino", update.getPlayer());
        assertEquals("Chest", update.getContainerId());
        assertEquals("gold", update.getResource());
        assertEquals(2, update.getAmount());
    }

}
