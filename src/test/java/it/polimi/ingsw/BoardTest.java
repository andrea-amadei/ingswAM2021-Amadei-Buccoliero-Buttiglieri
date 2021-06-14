package it.polimi.ingsw;

import it.polimi.ingsw.gamematerials.BaseFlag;
import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.leader.*;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.storage.BaseStorage;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.model.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BoardTest {

    private Storage storage;
    private Production production;
    private FaithHolder faithHolder;

    @BeforeEach
    public void init(){
        storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        production = new Production(3);
        faithHolder = new FaithHolder(3);
    }
    @Test
    public void checkBoardInstantiation(){
        assertDoesNotThrow(() -> new Board(storage, production, faithHolder));
    }

    @Test
    public void storageCreation(){
        Board b = new Board(storage, production, faithHolder);
        assertNotNull(b.getStorage());
    }

    @Test
    public void leaderCardListCreation(){
        Board b = new Board(storage, production, faithHolder);
        assertNotNull(b.getLeaderCards());
    }

    @Test
    public void discountHolderCreation(){
        Board b = new Board(storage, production, faithHolder);
        assertNotNull(b.getDiscountHolder());
    }

    @Test
    public void flagHolderCreation(){
        Board b = new Board(storage, production, faithHolder);
        assertNotNull(b.getFlagHolder());
    }

    @Test
    public void productionCreation(){
        Board b = new Board(storage, production, faithHolder);
        assertNotNull(b.getProduction());
    }

    @Test
    public void faithHolderCreation(){
        Board b = new Board(storage, production, faithHolder);
        assertNotNull(b.getFaithHolder());
    }

    @Test
    public void addNullLeaderCard(){
        Board b = new Board(storage, production, faithHolder);
        assertThrows(NullPointerException.class, () -> b.addLeaderCard(null));
    }

    @Test
    public void addNewCard(){
        Board b = new Board(storage, production, faithHolder);
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
        Board b = new Board(storage, production, faithHolder);
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
        Board b = new Board(storage, production, faithHolder);
        assertThrows(IllegalArgumentException.class, ()-> b.getLeaderCardByID(0));
        assertThrows(IllegalArgumentException.class, ()-> b.getLeaderCardByID(-5));
    }

    @Test
    public void noSuchElementExceptionOnGetLeaderCardByID(){
        Board b = new Board(storage, production, faithHolder);
        assertThrows(NoSuchElementException.class, ()-> b.getLeaderCardByID(1));
    }

}
