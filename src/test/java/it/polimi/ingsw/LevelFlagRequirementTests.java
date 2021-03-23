package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.gamematerials.LevelFlag;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.leader.LevelFlagRequirement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LevelFlagRequirementTests {

    @Test
    public void levelFlagRequirementConstructorTest(){

        LevelFlag flag = new LevelFlag(FlagColor.BLUE, 3);
        LevelFlagRequirement levelFlagRequirement = new LevelFlagRequirement(flag, 2);

        assertEquals(levelFlagRequirement.getFlag(), flag);
        assertEquals(levelFlagRequirement.getAmount(), 2);
        assertNotNull(levelFlagRequirement.getFlag());

    }

    @Test
    public void isSatisfiedTest(){

        LevelFlagRequirement levelFlagRequirement = new LevelFlagRequirement(new LevelFlag(FlagColor.PURPLE, 2), 4);

        assertFalse(levelFlagRequirement.isSatisfied(new Player("Player", 1)));

    }

    @Test
    public void exceptionOnLevelFlagRequirementConstructor(){

        assertThrows(IllegalArgumentException.class, ()-> new LevelFlagRequirement(new LevelFlag(FlagColor.GREEN, 1), -4));
        assertThrows(IllegalArgumentException.class, ()-> new LevelFlagRequirement(new LevelFlag(FlagColor.GREEN, 1), 0));
        assertThrows(NullPointerException.class, ()-> new LevelFlagRequirement(null, 1));

    }
}
