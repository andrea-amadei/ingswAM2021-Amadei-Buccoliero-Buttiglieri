package it.polimi.ingsw.payloads;
import it.polimi.ingsw.clientproto.network.ServerNetworkObject;
import it.polimi.ingsw.clientproto.parser.ClientDeserializer;
import it.polimi.ingsw.clientproto.updates.AddDiscountUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddDiscountUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.addDiscount("Ernestino", "gold", 1);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"add_discount\",\"group\":\"update\",\"resource\":\"gold\",\"discount\":1,\"player\":\"Ernestino\"}",
                serialized);
    }

    @Test
    public void correctlyDeserialize(){
        String serialized = "{\"type\":\"add_discount\",\"group\":\"update\",\"resource\":\"gold\",\"discount\":1,\"player\"" +
                ":\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = ClientDeserializer.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof AddDiscountUpdate);

        AddDiscountUpdate update = ((AddDiscountUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals("Ernestino", update.getPlayer());
        assertEquals("gold", update.getResource());
        assertEquals(1, update.getDiscount());
    }
}
