package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.parser.raw.RawCrafting;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddCraftingUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        ResourceType gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceType servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceType shield = ResourceTypeSingleton.getInstance().getShieldResource();
        Map<ResourceType, Integer> input = new HashMap<>();
        Map<ResourceType, Integer> output = new HashMap<>();
        input.put(gold, 1);
        input.put(servant, 1);
        output.put(shield, 4);
        Crafting crafting = new UpgradableCrafting(input, output, 1, 1);
        RawCrafting rawCrafting = crafting.toRaw();
        PayloadComponent payload = PayloadFactory.addCrafting("Marietta", rawCrafting, Production.CraftingType.UPGRADABLE,
                2);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"add_crafting\",\"group\":\"update\",\"crafting\":{\"input\":{\"gold\":1,\"servant\":1},\"output\":{\"shield\":4},\"faithOutput\":1},\"crafting_type\":\"UPGRADABLE\",\"index\":2,\"player\":\"Marietta\"}",
                serialized);
    }

}
