package it.polimi.ingsw.payloads;
import it.polimi.ingsw.client.network.ServerNetworkObject;
import it.polimi.ingsw.client.updates.AddFlagUpdate;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.server.model.basetypes.FlagColor;
import it.polimi.ingsw.server.model.basetypes.LevelFlag;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.common.parser.raw.RawLevelFlag;
import it.polimi.ingsw.common.utils.PayloadFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddFlagUpdatePayloadComponentTest {

    private RawLevelFlag rawLevelFlag;

    @BeforeEach
    public void init(){
        LevelFlag flag = new LevelFlag(FlagColor.PURPLE, 1);
        rawLevelFlag = flag.toRaw();
    }

    @Test
    public void correctlySerialized(){

        PayloadComponent payload = PayloadFactory.addFlag("Ernestino", rawLevelFlag);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"add_flag\",\"group\":\"update\",\"flag\":{\"color\":\"PURPLE\",\"level\":1}," +
                        "\"player\":\"Ernestino\"}", serialized);
    }

    @Test
    public void correctlyDeserialized(){
        String serialized = "{\"type\":\"add_flag\",\"group\":\"update\",\"flag\":{\"color\":\"PURPLE\",\"level\":1}," +
                "\"player\":\"Ernestino\"}";
        ServerNetworkObject serverNetworkObject = JSONParser.getServerNetworkObject(serialized);

        assertTrue(serverNetworkObject instanceof AddFlagUpdate);

        AddFlagUpdate update = ((AddFlagUpdate)serverNetworkObject);
        update.checkFormat();

        assertEquals(rawLevelFlag.getLevel(), update.getFlag().getLevel());
        assertEquals(rawLevelFlag.getColor(), update.getFlag().getColor());
        assertEquals("Ernestino", update.getPlayer());
    }

}
