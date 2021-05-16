package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeCoveredLeaderCardUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.changeCoveredLeaderCard("Ernestino", 2);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"change_covered_leader_card\",\"group\":\"update\",\"delta\":2,\"player\":\"Ernestino\"}",
                serialized);
    }

}
