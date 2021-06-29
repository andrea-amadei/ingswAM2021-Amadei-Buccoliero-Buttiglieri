package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.updates.ChangeResourcesUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.common.parser.raw.RawStorage;
import it.polimi.ingsw.common.utils.PayloadFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeResourcesUpdatePayloadComponentTest {

    private RawStorage rawStorage;

    @BeforeEach
    public void init(){
        Map<String, Integer> resources = new HashMap<>();
        resources.put("gold", 2);
        resources.put("servant", 1);
        rawStorage = new RawStorage("id", resources);
    }

    @Test
    public void correctlySerialized(){
        PayloadComponent payload = PayloadFactory.changeResources("Ernestino", rawStorage);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"change_resources\",\"group\":\"update\",\"delta\":{\"id\":\"id\"," +
                "\"resources\":{\"gold\":2,\"servant\":1}},\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"change_resources\",\"group\":\"update\",\"delta\":{\"id\":\"id\"," +
                "\"resources\":{\"gold\":2,\"servant\":1}},\"player\":\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = JSONParser.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof ChangeResourcesUpdate);

        ChangeResourcesUpdate update = ((ChangeResourcesUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals("Ernestino", update.getPlayer());
        assertEquals(rawStorage.getResources(), update.getDelta().getResources());
    }

}
