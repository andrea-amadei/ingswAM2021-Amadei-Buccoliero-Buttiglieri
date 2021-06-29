package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.DiscardLeaderActionPayloadComponent;
import it.polimi.ingsw.server.model.actions.DiscardLeaderAction;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscardLeaderActionPayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new DiscardLeaderActionPayloadComponent("Ernestino", 1);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"discard_leader\",\"group\":\"action\",\"leaderID\":1,\"player\":\"Ernestino\"}",
                serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"discard_leader\",\"group\":\"action\",\"leaderID\":1,\"player\":\"Ernestino\"}";
        ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

        assertTrue(clientNetworkObject instanceof DiscardLeaderAction);

        DiscardLeaderAction action = ((DiscardLeaderAction)clientNetworkObject);

        assertEquals("Ernestino", action.getSender());
        assertEquals(1, action.getLeaderID());
    }

}
