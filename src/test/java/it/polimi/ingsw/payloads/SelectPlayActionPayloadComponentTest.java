package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.SelectPlayActionPayloadComponent;
import it.polimi.ingsw.model.actions.SelectPlayAction;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectPlayActionPayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new SelectPlayActionPayloadComponent("Ernestino",
                SelectPlayAction.Play.CRAFTING);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"select_play\",\"group\":\"action\",\"play\":\"CRAFTING\",\"player\":" +
                "\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"select_play\",\"group\":\"action\",\"play\":\"CRAFTING\",\"player\"" +
                ":\"Ernestino\"}";
        ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

        assertTrue(clientNetworkObject instanceof SelectPlayAction);

        SelectPlayAction action = ((SelectPlayAction)clientNetworkObject);

        assertEquals("Ernestino", action.getSender());
        assertEquals("CRAFTING", action.getPlay().name());
    }

}
