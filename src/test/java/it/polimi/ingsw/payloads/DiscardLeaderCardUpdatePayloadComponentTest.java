package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscardLeaderCardUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        //TODO: possibly change "id" to "index"
        PayloadComponent payload = PayloadFactory.discardLeaderCard("Ernestino", 0);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"discard_leader_card\",\"group\":\"update\",\"id\":0,\"player\":\"Ernestino\"}",
                serialized);
    }

}
