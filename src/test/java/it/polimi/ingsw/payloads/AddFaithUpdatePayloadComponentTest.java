package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddFaithUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.addFaith("Giocatore", 2);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"add_faith\",\"group\":\"update\",\"amount\":2,\"player\":\"Giocatore\"}",
                serialized);
    }

}
