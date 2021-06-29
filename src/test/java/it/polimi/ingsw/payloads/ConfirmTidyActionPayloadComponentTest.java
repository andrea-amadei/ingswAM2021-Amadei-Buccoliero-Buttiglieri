package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.ConfirmTidyActionPayloadComponent;
import it.polimi.ingsw.server.model.actions.ConfirmTidyAction;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConfirmTidyActionPayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new ConfirmTidyActionPayloadComponent("Ernestino");
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"confirm_tidy\",\"group\":\"action\",\"player\":\"Ernestino\"}",
                serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"confirm_tidy\",\"group\":\"action\",\"player\":\"Ernestino\"}";
        ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

        assertTrue(clientNetworkObject instanceof ConfirmTidyAction);

        ConfirmTidyAction action = ((ConfirmTidyAction)clientNetworkObject);

        assertEquals("Ernestino", action.getSender());
    }

}
