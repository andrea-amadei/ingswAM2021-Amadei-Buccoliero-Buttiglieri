package it.polimi.ingsw;

import it.polimi.ingsw.server.model.basetypes.FlagColor;
import it.polimi.ingsw.server.model.basetypes.LevelFlag;
import it.polimi.ingsw.server.model.holder.FlagHolder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FlagHolderTest {
    @Test
    public void testMethods() {
        FlagHolder fh = new FlagHolder();

        assertEquals(fh.getFlags().size(), 0);

        fh.addFlag(new LevelFlag(FlagColor.GREEN, 2));
        fh.addFlag(new LevelFlag(FlagColor.PURPLE, 1));
        fh.addFlag(new LevelFlag(FlagColor.GREEN, 2));
        fh.addFlag(new LevelFlag(FlagColor.GREEN, 3));

        assertEquals(fh.getFlags().size(), 3);

        assertEquals(fh.numberOfFlags(new LevelFlag(FlagColor.GREEN, 2)), 2);
        assertEquals(fh.numberOfFlags(new LevelFlag(FlagColor.GREEN, 3)), 1);
        assertEquals(fh.numberOfFlags(new LevelFlag(FlagColor.PURPLE, 1)), 1);
        assertEquals(fh.numberOfFlags(new LevelFlag(FlagColor.YELLOW, 2)), 0);

        assertEquals(fh.numberOfFlagsByColor(FlagColor.GREEN), 3);
        assertEquals(fh.numberOfFlagsByColor(FlagColor.YELLOW), 0);

        assertEquals(fh.numberOfFlagsByLevel(1), 1);
        assertEquals(fh.numberOfFlagsByLevel(2), 2);
        assertEquals(fh.numberOfFlagsByLevel(3), 1);
        assertEquals(fh.numberOfFlagsByLevel(4), 0);

        fh.reset();

        assertEquals(fh.getFlags().size(), 0);
    }

    @Test
    public void testExceptions() {
        FlagHolder fh = new FlagHolder();

        assertThrows(NullPointerException.class, () -> fh.addFlag(null));
        assertThrows(NullPointerException.class, () -> fh.numberOfFlags(null));
        assertThrows(NullPointerException.class, () -> fh.numberOfFlagsByColor(null));
        assertThrows(IllegalArgumentException.class, () -> fh.numberOfFlagsByLevel(-1));
    }
}
