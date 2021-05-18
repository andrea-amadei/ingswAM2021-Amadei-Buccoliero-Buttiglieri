package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.BackActionPayloadComponent;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BackActionPayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new BackActionPayloadComponent("Ernestino");
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"back\",\"group\":\"action\",\"player\":\"Ernestino\"}",
                serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"back\",\"group\":\"action\",\"player\":\"Ernestino\"}";
        ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

        assertTrue(clientNetworkObject instanceof BackAction);

        BackAction action = ((BackAction)clientNetworkObject);

        assertEquals("Ernestino", action.getSender());
    }

}
