package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.BuyFromMarketActionPayloadComponent;
import it.polimi.ingsw.server.model.actions.BuyFromMarketAction;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BuyFromMarketActionPayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new BuyFromMarketActionPayloadComponent("Ernestino", false, 1);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"buy_from_market\",\"group\":\"action\",\"isRow\":false,\"index\":1," +
                "\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"buy_from_market\",\"group\":\"action\",\"isRow\":false,\"index\":1," +
                "\"player\":\"Ernestino\"}";
        ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

        assertTrue(clientNetworkObject instanceof BuyFromMarketAction);

        BuyFromMarketAction action = ((BuyFromMarketAction)clientNetworkObject);

        assertEquals("Ernestino", action.getSender());
        assertFalse(action.getIsRow());
        assertEquals(1, action.getIndex());
    }

}
