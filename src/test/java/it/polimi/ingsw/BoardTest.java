package it.polimi.ingsw;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.leader.LeaderCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        LeaderCard card = new LeaderCard(0, "Lorenzo il Magnifico", 100, new ArrayList<>(), new ArrayList<>());
        b.addLeaderCard(card);
        assertEquals(card, b.getLeaderCards().get(0));
    }

}
