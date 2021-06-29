package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.updates.AddPointsUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.common.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddPointsUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.addPoints("Ernestino", 5);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"add_points\",\"group\":\"update\",\"amount\":5,\"player\":\"Ernestino\"}",
                serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"add_points\",\"group\":\"update\",\"amount\":5,\"player\":\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = JSONParser.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof AddPointsUpdate);

        AddPointsUpdate update = ((AddPointsUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals(5, update.getAmount());
        assertEquals("Ernestino", update.getPlayer());
    }

}
