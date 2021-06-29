package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.updates.ChangeMarketUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.server.model.basetypes.MarbleColor;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.MarbleFactory;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.common.parser.raw.RawMarket;
import it.polimi.ingsw.common.utils.PayloadFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeMarketUpdatePayloadComponentTest {

    private RawMarket rawMarket;

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
        rawMarket = new RawMarket(market);
    }

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.changeMarket(rawMarket);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"change_market\",\"group\":" +
                "\"update\",\"market\":{\"marbles\":[\"WHITE\",\"BLUE\",\"WHITE\"," +
                "\"YELLOW\",\"PURPLE\",\"YELLOW\",\"WHITE\",\"GREY\",\"BLUE\",\"GREY\",\"RED\"," +
                "\"WHITE\"],\"odd\":\"PURPLE\"}}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"change_market\",\"group\":" +
                "\"update\",\"market\":{\"marbles\":[\"WHITE\",\"BLUE\",\"WHITE\"," +
                "\"YELLOW\",\"PURPLE\",\"YELLOW\",\"WHITE\",\"GREY\",\"BLUE\",\"GREY\",\"RED\"," +
                "\"WHITE\"],\"odd\":\"PURPLE\"}}";
        ServerNetworkObject serverNetworkObject = JSONParser.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof ChangeMarketUpdate);

        ChangeMarketUpdate update = ((ChangeMarketUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals(rawMarket.getOdd(), update.getMarket().getOdd());
        assertEquals(rawMarket.getMarbles(), update.getMarket().getMarbles());
    }

}
