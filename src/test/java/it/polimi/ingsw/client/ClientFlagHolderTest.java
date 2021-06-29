package it.polimi.ingsw.client;

import it.polimi.ingsw.client.clientmodel.ClientFlagHolder;
import it.polimi.ingsw.server.model.basetypes.FlagColor;
import it.polimi.ingsw.server.model.basetypes.LevelFlag;
import it.polimi.ingsw.common.parser.raw.RawLevelFlag;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class ClientFlagHolderTest {

    @Test
    public void creation(){
        ClientFlagHolder flagHolder = new ClientFlagHolder();
        assertEquals(0, flagHolder.getFlags().size());
    }

    @Test
    public void addFlag(){
        ClientFlagHolder flagHolder = new ClientFlagHolder();

        RawLevelFlag flag1 = new LevelFlag(FlagColor.PURPLE, 1).toRaw();
        RawLevelFlag flag2 = new LevelFlag(FlagColor.PURPLE, 2).toRaw();
        RawLevelFlag flag3 = new LevelFlag(FlagColor.YELLOW, 3).toRaw();
        RawLevelFlag flag4 = new LevelFlag(FlagColor.PURPLE, 2).toRaw();
        flagHolder.addFlag(flag1);
        flagHolder.addFlag(flag2);
        flagHolder.addFlag(flag3);
        flagHolder.addFlag(flag4);

        assertEquals(new HashSet<RawLevelFlag>(){{
            add(flag1);
            add(flag2);
            add(flag3);
            add(flag4);
        }}, flagHolder.getFlags());
    }
}
