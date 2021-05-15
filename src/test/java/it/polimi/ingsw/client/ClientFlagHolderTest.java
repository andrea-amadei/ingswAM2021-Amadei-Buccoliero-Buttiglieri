package it.polimi.ingsw.client;

import it.polimi.ingsw.clientproto.model.ClientDiscountHolder;
import it.polimi.ingsw.clientproto.model.ClientFlagHolder;
import it.polimi.ingsw.gamematerials.FlagColor;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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

        flagHolder.addFlag(FlagColor.PURPLE, 1);
        flagHolder.addFlag(FlagColor.PURPLE, 2);
        flagHolder.addFlag(FlagColor.YELLOW, 3);
        flagHolder.addFlag(FlagColor.PURPLE, 2);

        assertEquals(new HashMap<FlagColor, Map<Integer, Integer>>(){{
            put(FlagColor.PURPLE, new HashMap<>(){{
                put(2, 2);
                put(1, 1);
            }});
            put(FlagColor.YELLOW, new HashMap<>(){{
                put(3, 1);
            }});

        }}, flagHolder.getFlags());
    }
}
