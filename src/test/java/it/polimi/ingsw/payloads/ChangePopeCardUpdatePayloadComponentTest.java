package it.polimi.ingsw.payloads;
import it.polimi.ingsw.clientproto.network.ServerNetworkObject;
import it.polimi.ingsw.clientproto.parser.ClientDeserializer;
import it.polimi.ingsw.clientproto.updates.ChangePopeCardUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangePopeCardUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.changePopeCard("Ernestino",
                FaithHolder.CheckpointStatus.ACTIVE,1);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"change_pope_card\",\"group\":\"update\",\"status\":\"ACTIVE\"," +
                "\"index\":1,\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"change_pope_card\",\"group\":\"update\",\"status\":\"ACTIVE\"," +
                "\"index\":1,\"player\":\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = ClientDeserializer.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof ChangePopeCardUpdate);

        ChangePopeCardUpdate update = ((ChangePopeCardUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals("Ernestino", update.getPlayer());
        assertEquals(FaithHolder.CheckpointStatus.ACTIVE, update.getStatus());
        assertEquals(1, update.getIndex());
    }

}
