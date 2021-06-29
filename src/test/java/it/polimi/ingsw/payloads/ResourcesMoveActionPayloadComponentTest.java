package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.ResourcesMoveActionPayloadComponent;
import it.polimi.ingsw.server.model.actions.ResourcesMoveAction;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResourcesMoveActionPayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new ResourcesMoveActionPayloadComponent("Ernestino", "Hand",
                "TopShelf", "servant", 1);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"resources_move\",\"group\":\"action\",\"origin\":\"Hand\",\"destination\":" +
                "\"TopShelf\",\"resourceToMove\":\"servant\",\"amount\":1,\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"resources_move\",\"group\":\"action\",\"origin\":\"Hand\",\"destination\":" +
                "\"TopShelf\",\"resourceToMove\":\"servant\",\"amount\":1,\"player\":\"Ernestino\"}";
        ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

        assertTrue(clientNetworkObject instanceof ResourcesMoveAction);

        ResourcesMoveAction action = ((ResourcesMoveAction)clientNetworkObject);
        assertEquals("Ernestino", action.getSender());
        assertEquals("Hand", action.getOrigin());
        assertEquals("TopShelf", action.getDestination());
        assertEquals("servant", action.getResourceToMove().getId());
        assertEquals(1, action.getAmount());
    }

}
