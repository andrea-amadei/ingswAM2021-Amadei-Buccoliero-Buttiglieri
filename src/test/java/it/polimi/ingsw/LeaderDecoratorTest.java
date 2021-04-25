package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.DuplicatedShelfException;
import it.polimi.ingsw.exceptions.IllegalCupboardException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.storage.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;


import static org.junit.jupiter.api.Assertions.*;

public class LeaderDecoratorTest {

    @Test
    public void validCreation(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );
        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        Shelf leaderShelf = new Shelf("LeaderShelf1", ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        c = new LeaderDecorator(leaderShelf, c);

        assertEquals(c.toString(), "{BaseCupboard {BottomShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=3}, MiddleShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=2}, TopShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=1}}, LeaderShelf1{acceptedTypes=Gold, amount=2}}");
    }

    @Test
    public void invalidParametersCreation(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );
        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        Shelf leaderShelf2 = new Shelf("LeaderShelf2", ResourceTypeSingleton.getInstance().getGoldResource(), 2);
        assertDoesNotThrow(()->leaderShelf2.addResources(ResourceTypeSingleton.getInstance().getGoldResource(), 1));

        Shelf leaderShelf3 = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getGoldResource(), 2);

        assertThrows(NullPointerException.class, () -> new LeaderDecorator(null, c));
        assertThrows(DuplicatedShelfException.class, () -> new LeaderDecorator(leaderShelf3, c));
        assertThrows(IllegalArgumentException.class, () -> new LeaderDecorator(leaderShelf2, c));
    }

    @Test
    public void decorateTwoTimes(){
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

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        assertEquals(c1.toString(), "{{BaseCupboard {BottomShelf{Stone: 3}, MiddleShelf{Gold: 1}, TopShelf{Servant: 1}}, LeaderShelf1{acceptedTypes=Stone, amount=2}}, LeaderShelf2{acceptedTypes=Servant, amount=2}}");
    }

    @Test
    public void getShelvesTest(){
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

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        assertEquals(c1.getShelves(), new HashSet<>(Arrays.asList(bottom, middle, top, leaderShelf1, leaderShelf2)));
    }

    @Test
    public void validGetShelfById(){
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

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        assertEquals(c1.getShelfById("BottomShelf"), bottom);
        assertEquals(c1.getShelfById("MiddleShelf"), middle);
        assertEquals(c1.getShelfById("TopShelf"), top);
        assertEquals(c1.getShelfById("LeaderShelf1"), leaderShelf1);
        assertEquals(c1.getShelfById("LeaderShelf2"), leaderShelf2);
        assertEquals(c1.toString(), "{{BaseCupboard {BottomShelf{Stone: 3}, MiddleShelf{Gold: 1}, TopShelf{Servant: 1}}, LeaderShelf1{acceptedTypes=Stone, amount=2}}, LeaderShelf2{acceptedTypes=Servant, amount=2}}");
    }

    @Test
    public void invalidParametersGetShelfById(){

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

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        assertThrows(NullPointerException.class, ()->c2.getShelfById(null));
        assertThrows(NoSuchElementException.class, ()->c2.getShelfById("lol"));
        assertEquals(c1.toString(), "{{BaseCupboard {BottomShelf{Stone: 3}, MiddleShelf{Gold: 1}, TopShelf{Servant: 1}}, LeaderShelf1{acceptedTypes=Stone, amount=2}}, LeaderShelf2{acceptedTypes=Servant, amount=2}}");
    }

    @Test
    public void validMoveBetweenShelves(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, stone, 2));
        assertDoesNotThrow(()->c.addResource(top, servant, 1));

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        assertDoesNotThrow(() -> c2.moveBetweenShelves(bottom, middle, 2));
        assertEquals(c2.toString(), "{{BaseCupboard {BottomShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=3}, MiddleShelf{Stone: 2}, TopShelf{Servant: 1}}, LeaderShelf1{acceptedTypes=Stone, amount=2}}, LeaderShelf2{acceptedTypes=Servant, amount=2}}");
    }

    @Test
    public void invalidParametersMoveBetweenShelves(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, stone, 2));
        assertDoesNotThrow(()->c.addResource(top, servant, 1));

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        assertDoesNotThrow(()->c2.addResource(leaderShelf1, stone, 1));
        assertThrows(NullPointerException.class, () -> c2.moveBetweenShelves(null, bottom, 2));
        assertThrows(IllegalCupboardException.class, () -> c2.moveBetweenShelves(leaderShelf1, bottom, 1));
        assertThrows(NoSuchElementException.class, () -> c2.moveBetweenShelves(new Shelf("lol", gold, 3), bottom, 1));
    }

    @Test
    public void invalidParametersAddResource(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, stone, 2));
        assertDoesNotThrow(()->c.addResource(top, servant, 1));

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        assertThrows(NoSuchElementException.class, () -> c2.addResource(new Shelf("lol", gold, 3), gold, 2));
    }
    @Test
    public void invalidParametersRemoveResource(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, stone, 2));
        assertDoesNotThrow(()->c.addResource(top, servant, 1));

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        assertThrows(NoSuchElementException.class, () -> c2.removeResource(new Shelf("lol", gold, 3),  2));
    }

    @Test
    public void validRemoveResourceTest(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, stone, 2));
        assertDoesNotThrow(()->c.addResource(top, servant, 1));

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        assertDoesNotThrow(() -> c2.addResource(leaderShelf2, servant, 2));
        assertDoesNotThrow(() -> c2.removeResource(leaderShelf2, 1));
        assertDoesNotThrow(() -> c2.removeResource(bottom, 1));

        assertEquals(c2.toString(), "{{BaseCupboard {BottomShelf{Stone: 1}, MiddleShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=2}, TopShelf{Servant: 1}}, LeaderShelf1{acceptedTypes=Stone, amount=2}}, LeaderShelf2{Servant: 1}}");
    }

    @Test
    public void RemoveTooManyResources(){

        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, stone, 2));
        assertDoesNotThrow(()->c.addResource(top, servant, 1));

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        assertDoesNotThrow(() -> c2.addResource(leaderShelf2, servant, 2));
        assertThrows(IllegalCupboardException.class, () -> c2.removeResource(leaderShelf2, 3));

        assertEquals(c2.toString(), "{{BaseCupboard {BottomShelf{Stone: 2}, MiddleShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=2}, TopShelf{Servant: 1}}, LeaderShelf1{acceptedTypes=Stone, amount=2}}, LeaderShelf2{Servant: 2}}");
    }

    @Test
    public void AddTooManyResources(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, stone, 2));
        assertDoesNotThrow(()->c.addResource(top, servant, 1));

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        assertDoesNotThrow(() -> c2.addResource(leaderShelf2, servant, 1));
        assertThrows(IllegalCupboardException.class, () -> c2.addResource(leaderShelf2,servant, 3));

        assertEquals(c2.toString(), "{{BaseCupboard {BottomShelf{Stone: 2}, MiddleShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=2}, TopShelf{Servant: 1}}, LeaderShelf1{acceptedTypes=Stone, amount=2}}, LeaderShelf2{Servant: 1}}");
    }

    @Test
    public void containsTest(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        assertDoesNotThrow(()->c.addResource(bottom, stone, 2));
        assertDoesNotThrow(()->c.addResource(top, servant, 1));

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        assertFalse(c2.contains(null));
        assertFalse(c2.contains(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3)));
        assertTrue(c2.contains(bottom));
        assertTrue(c2.contains(leaderShelf1));
        assertTrue(c2.contains(leaderShelf2));
    }

    @Test
    public void validAddResourceFromContainer(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        ResourceContainer container = new BaseStorage("id");
        assertDoesNotThrow(() -> container.addResources(servant, 1));

        assertDoesNotThrow(() -> c2.addResourceFromContainer(container, leaderShelf2, servant, 1));
        assertEquals(c2.toString(), "{{BaseCupboard {BottomShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=3}, MiddleShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=2}, TopShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=1}}, LeaderShelf1{acceptedTypes=Stone, amount=2}}, LeaderShelf2{Servant: 1}}");
    }

    @Test
    public void emptyContainerInvalidAddResourceFromContainer(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        ResourceContainer container = new BaseStorage("id");
        assertDoesNotThrow(() -> container.addResources(servant, 1));

        assertDoesNotThrow(() -> c2.addResourceFromContainer(container, leaderShelf2, servant, 1));
        assertThrows(IllegalCupboardException.class, ()->c2.addResourceFromContainer(container, leaderShelf2, servant, 1));
        assertEquals(c2.toString(), "{{BaseCupboard {BottomShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=3}, MiddleShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=2}, TopShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=1}}, LeaderShelf1{acceptedTypes=Stone, amount=2}}, LeaderShelf2{Servant: 1}}");
    }

    @Test
    public void validMoveResourceToContainer(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        ResourceContainer container = new BaseStorage("id");
        assertDoesNotThrow(() -> c2.addResource(leaderShelf1, stone, 2));

        assertDoesNotThrow(() -> c2.moveResourceToContainer(container, leaderShelf1, stone, 1));
        assertEquals(c2.toString(), "{{BaseCupboard {BottomShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=3}, MiddleShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=2}, TopShelf{acceptedTypes=Any {Gold, Servant, Shield, Stone}, amount=1}}, LeaderShelf1{Stone: 1}}, LeaderShelf2{acceptedTypes=Servant, amount=2}}");
    }

    @Test
    public void noSuchElementMoveResourceToContainer(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        ResourceContainer container = new BaseStorage("id");
        assertDoesNotThrow(() -> c2.addResource(leaderShelf1, stone, 2));

        assertThrows(NoSuchElementException.class, () -> c2.moveResourceToContainer(container, new Shelf("test", stone, 2), stone, 1));
    }

    @Test
    public void invalidRemovalMoveResourceToContainer(){
        Shelf bottom = new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3);
        Shelf middle = new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2 );
        Shelf top = new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1 );

        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();
        ResourceSingle stone = ResourceTypeSingleton.getInstance().getStoneResource();

        Cupboard c = new BaseCupboard(Arrays.asList(bottom, middle, top));

        Shelf leaderShelf1 = new Shelf("LeaderShelf1", stone, 2);
        Shelf leaderShelf2 = new Shelf("LeaderShelf2", servant, 2);

        Cupboard c1;
        c1 = new LeaderDecorator(leaderShelf1, c);
        c1 = new LeaderDecorator(leaderShelf2, c1);

        Cupboard c2 = c1;

        ResourceContainer container = new BaseStorage("id");
        assertDoesNotThrow(() -> c2.addResource(leaderShelf1, stone, 2));

        assertThrows(IllegalCupboardException.class, () -> c2.moveResourceToContainer(container, leaderShelf1, servant, 1));
    }
}
