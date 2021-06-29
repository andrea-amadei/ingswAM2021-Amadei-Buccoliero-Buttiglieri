package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.updates.AddFaithUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.common.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddFaithUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.addFaith("Giocatore", 2);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"add_faith\",\"group\":\"update\",\"amount\":2,\"player\":\"Giocatore\"}",
                serialized);
    }

    @Test
    public void correctlyDeserialize(){
        String serialized = "{\"type\":\"add_faith\",\"group\":\"update\",\"amount\":2,\"player\":\"Giocatore\"}";
        ServerNetworkObject serverNetworkObject = JSONParser.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof AddFaithUpdate);

        AddFaithUpdate update = ((AddFaithUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals("Giocatore", update.getPlayer());
        assertEquals(2, update.getAmount());
    }

}
