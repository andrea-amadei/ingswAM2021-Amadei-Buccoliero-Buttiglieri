package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.leader.*;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.storage.Shelf;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class LeaderTests {

    @Test
    public void leaderCardConstructorTest(){

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();
        BaseFlag flag1 = new BaseFlag(FlagColor.PURPLE);
        LevelFlag flag2 = new LevelFlag(FlagColor.YELLOW, 3);
        Shelf shelf1 = new Shelf("shelf1", gold, 2);

        SpecialAbility discount1 = new DiscountAbility(1, stone);
        SpecialAbility discount2 = new DiscountAbility(4, servant);
        SpecialAbility conversion1 = new ConversionAbility(MarbleColor.YELLOW, shield);
        SpecialAbility storage1 = new StorageAbility(shelf1);
        //TODO: test crafting ability after crafting class is complete
        Requirement requirement1 = new FlagRequirement(flag1, 2);
        Requirement requirement2 = new LevelFlagRequirement(flag2, 1);
        Requirement requirement3 = new ResourceRequirement(servant, 5);

        List<SpecialAbility> abilities = new ArrayList<>();
        List<Requirement> requirements = new ArrayList<>();

        abilities.add(discount1);
        abilities.add(discount2);
        abilities.add(conversion1);
        abilities.add(storage1);
        requirements.add(requirement1);
        requirements.add(requirement2);
        requirements.add(requirement3);

        LeaderCard leaderCard1 = new LeaderCard(1, "Lorenzo", 6, abilities, requirements);

        assertEquals(leaderCard1.getId(), 1);
        assertSame("Lorenzo", leaderCard1.getName());
        assertEquals(leaderCard1.getPoints(), 6);
        assertNotNull(abilities);
        assertNotNull(requirements);
        assertEquals(leaderCard1.getAbilities(), abilities);
        assertEquals(leaderCard1.getRequirements(), requirements);
        assertFalse(leaderCard1.isActive());

    }

    @Test
    public void exceptionOnLeaderCardConstructor(){

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

        Requirement flagRequirement = new FlagRequirement(new BaseFlag(FlagColor.BLUE), 1);
        SpecialAbility conversion = new ConversionAbility(MarbleColor.YELLOW, servant);

        List<SpecialAbility> abilities = new ArrayList<>();
        List<Requirement> requirements = new ArrayList<>();

        abilities.add(conversion);
        requirements.add(flagRequirement);

        assertThrows(IllegalArgumentException.class, () -> new LeaderCard(-2, "Giorgio", 4, abilities, requirements));
        assertThrows(IllegalArgumentException.class, ()-> new LeaderCard(2, "S", 5, abilities, requirements));
        assertThrows(IllegalArgumentException.class, ()-> new LeaderCard(3, "Andrea", -5, abilities, requirements));
        assertThrows(NullPointerException.class, ()-> new LeaderCard(4, "Caterina", 7, null, requirements));
        assertThrows(NullPointerException.class, ()-> new LeaderCard(5, "Matilde", 5, abilities, null));

    }

}
