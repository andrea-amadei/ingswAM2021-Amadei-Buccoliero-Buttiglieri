package it.polimi.ingsw.client;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.updates.ChangeResourcesUpdate;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.parser.raw.RawStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeResourcesUpdateTest {

    private ClientModel clientModel;

    @BeforeEach
    public void init() throws IOException, ParserException {
        clientModel = new ClientModel();

        List<String> usernames = new ArrayList<>();
        usernames.add("Ernestino");
        usernames.add("Giuseppa");
        usernames.add("Domenica");

        String configJSON = Files.readString(Path.of("src/main/config.json"));
        String craftingJSON = Files.readString(Path.of("src/main/crafting.json"));
        String faithJSON = Files.readString(Path.of("src/main/faith.json"));
        String leadersJSON = Files.readString(Path.of("src/main/leaders.json"));

        ClientGameBuilder.buildGame(clientModel, usernames, configJSON, craftingJSON, faithJSON, leadersJSON);
    }

    @Test
    public void correctApply(){

        RawStorage delta = new RawStorage("BottomShelf", new HashMap<>(){{
            put("gold", 1);
        }});
        ChangeResourcesUpdate update = new ChangeResourcesUpdate(delta, "Ernestino");
        update.apply(clientModel);

        assertEquals(1, clientModel.getPlayerByName("Ernestino").getClientShelfById("BottomShelf")
                .getStorage().getResources().get("gold"));
    }

}
