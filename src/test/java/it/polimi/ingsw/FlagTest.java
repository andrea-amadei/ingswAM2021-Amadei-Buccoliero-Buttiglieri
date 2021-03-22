package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.gamematerials.*;
import org.junit.jupiter.api.Test;

public class FlagTest {
    @Test
    public void test() {
        BaseFlag green = new BaseFlag(FlagColor.GREEN);
        assertSame(green.getColor(), FlagColor.GREEN);

        LevelFlag blue2 = new LevelFlag(FlagColor.BLUE, 2);
        assertSame(blue2.getColor(), FlagColor.BLUE);
        assertSame(blue2.getLevel(), 2);

        assertThrows(IllegalArgumentException.class, () -> new LevelFlag(FlagColor.YELLOW, 4));
    }
}
