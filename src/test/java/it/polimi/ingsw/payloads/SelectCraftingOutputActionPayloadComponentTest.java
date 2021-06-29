package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.actions.SelectCraftingOutputActionPayloadComponent;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.actions.SelectCraftingOutputAction;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientNetworkObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SelectCraftingOutputActionPayloadComponentTest {

    private Map<String, Integer> conversions = new HashMap<>();

    @BeforeEach
    public void init(){
        conversions.put("gold", 1);
    }

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = new SelectCraftingOutputActionPayloadComponent("Ernestino", conversions);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"select_crafting_output\",\"group\":\"action\",\"conversion\":{\"gold\":1}," +
                "\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"select_crafting_output\",\"group\":\"action\",\"conversion\":{\"gold\":1}," +
                "\"player\":\"Ernestino\"}";
        ClientNetworkObject clientNetworkObject = JSONParser.getClientNetworkObject(serialized);

        assertTrue(clientNetworkObject instanceof SelectCraftingOutputAction);

        SelectCraftingOutputAction action = ((SelectCraftingOutputAction)clientNetworkObject);

        assertEquals("Ernestino", action.getSender());
        assertEquals(1, action.getConversion().get(ResourceTypeSingleton.getInstance().getGoldResource()));
    }

}
