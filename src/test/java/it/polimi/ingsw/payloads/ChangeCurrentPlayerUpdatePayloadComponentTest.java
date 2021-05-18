package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.updates.ChangeCurrentPlayerUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeCurrentPlayerUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.changeCurrentPlayer("Genoveffa");
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"change_current_player\",\"group\":\"update\",\"newPlayer\":\"Genoveffa\"}",
                serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"change_current_player\",\"group\":\"update\",\"newPlayer\":\"Genoveffa\"}";
        ServerNetworkObject serverNetworkObject = JSONParser.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof ChangeCurrentPlayerUpdate);

        ChangeCurrentPlayerUpdate update = ((ChangeCurrentPlayerUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals("Genoveffa", update.getNewPlayer());
    }

}
