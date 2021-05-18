package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.MoveFromBasketToShelfActionPayloadComponent;
import it.polimi.ingsw.model.actions.MoveFromBasketToShelfAction;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoveFromBasketToShelfActionPayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new MoveFromBasketToShelfActionPayloadComponent("Ernestino",
                "shield", 2, "BottomShelf");
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"move_from_basket_to_shelf\",\"group\":\"action\",\"resourceToMove\":\"" +
                "shield\",\"amount\":2,\"shelfID\":\"BottomShelf\",\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"move_from_basket_to_shelf\",\"group\":\"action\",\"resourceToMove\":\"shield\"," +
                "\"amount\":2,\"shelfID\":\"BottomShelf\",\"player\":\"Ernestino\"}";
        ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

        assertTrue(clientNetworkObject instanceof MoveFromBasketToShelfAction);

        MoveFromBasketToShelfAction action = ((MoveFromBasketToShelfAction)clientNetworkObject);

        assertEquals("Ernestino", action.getSender());
        assertEquals("shield", action.getResourceToMove().getId());
        assertEquals(2, action.getAmount());
        assertEquals("BottomShelf", action.getShelfID());
    }

}
