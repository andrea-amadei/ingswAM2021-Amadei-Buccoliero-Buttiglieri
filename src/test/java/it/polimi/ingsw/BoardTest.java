package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.BaseFlag;
import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.leader.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Test
    public void checkBoardInstantiation(){
        assertDoesNotThrow(Board::new);
    }

    @Test
    public void storageCreation(){
        Board b = new Board();
        assertNotNull(b.getStorage());
    }

    @Test
    public void leaderCardListCreation(){
        Board b = new Board();
        assertNotNull(b.getLeaderCards());
    }

    @Test
    public void discountHolderCreation(){
        Board b = new Board();
        assertNotNull(b.getDiscountHolder());
    }

    @Test
    public void flagHolderCreation(){
        Board b = new Board();
        assertNotNull(b.getFlagHolder());
    }

    @Test
    public void productionCreation(){
        Board b = new Board();
        assertNotNull(b.getProduction());
    }

    @Test
    public void faithHolderCreation(){
        Board b = new Board();
        assertNotNull(b.getFaithHolder());
    }

    @Test
    public void addNullLeaderCard(){
        Board b = new Board();
        assertThrows(NullPointerException.class, () -> b.addLeaderCard(null));
    }

    @Test
    public void addNewCard(){
        Board b = new Board();
        ArrayList<SpecialAbility> abilities = new ArrayList<>() {{
            add(new DiscountAbility(1, ResourceTypeSingleton.getInstance().getGoldResource()));
        }};
        ArrayList<Requirement> requirements = new ArrayList<>() {{
            add(new FlagRequirement(new BaseFlag(FlagColor.GREEN), 1));
        }};

        LeaderCard card = new LeaderCard(1, "Lorenzo il Magnifico", 100, abilities, requirements);
        b.addLeaderCard(card);
        assertEquals(card, b.getLeaderCards().get(0));
    }

    @Test
    public void correctUsageOfGetLeaderCardByID(){
        Board b = new Board();
        ArrayList<SpecialAbility> abilities = new ArrayList<>() {{
            add(new DiscountAbility(1, ResourceTypeSingleton.getInstance().getGoldResource()));
        }};
        ArrayList<Requirement> requirements = new ArrayList<>() {{
            add(new FlagRequirement(new BaseFlag(FlagColor.GREEN), 1));
        }};

        LeaderCard card = new LeaderCard(1, "Lorenzo il Magnifico", 100, abilities, requirements);
        b.addLeaderCard(card);

        assertEquals(card, b.getLeaderCardByID(1));
    }
    
    @Test
    public void illegalArgumentExceptionOnGetLeaderCardByID(){
        Board b = new Board();
        assertThrows(IllegalArgumentException.class, ()-> b.getLeaderCardByID(0));
        assertThrows(IllegalArgumentException.class, ()-> b.getLeaderCardByID(-5));
    }

    @Test
    public void noSuchElementExceptionOnGetLeaderCardByID(){
        Board b = new Board();
        assertThrows(NoSuchElementException.class, ()-> b.getLeaderCardByID(1));
    }

}
