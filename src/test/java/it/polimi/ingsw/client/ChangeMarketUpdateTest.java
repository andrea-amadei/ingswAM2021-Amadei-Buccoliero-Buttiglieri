package it.polimi.ingsw.client;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.updates.ChangeMarketUpdate;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.parser.raw.RawMarket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeMarketUpdateTest {

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
        Market market1 = new Market(new Random(3));
        RawMarket market = new RawMarket(market1);
        ChangeMarketUpdate update = new ChangeMarketUpdate(market);
        update.apply(clientModel);

        assertEquals(market, clientModel.getMarket().getMarket());
    }

}
