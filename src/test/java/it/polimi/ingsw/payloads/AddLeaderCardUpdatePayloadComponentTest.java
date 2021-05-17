package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.parser.ClientDeserializer;
import it.polimi.ingsw.client.updates.AddLeaderCardUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddLeaderCardUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.addLeaderCard("Ernestino", 9);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"add_leader_card\",\"group\":\"update\",\"id\":9,\"player\":\"Ernestino\"}",
                serialized);
    }

    @Test
    public void correctlyDeserialize(){
        String serialized = "{\"type\":\"add_leader_card\",\"group\":\"update\",\"id\":9,\"player\":\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = ClientDeserializer.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof AddLeaderCardUpdate);

        AddLeaderCardUpdate update = ((AddLeaderCardUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals(9, update.getId());
        assertEquals("Ernestino", update.getPlayer());
    }

}
