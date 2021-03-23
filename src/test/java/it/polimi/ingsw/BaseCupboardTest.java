package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.DuplicatedShelfException;
import it.polimi.ingsw.exceptions.IllegalCupboardException;
import it.polimi.ingsw.exceptions.UnsupportedShelfInsertionException;
import it.polimi.ingsw.exceptions.UnsupportedShelfRemovalException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.storage.BaseCupboard;
import it.polimi.ingsw.model.storage.Cupboard;
import it.polimi.ingsw.model.storage.Shelf;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class BaseCupboardTest {
    @Test
    public void validConstruction(){

        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );
        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertTrue(c.contains(bottom));
        assertTrue(c.contains(middle));
        assertTrue(c.contains(top));
        assertEquals(new HashSet<>(Arrays.asList(bottom, middle, top)), c.getShelves());
        assertEquals(c.toString(), "BaseCupboard {BottomShelf {}, MiddleShelf {}, TopShelf {}}");
    }

    @Test
    public void duplicateShelvesConstruction(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );
        assertThrows(DuplicatedShelfException.class, ()->new BaseCupboard(Arrays.asList(bottom, top, bottom)));
    }

    @Test
    public void duplicateShelveIdConstruction(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf anotherBottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2);
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );
        assertThrows(DuplicatedShelfException.class, ()->new BaseCupboard(Arrays.asList(bottom, anotherBottom, top)));
    }

    @Test
    public void nonEmptyShelvesConstructor(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2);
        bottom.addResources(ResourceTypeSingleton.getInstance().getGoldResource(), 1);

        assertThrows(IllegalArgumentException.class, () -> new BaseCupboard(Arrays.asList(bottom, middle)));
    }

    @Test
    public void nullOrEmptyShelfListConstruction(){
        assertThrows(NullPointerException.class, () -> new BaseCupboard(null));
        assertThrows(IllegalArgumentException.class, () -> new BaseCupboard(new ArrayList<>()));
    }

    @Test
    public void nullShelfInListConstruction(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        assertThrows(IllegalArgumentException.class, () -> new BaseCupboard(Arrays.asList(bottom, null)));
    }

    @Test
    public void validGetById(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );
        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));
        assertEquals(bottom, c.getShelfById("BottomShelf"));
        assertEquals(middle, c.getShelfById("MiddleShelf"));
        assertEquals(top, c.getShelfById("TopShelf"));
        assertEquals(c.toString(), "BaseCupboard {BottomShelf {}, MiddleShelf {}, TopShelf {}}");
    }

    @Test
    public void nullArgumentGetById(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );
        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));
        assertThrows(NullPointerException.class, () -> c.getShelfById(null));
        assertEquals(c.toString(), "BaseCupboard {BottomShelf {}, MiddleShelf {}, TopShelf {}}");
    }

    @Test
    public void noMatchGetById(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );
        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));
        assertThrows(NoSuchElementException.class, () -> c.getShelfById("hmm"));
        assertEquals(c.toString(), "BaseCupboard {BottomShelf {}, MiddleShelf {}, TopShelf {}}");
    }

    @Test
    public void validAdding(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, stone, 3));
        assertDoesNotThrow(()->c.addResource(middle, gold, 1));
        assertDoesNotThrow(()->c.addResource(top, servant, 1));
        assertEquals(c.toString(), "BaseCupboard {BottomShelf {Stone: 3}, MiddleShelf {Gold: 1}, TopShelf {Servant: 1}}");
    }

    @Test
    public void addingInvalidParameters(){

        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle shield = ResourceTypeSingleton.getInstance().getShieldResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertThrows(NullPointerException.class, () -> c.addResource(null, shield, 3));
        assertThrows(IllegalArgumentException.class, () -> c.addResource(bottom, shield, -1));
        assertThrows(IllegalArgumentException.class, () -> c.addResource(bottom, shield, 0));

        assertEquals(c.toString(), "BaseCupboard {BottomShelf {}, MiddleShelf {}, TopShelf {}}");
    }

    @Test
    public void addingSameShelfMultipleTypes(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, stone, 1));
        assertThrows(UnsupportedShelfInsertionException.class, ()->c.addResource(bottom, servant, 1));

        assertEquals(c.toString(), "BaseCupboard {BottomShelf {Stone: 1}, MiddleShelf {}, TopShelf {}}");
    }

    @Test
    public void addingInvalidConfiguration(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(() -> c.addResource(bottom, stone, 2));
        assertThrows(IllegalCupboardException.class, ()->c.addResource(top, stone, 1));
        assertEquals(c.toString(), "BaseCupboard {BottomShelf {Stone: 2}, MiddleShelf {}, TopShelf {}}");
    }

    @Test
    public void addNonContainedShelf(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertThrows(NoSuchElementException.class, ()->c.addResource(new Shelf("fakeID", gold, 2), stone, 1));
        assertEquals(c.toString(), "BaseCupboard {BottomShelf {}, MiddleShelf {}, TopShelf {}}");
    }

    @Test
    public void moveResourceInvalidArguments(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertThrows(NullPointerException.class, () -> c.moveBetweenShelves(null, bottom, 3));
        assertThrows(IllegalArgumentException.class, () -> c.moveBetweenShelves(bottom, bottom, 0));
        assertThrows(IllegalArgumentException.class, () -> c.moveBetweenShelves(top, bottom, -1));
        assertThrows(NoSuchElementException.class, () -> c.moveBetweenShelves(bottom, new Shelf("a", gold, 3), 3));

        assertEquals(c.toString(), "BaseCupboard {BottomShelf {}, MiddleShelf {}, TopShelf {}}");
    }

    @Test
    public void invalidRemoveInTransfer(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );


        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertThrows(UnsupportedShelfRemovalException.class, () -> c.moveBetweenShelves(top, bottom,1));
        assertEquals(c.toString(), "BaseCupboard {BottomShelf {}, MiddleShelf {}, TopShelf {}}");
    }

    @Test
    public void invalidInsertionInTransfer(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, stone, 3));
        assertDoesNotThrow(()->c.addResource(middle, gold, 1));
        assertDoesNotThrow(()->c.addResource(top, servant, 1));
        assertEquals(c.toString(), "BaseCupboard {BottomShelf {Stone: 3}, MiddleShelf {Gold: 1}, TopShelf {Servant: 1}}");

        assertThrows(UnsupportedShelfInsertionException.class, ()->c.moveBetweenShelves(top, bottom, 1));
        assertEquals(c.toString(), "BaseCupboard {BottomShelf {Stone: 3}, MiddleShelf {Gold: 1}, TopShelf {Servant: 1}}");
    }

    @Test
    public void invalidConfigurationTransfer(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, stone, 3));
        assertDoesNotThrow(()->c.addResource(middle, gold, 2));

        assertThrows(IllegalCupboardException.class, ()->c.moveBetweenShelves(middle, top, 1));
        assertEquals(c.toString(), "BaseCupboard {BottomShelf {Stone: 3}, MiddleShelf {Gold: 2}, TopShelf {}}");

    }

    @Test
    public void validTransfer(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, stone, 2));
        assertDoesNotThrow(()->c.addResource(top, gold, 1));
        assertDoesNotThrow(()->c.moveBetweenShelves(bottom, middle, 2));

        assertEquals(c.toString(), "BaseCupboard {BottomShelf {}, MiddleShelf {Stone: 2}, TopShelf {Gold: 1}}");
    }

    @Test
    public void removeBadParameters(){

        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertThrows(NullPointerException.class, ()->c.removeResource(null, 3));
        assertThrows(IllegalArgumentException.class, ()->c.removeResource(bottom, 0));
        assertThrows(IllegalArgumentException.class, ()->c.removeResource(top, -1));
        assertThrows(NoSuchElementException.class, ()->c.removeResource(new Shelf("BottomShelf", gold, 3), 3));

        assertEquals(c.toString(), "BaseCupboard {BottomShelf {}, MiddleShelf {}, TopShelf {}}");
    }

    @Test
    public void validRemove(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, gold, 2));
        assertDoesNotThrow(()->c.addResource(top, servant, 1));
        assertDoesNotThrow(()->c.removeResource(top, 1));

        assertEquals(c.toString(), "BaseCupboard {BottomShelf {Gold: 2}, MiddleShelf {}, TopShelf {}}");
    }

}
