package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.parser.ClientDeserializer;
import it.polimi.ingsw.client.updates.UnselectUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UnselectUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.unselect("Ernestino", "market");
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"unselect\",\"group\":\"update\",\"section\":\"market\",\"player\":" +
                "\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized() {
        String serialized = "{\"type\":\"unselect\",\"group\":\"update\",\"section\":\"market\",\"player\":" +
                "\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = ClientDeserializer.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof UnselectUpdate);

        UnselectUpdate update = ((UnselectUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals("Ernestino", update.getPlayer());
        assertEquals("market", update.getSection());
    }
}
