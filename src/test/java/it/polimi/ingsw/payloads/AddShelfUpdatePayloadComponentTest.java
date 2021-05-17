package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.updates.AddShelfUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddShelfUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.addShelf("Ernestino", "LeaderShelfPaolo",
                "gold", 1);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"add_shelf\",\"group\":\"update\",\"id\":\"LeaderShelfPaolo\",\"resource\":" +
                        "\"gold\",\"index\":1,\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"add_shelf\",\"group\":\"update\",\"id\":\"LeaderShelfPaolo\",\"resource\":" +
                "\"gold\",\"index\":1,\"player\":\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = JSONParser.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof AddShelfUpdate);

        AddShelfUpdate update = ((AddShelfUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals("Ernestino", update.getPlayer());
        assertEquals("LeaderShelfPaolo", update.getId());
        assertEquals("gold", update.getResource());
        assertEquals(1, update.getIndex());
    }

}
