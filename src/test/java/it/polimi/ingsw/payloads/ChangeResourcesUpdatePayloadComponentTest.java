package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangeResourcesUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        Map<String, Integer> resources = new HashMap<>();
        resources.put("gold", 2);
        resources.put("servant", 1);
        RawStorage rawStorage = new RawStorage("id", resources);
        PayloadComponent payload = PayloadFactory.changeResources("Ernestino", rawStorage);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"change_resources\",\"group\":\"update\",\"delta\":{\"id\":\"id\"," +
                "\"resources\":{\"gold\":2,\"servant\":1}},\"player\":\"Ernestino\"}", serialized);
    }

}
