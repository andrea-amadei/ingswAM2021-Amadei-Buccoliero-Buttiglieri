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
    public void exceptionOnLevelFlagRequirementConstructor(){

        assertThrows(IllegalArgumentException.class, ()-> new LevelFlagRequirement(new LevelFlag(FlagColor.GREEN, 1), -4));
        assertThrows(IllegalArgumentException.class, ()-> new LevelFlagRequirement(new LevelFlag(FlagColor.GREEN, 1), 0));
        assertThrows(NullPointerException.class, ()-> new LevelFlagRequirement(null, 1));

    }

    @Test
    public void isSatisfiedMethodTest(){

        Player player = new Player("Player", 2);
        Player player1 = new Player("Player1", 3);
        LevelFlag levelFlag = new LevelFlag(FlagColor.GREEN, 3);
        LevelFlag levelFlag1 = new LevelFlag(FlagColor.PURPLE, 2);
        LevelFlag levelFlag2 = new LevelFlag(FlagColor.BLUE, 1);
        LevelFlag levelFlag3 = new LevelFlag(FlagColor.GREEN, 2);
        LevelFlagRequirement requirement = new LevelFlagRequirement(levelFlag, 1);
        LevelFlagRequirement requirement1 = new LevelFlagRequirement(levelFlag, 3);
        LevelFlagRequirement requirement2 = new LevelFlagRequirement(levelFlag1, 2);

        player.getBoard().getFlagHolder().addFlag(levelFlag);
        player.getBoard().getFlagHolder().addFlag(levelFlag2);
        player.getBoard().getFlagHolder().addFlag(levelFlag3);
        for(int i=0; i<=4; i++)
            player1.getBoard().getFlagHolder().addFlag(levelFlag1);

        assertTrue(requirement.isSatisfied(player));
        assertFalse(requirement1.isSatisfied(player));
        assertTrue(requirement2.isSatisfied(player1));
        assertFalse(requirement2.isSatisfied(player));
    }

    @Test
    public void exceptionOnIsSatisfiedMethod(){
        LevelFlag levelFlag = new LevelFlag(FlagColor.GREEN, 3);
        LevelFlagRequirement requirement = new LevelFlagRequirement(levelFlag, 1);

        assertThrows(NullPointerException.class, ()-> requirement.isSatisfied(null));

    }
}
