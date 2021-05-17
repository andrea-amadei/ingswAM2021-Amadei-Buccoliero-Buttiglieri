package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.parser.ClientDeserializer;
import it.polimi.ingsw.client.updates.ChangeMarketUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.parser.raw.RawMarket;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeMarketUpdatePayloadComponentTest {

    private RawMarket rawMarket;

    @BeforeEach
    public void init(){
        Market market = new Market(new Random(17));
        rawMarket = new RawMarket(market);
    }

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.changeMarket(rawMarket);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"change_market\",\"group\":\"update\",\"market\":" +
                "{\"marbles\":[\"WHITE\",\"RED\",\"PURPLE\",\"WHITE\",\"GREY\",\"YELLOW\",\"WHITE\"," +
                "\"BLUE\",\"GREY\",\"WHITE\",\"BLUE\",\"YELLOW\"],\"odd\":\"PURPLE\"}}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"change_market\",\"group\":\"update\",\"market\":" +
                "{\"marbles\":[\"WHITE\",\"RED\",\"PURPLE\",\"WHITE\",\"GREY\",\"YELLOW\",\"WHITE\"," +
                "\"BLUE\",\"GREY\",\"WHITE\",\"BLUE\",\"YELLOW\"],\"odd\":\"PURPLE\"}}";
        ServerNetworkObject serverNetworkObject = ClientDeserializer.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof ChangeMarketUpdate);

        ChangeMarketUpdate update = ((ChangeMarketUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals(rawMarket.getOdd(), update.getMarket().getOdd());
        assertEquals(rawMarket.getMarbles(), update.getMarket().getMarbles());
    }

}
