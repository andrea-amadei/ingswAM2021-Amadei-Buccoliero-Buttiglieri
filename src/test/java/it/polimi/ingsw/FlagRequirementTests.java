package it.polimi.ingsw;

import it.polimi.ingsw.server.model.basetypes.BaseFlag;
import it.polimi.ingsw.server.model.basetypes.FlagColor;
import it.polimi.ingsw.server.model.basetypes.LevelFlag;
import it.polimi.ingsw.server.model.basetypes.ResourceTypeSingleton;
import it.polimi.ingsw.server.model.global.Board;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.holder.FaithHolder;
import it.polimi.ingsw.server.model.leader.FlagRequirement;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.server.model.storage.BaseStorage;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.server.model.storage.Storage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
    public void exceptionOnFlagRequirementConstructor(){

        assertThrows(IllegalArgumentException.class, ()-> new FlagRequirement(new BaseFlag(FlagColor.PURPLE), 0));
        assertThrows(IllegalArgumentException.class, ()-> new FlagRequirement(new BaseFlag(FlagColor.PURPLE), -7));
        assertThrows(NullPointerException.class, ()-> new FlagRequirement(null, 4));

    }

    @Test
    public void isSatisfiedMethodTest(){

        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("Player", 0, b);

        Storage storage1 = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production1 = new Production(3);
        FaithHolder faithHolder1 = new FaithHolder(3);

        Board b1 = new Board(storage1, production1, faithHolder1);
        Player player1 = new Player("Player1", 0, b1);

        LevelFlag levelFlag = new LevelFlag(FlagColor.GREEN, 3);
        LevelFlag levelFlag1 = new LevelFlag(FlagColor.PURPLE, 2);
        LevelFlag levelFlag2 = new LevelFlag(FlagColor.BLUE, 1);
        LevelFlag levelFlag3 = new LevelFlag(FlagColor.GREEN, 2);
        BaseFlag flag = new BaseFlag(FlagColor.GREEN);
        BaseFlag flag1 = new BaseFlag(FlagColor.PURPLE);

        player.getBoard().getFlagHolder().addFlag(levelFlag);
        player.getBoard().getFlagHolder().addFlag(levelFlag1);
        player.getBoard().getFlagHolder().addFlag(levelFlag3);
        player1.getBoard().getFlagHolder().addFlag(levelFlag2);

        FlagRequirement requirement = new FlagRequirement(flag, 2);
        FlagRequirement requirement1 = new FlagRequirement(flag, 4);
        FlagRequirement requirement2 = new FlagRequirement(flag1, 1);

        assertTrue(requirement.isSatisfied(player));
        assertFalse(requirement1.isSatisfied(player));
        assertFalse(requirement2.isSatisfied(player1));

    }

    @Test
    public void exceptionOnIsSatisfiedMethod(){

        BaseFlag flag = new BaseFlag(FlagColor.GREEN);
        FlagRequirement requirement = new FlagRequirement(flag, 2);

        assertThrows(NullPointerException.class, ()-> requirement.isSatisfied(null));

    }


}
