package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientmodel.ClientMarket;
import it.polimi.ingsw.server.model.basetypes.MarbleColor;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.MarbleFactory;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.common.parser.raw.RawMarket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClientMarketTest {

    private static RawMarket dummyMarket;

    @BeforeEach
    public void init(){
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
        Market market = new Market(marbles, MarbleFactory.createMarble(MarbleColor.PURPLE), 3, 4);
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
