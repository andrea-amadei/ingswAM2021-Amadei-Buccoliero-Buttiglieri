package it.polimi.ingsw.client;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.updates.ChangeMarketUpdate;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleFactory;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.parser.raw.RawMarket;
import it.polimi.ingsw.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
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

        String configJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/config.json"));
        String craftingJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/crafting.json"));
        String faithJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/faith.json"));
        String leadersJSON = Files.readString(ResourceLoader.getPathFromResource("cfg/leaders.json"));

        ClientGameBuilder.buildGame(clientModel, usernames, configJSON, craftingJSON, faithJSON, leadersJSON);
    }

    @Test
    public void correctApply(){
        List<Marble> marbles = Arrays.asList(
                MarbleFactory.createMarble(MarbleColor.WHITE),
                MarbleFactory.createMarble(MarbleColor.BLUE),
                MarbleFactory.createMarble(MarbleColor.WHITE),
                MarbleFactory.createMarble(MarbleColor.YELLOW),
                MarbleFactory.createMarble(MarbleColor.PURPLE),
                MarbleFactory.createMarble(MarbleColor.YELLOW),
                MarbleFactory.createMarble(MarbleColor.WHITE),
                MarbleFactory.createMarble(MarbleColor.GREY),
                MarbleFactory.createMarble(MarbleColor.BLUE),
                MarbleFactory.createMarble(MarbleColor.GREY),
                MarbleFactory.createMarble(MarbleColor.RED),
                MarbleFactory.createMarble(MarbleColor.WHITE)
        );
        Market market1 = new Market(marbles, MarbleFactory.createMarble(MarbleColor.PURPLE), 3, 4);
        RawMarket market = new RawMarket(market1);
        ChangeMarketUpdate update = new ChangeMarketUpdate(market);
        update.apply(clientModel);

        assertEquals(market, clientModel.getMarket().getMarket());
    }

}
