package it.polimi.ingsw.payloads;

import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.ActivateLeaderActionPayloadComponent;
import it.polimi.ingsw.server.model.actions.ActivateLeaderAction;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActivateLeaderActionPayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new ActivateLeaderActionPayloadComponent("Ernestino", 2);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"activate_leader\",\"group\":\"action\",\"leaderID\":2,\"player\":\"Ernestino\"}",
                serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"activate_leader\",\"group\":\"action\",\"leaderID\":2,\"player\":\"Ernestino\"}";
        ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

        assertTrue(clientNetworkObject instanceof ActivateLeaderAction);

        ActivateLeaderAction action = ((ActivateLeaderAction)clientNetworkObject);

        assertEquals("Ernestino", action.getSender());
        assertEquals(2, action.getLeaderID());
    }

}
