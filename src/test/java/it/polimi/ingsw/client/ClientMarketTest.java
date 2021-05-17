package it.polimi.ingsw.client;

import it.polimi.ingsw.client.model.ClientMarket;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.parser.raw.RawMarket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

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
