package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.parser.raw.RawMarket;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeMarketUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        Market market = new Market(new Random(17));
        RawMarket rawMarket = new RawMarket(market);
        PayloadComponent payload = PayloadFactory.changeMarket(rawMarket);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"change_market\",\"group\":\"update\",\"market\":" +
                "{\"marbles\":[\"WHITE\",\"RED\",\"PURPLE\",\"WHITE\",\"GREY\",\"YELLOW\",\"WHITE\"," +
                "\"BLUE\",\"GREY\",\"WHITE\",\"BLUE\",\"YELLOW\"],\"odd\":\"PURPLE\"}}", serialized);
    }

}
