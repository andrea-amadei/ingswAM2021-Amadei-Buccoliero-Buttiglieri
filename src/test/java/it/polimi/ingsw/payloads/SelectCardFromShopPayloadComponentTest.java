package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.SelectCardFromShopActionPayloadComponent;
import it.polimi.ingsw.server.model.actions.SelectCardFromShopAction;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectCardFromShopPayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new SelectCardFromShopActionPayloadComponent("Ernestino", 1,
                2, 3);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"select_card_from_shop\",\"group\":\"action\",\"row\":1,\"col\":2," +
                "\"upgradableCraftingId\":3,\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"select_card_from_shop\",\"group\":\"action\",\"row\":1,\"col\":2,\"" +
                "upgradableCraftingId\":3,\"player\":\"Ernestino\"}";
        ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

        assertTrue(clientNetworkObject instanceof SelectCardFromShopAction);

        SelectCardFromShopAction action = ((SelectCardFromShopAction)clientNetworkObject);

        assertEquals("Ernestino", action.getSender());
        assertEquals(1, action.getRow());
        assertEquals(2, action.getCol());
        assertEquals(3, action.getUpgradableCraftingId());
    }

}
