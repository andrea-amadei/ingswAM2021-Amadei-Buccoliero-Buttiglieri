package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.server.model.basetypes.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Flag tests")
public class FlagTest {
    @Test
    @DisplayName("Construction")
    public void constructionTest() {
        BaseFlag green = new BaseFlag(FlagColor.GREEN);
        assertSame(green.getColor(), FlagColor.GREEN);

        LevelFlag blue2 = new LevelFlag(FlagColor.BLUE, 2);
        assertSame(blue2.getColor(), FlagColor.BLUE);
        assertSame(blue2.getLevel(), 2);

        assertThrows(NullPointerException.class, () -> new BaseFlag(null));
    }

    @Test
    @DisplayName("To string")
    public void toStringTest() {
        BaseFlag green = new BaseFlag(FlagColor.GREEN);
        LevelFlag blue2 = new LevelFlag(FlagColor.BLUE, 2);

        assertEquals("BaseFlag{color=GREEN}", green.toString());
        assertEquals("LevelFlag{color=BLUE, level=2}", blue2.toString());
    }
}
