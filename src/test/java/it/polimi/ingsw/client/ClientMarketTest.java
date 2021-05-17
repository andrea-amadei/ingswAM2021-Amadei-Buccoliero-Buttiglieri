package it.polimi.ingsw.client;

import it.polimi.ingsw.clientproto.model.ClientLeaderCards;
import it.polimi.ingsw.clientproto.model.ClientMarket;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawLeaderCard;
import it.polimi.ingsw.parser.raw.RawMarket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ClientMarketTest {

    private static RawMarket dummyMarket;

    @BeforeEach
    public void init(){
        Market market = new Market(new Random(3));
        dummyMarket = market.toRaw();
    }

    @Test
    public void creation(){
        ClientMarket clientMarket = new ClientMarket(3, 4);

        assertNull(clientMarket.getMarket());
        assertEquals(3, clientMarket.getRowSize());
        assertEquals(4, clientMarket.getColSize());
    }

    @Test
    public void changeMarket(){
        ClientMarket clientMarket = new ClientMarket(3, 4);
        clientMarket.changeMarket(dummyMarket);
        assertEquals(dummyMarket, clientMarket.getMarket());
    }
}
