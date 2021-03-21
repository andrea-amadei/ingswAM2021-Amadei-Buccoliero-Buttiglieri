package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.gamematerials.*;
import org.junit.jupiter.api.Test;

public class FlagTest {
    @Test
    public void getterTest() {
        BaseFlag green = new BaseFlag(FlagColor.GREEN);
        LevelFlag blue2 = new LevelFlag(FlagColor.BLUE, 2);

        assertSame(green.getColor(), FlagColor.GREEN);
        assertSame(blue2.getColor(), FlagColor.BLUE);
        assertSame(blue2.getLevel(), 2);
    }
}
