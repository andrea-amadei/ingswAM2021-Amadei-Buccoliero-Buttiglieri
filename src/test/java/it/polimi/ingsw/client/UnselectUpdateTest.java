package it.polimi.ingsw.client;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.updates.UnselectUpdate;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.production.UpgradableCrafting;
import it.polimi.ingsw.parser.raw.RawCrafting;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UnselectUpdateTest {

    private ClientModel clientModel;

    @BeforeEach
    public void init() throws IOException, ParserException {
        clientModel = new ClientModel();

        List<String> usernames = new ArrayList<>();
        usernames.add("Ernestino");
        usernames.add("Giuseppa");
        usernames.add("Domenica");

        String configJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/config.json"));
        String craftingJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/crafting.json"));
        String faithJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/faith.json"));
        String leadersJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/leaders.json"));

        ClientGameBuilder.buildGame(clientModel, usernames, configJSON, craftingJSON, faithJSON, leadersJSON);

        Map<ResourceType, Integer> input = new HashMap<>(){{put(ResourceTypeSingleton.getInstance().getStoneResource(),
                2);}};
        Map<ResourceType, Integer> output = new HashMap<>(){{put(ResourceTypeSingleton.getInstance().getShieldResource(),
                4);}};
        UpgradableCrafting crafting = new UpgradableCrafting(input, output, 0, 1);
        RawCrafting rawCrafting = new RawCrafting(crafting);
        clientModel.getPlayerByName("Ernestino").getProduction().addUpgradableCrafting(rawCrafting, 1,
                0);
        clientModel.getPlayerByName("Ernestino").getProduction().selectCrafting(Production.CraftingType
                        .UPGRADABLE, 0);
    }

    @Test
    public void correctApply(){
        UnselectUpdate update = new UnselectUpdate("production", "Ernestino");
        update.apply(clientModel);

        assertNull(clientModel.getPlayerByName("Ernestino").getProduction().getSelectedCraftingIndex());
        assertNull(clientModel.getPlayerByName("Ernestino").getProduction().getSelectedType());
    }

}
