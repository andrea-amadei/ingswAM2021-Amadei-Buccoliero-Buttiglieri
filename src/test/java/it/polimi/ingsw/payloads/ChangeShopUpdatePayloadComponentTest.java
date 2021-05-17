package it.polimi.ingsw.payloads;
import it.polimi.ingsw.clientproto.network.ServerNetworkObject;
import it.polimi.ingsw.clientproto.parser.ClientDeserializer;
import it.polimi.ingsw.clientproto.updates.ChangeShopUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeShopUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.changeShop(1,2,4);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"change_shop\",\"group\":\"update\",\"x\":1,\"y\":2,\"id\":4}",
                serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"change_shop\",\"group\":\"update\",\"x\":1,\"y\":2,\"id\":4}";
        ServerNetworkObject serverNetworkObject = ClientDeserializer.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof ChangeShopUpdate);

        ChangeShopUpdate update = ((ChangeShopUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals(1, update.getX());
        assertEquals(2, update.getY());
        assertEquals(4, update.getId());
    }

}
