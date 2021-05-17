package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.parser.ClientDeserializer;
import it.polimi.ingsw.client.updates.DiscardLeaderCardUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscardLeaderCardUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        //TODO: possibly change "id" to "index"
        PayloadComponent payload = PayloadFactory.discardLeaderCard("Ernestino", 0);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"discard_leader_card\",\"group\":\"update\",\"id\":0,\"player\":\"Ernestino\"}",
                serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"discard_leader_card\",\"group\":\"update\",\"id\":0,\"player\":\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = ClientDeserializer.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof DiscardLeaderCardUpdate);

        DiscardLeaderCardUpdate update = ((DiscardLeaderCardUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals("Ernestino", update.getPlayer());
        assertEquals(0, update.getId());
    }

}
