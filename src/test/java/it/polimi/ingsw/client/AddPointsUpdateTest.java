package it.polimi.ingsw.client;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.updates.AddPointsUpdate;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddPointsUpdateTest {

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
    }

    @Test
    public void correctApply(){
        AddPointsUpdate update = new AddPointsUpdate(5, "Ernestino");
        update.apply(clientModel);

        assertEquals(5, clientModel.getPlayerByName("Ernestino").getVictoryPoints());
    }

}
