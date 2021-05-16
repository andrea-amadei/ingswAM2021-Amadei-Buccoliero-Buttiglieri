package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeCurrentPlayerPayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.changeCurrentPlayer("Genoveffa");
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"change_current_player\",\"group\":\"update\",\"newPlayer\":\"Genoveffa\"}",
                serialized);
    }

}
