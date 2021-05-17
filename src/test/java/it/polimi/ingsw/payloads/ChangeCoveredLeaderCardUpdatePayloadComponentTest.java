package it.polimi.ingsw.payloads;
import it.polimi.ingsw.clientproto.network.ServerNetworkObject;
import it.polimi.ingsw.clientproto.parser.ClientDeserializer;
import it.polimi.ingsw.clientproto.updates.ChangeCoveredLeaderCardUpdate;
import it.polimi.ingsw.clientproto.updates.ChangeShopUpdate;
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

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"change_covered_leader_card\",\"group\":\"update\",\"delta\":2,\"player\":" +
                "\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = ClientDeserializer.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof ChangeCoveredLeaderCardUpdate);

        ChangeCoveredLeaderCardUpdate update = ((ChangeCoveredLeaderCardUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals(2, update.getDelta());
        assertEquals("Ernestino", update.getPlayer());
    }

}
