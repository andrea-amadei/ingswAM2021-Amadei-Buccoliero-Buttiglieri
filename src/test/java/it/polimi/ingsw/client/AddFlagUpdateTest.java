package it.polimi.ingsw.client;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.updates.AddFlagUpdate;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.gamematerials.LevelFlag;
import it.polimi.ingsw.parser.raw.RawLevelFlag;
import it.polimi.ingsw.utils.ResourceReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddFlagUpdateTest {

    private ClientModel clientModel;

    @BeforeEach
    public void init() throws IOException, ParserException {
        clientModel = new ClientModel();

        List<String> usernames = new ArrayList<>();
        usernames.add("Ernestino");
        usernames.add("Giuseppa");
        usernames.add("Domenica");

        String configJSON = Files.readString(ResourceReader.getPathFromResource("cfg/config.json"));
        String craftingJSON = Files.readString(ResourceReader.getPathFromResource("cfg/crafting.json"));
        String faithJSON = Files.readString(ResourceReader.getPathFromResource("cfg/faith.json"));
        String leadersJSON = Files.readString(ResourceReader.getPathFromResource("cfg/leaders.json"));

        ClientGameBuilder.buildGame(clientModel, usernames, configJSON, craftingJSON, faithJSON, leadersJSON);
    }

    @Test
    public void correctApply(){
        LevelFlag levelFlag = new LevelFlag(FlagColor.GREEN, 2);
        RawLevelFlag flag = new RawLevelFlag(levelFlag);
        AddFlagUpdate update = new AddFlagUpdate(flag, "Ernestino");
        update.apply(clientModel);

        assertEquals(1, clientModel.getPlayerByName("Ernestino").getFlagHolder().getFlags()
                    .size());
    }

}
