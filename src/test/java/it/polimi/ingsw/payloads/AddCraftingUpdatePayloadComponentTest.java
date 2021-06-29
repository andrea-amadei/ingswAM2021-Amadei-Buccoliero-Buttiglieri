package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.updates.AddCraftingUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.server.model.basetypes.ResourceType;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.production.Crafting;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.production.UpgradableCrafting;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.common.parser.raw.RawCrafting;
import it.polimi.ingsw.common.utils.PayloadFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddCraftingUpdatePayloadComponentTest {

    private RawCrafting rawCrafting;
    private Production.CraftingType craftingType;

    @BeforeEach
    public void init(){
        ResourceType gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceType servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceType shield = ResourceTypeSingleton.getInstance().getShieldResource();
        Map<ResourceType, Integer> input = new HashMap<>();
        Map<ResourceType, Integer> output = new HashMap<>();
        input.put(gold, 1);
        input.put(servant, 1);
        output.put(shield, 4);
        Crafting crafting = new UpgradableCrafting(input, output, 1, 1);
        rawCrafting = crafting.toRaw();
        craftingType = Production.CraftingType.UPGRADABLE;
    }

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.addCrafting("Marietta", rawCrafting, craftingType,
                2);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"add_crafting\",\"group\":\"update\",\"crafting\":{\"input\":{\"gold\":1,\"servant\":1}," +
                        "\"output\":{\"shield\":4},\"faithOutput\":1},\"crafting_type\":\"UPGRADABLE\",\"index\":2,\"player\":\"Marietta\"}",
                serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"add_crafting\",\"group\":\"update\",\"crafting\":{\"input\":{\"gold\":1,\"servant\":1}," +
                "\"output\":{\"shield\":4},\"faithOutput\":1},\"crafting_type\":\"UPGRADABLE\",\"index\":2,\"player\":\"Marietta\"}";
        ServerNetworkObject serverNetworkObject = JSONParser.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof AddCraftingUpdate);

        AddCraftingUpdate update = ((AddCraftingUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals("Marietta", update.getPlayer());
        assertEquals(2, update.getIndex());
        assertEquals(rawCrafting.getInput(), update.getCrafting().getInput());
        assertSame(craftingType, update.getCraftingType());
    }

}
