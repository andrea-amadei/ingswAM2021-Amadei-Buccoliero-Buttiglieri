package it.polimi.ingsw.payloads;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.gamematerials.LevelFlag;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.parser.raw.RawLevelFlag;
import it.polimi.ingsw.utils.PayloadFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddFlagUpdatePayloadComponentTest {

    @Test
    public void correctlySerialized(){
        LevelFlag flag = new LevelFlag(FlagColor.PURPLE, 1);
        RawLevelFlag rawLevelFlag = flag.toRaw();
        PayloadComponent payload = PayloadFactory.addFlag("Ernestino", rawLevelFlag);
        String serialized = JSONSerializer.toJson(payload);

        assertEquals("{\"type\":\"add_flag\",\"group\":\"update\",\"flag\":{\"color\":\"PURPLE\",\"level\":1},\"player\":\"Ernestino\"}",
                serialized);
    }

}
