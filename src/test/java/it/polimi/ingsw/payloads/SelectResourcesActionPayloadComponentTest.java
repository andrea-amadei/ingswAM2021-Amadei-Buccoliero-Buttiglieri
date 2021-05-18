package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.SelectResourcesActionPayloadComponent;
import it.polimi.ingsw.model.actions.SelectResourcesAction;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectResourcesActionPayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new SelectResourcesActionPayloadComponent("Ernestino",
                "MiddleShelf", "stone", 2);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"select_resources\",\"group\":\"action\",\"containerId\":\"MiddleShelf\"," +
                "\"resource\":\"stone\",\"amount\":2,\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"select_resources\",\"group\":\"action\",\"containerId\":\"MiddleShelf\"," +
                "\"resource\":\"stone\",\"amount\":2,\"player\":\"Ernestino\"}";
        ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

        assertTrue(clientNetworkObject instanceof SelectResourcesAction);

        SelectResourcesAction action = ((SelectResourcesAction)clientNetworkObject);

        assertEquals("Ernestino", action.getSender());
        assertEquals("MiddleShelf", action.getContainerId());
        assertEquals("stone", action.getResource().getId());
        assertEquals(2, action.getAmount());
    }

}
