package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.BaseFlag;
import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.gamematerials.LevelFlag;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.leader.FlagRequirement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FlagRequirementTests {

    @Test
    public void flagRequirementConstructorTest(){

        BaseFlag flag1 = new BaseFlag(FlagColor.PURPLE);
        LevelFlag flag2 = new LevelFlag(FlagColor.YELLOW, 2);
        BaseFlag flag3 = new BaseFlag(FlagColor.YELLOW);

        FlagRequirement flagRequirement1 = new FlagRequirement(flag1, 5);
        FlagRequirement flagRequirement2 = new FlagRequirement(flag2, 3);

        assertNotSame(flagRequirement1.getFlag(), flag2);
        assertEquals(flagRequirement1.getFlag(), flag1);
        assertEquals(flagRequirement1.getAmount(), 5);
        assertSame(flagRequirement2.getFlag().getColor(), flag3.getColor());
    }

    @Test
    public void isSatisfiedTest(){

        FlagRequirement flagRequirement = new FlagRequirement(new BaseFlag(FlagColor.PURPLE), 4);

        assertFalse(flagRequirement.isSatisfied(new Player("Player", 1)));

    }

    @Test
    public void exceptionOnFlagRequirementConstructor(){

        assertThrows(IllegalArgumentException.class, ()-> new FlagRequirement(new BaseFlag(FlagColor.PURPLE), 0));
        assertThrows(IllegalArgumentException.class, ()-> new FlagRequirement(new BaseFlag(FlagColor.PURPLE), -7));
        assertThrows(NullPointerException.class, ()-> new FlagRequirement(null, 4));

    }


}
