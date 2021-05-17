package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddPointsUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.addPoints("Ernestino", 5);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"add_points\",\"group\":\"update\",\"amount\":5,\"player\":\"Ernestino\"}",
                serialized);
    }

}
