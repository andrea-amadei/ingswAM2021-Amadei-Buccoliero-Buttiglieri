package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.SelectCraftingActionPayloadComponent;
import it.polimi.ingsw.model.actions.SelectCraftingAction;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectCraftingActionPayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new SelectCraftingActionPayloadComponent("Ernestino",
                Production.CraftingType.LEADER, 1);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"select_crafting\",\"group\":\"action\",\"craftingType\":\"LEADER\"," +
                "\"index\":1,\"player\":\"Ernestino\"}", serialized);
    }

   @Test
   public void correctlyDeserialized(){
        String serialized = "{\"type\":\"select_crafting\",\"group\":\"action\",\"craftingType\":\"LEADER\",\"index\":1," +
                "\"player\":\"Ernestino\"}";
       ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

       assertTrue(clientNetworkObject instanceof SelectCraftingAction);

       SelectCraftingAction action = ((SelectCraftingAction)clientNetworkObject);

       assertEquals("Ernestino", action.getSender());
       assertEquals("LEADER", action.getCraftingType().name());
       assertEquals(1, action.getIndex());
   }

}
