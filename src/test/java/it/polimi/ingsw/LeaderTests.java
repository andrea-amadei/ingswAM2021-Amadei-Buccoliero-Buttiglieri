package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.AlreadyActiveException;
import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.exceptions.RequirementsNotSatisfiedException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.leader.*;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.model.storage.BaseStorage;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.model.storage.Storage;
import org.junit.jupiter.api.Test;

import java.util.*;

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
        HashMap<ResourceType, Integer> input = new HashMap<>();
        HashMap<ResourceType, Integer> output = new HashMap<>();
        input.put(shield, 2);
        output.put(gold, 3);
        Crafting crafting = new Crafting(input, output, 1);

        SpecialAbility discountAbility1 = new DiscountAbility(1, stone);
        SpecialAbility discountAbility2 = new DiscountAbility(4, servant);
        SpecialAbility conversionAbility = new ConversionAbility(MarbleColor.YELLOW,
                new ConversionActuator(Collections.singletonList(shield), 0));
        SpecialAbility storageAbility = new StorageAbility(shelf1);
        CraftingAbility craftingAbility = new CraftingAbility(crafting);
        Requirement requirement1 = new FlagRequirement(flag1, 2);
        Requirement requirement2 = new LevelFlagRequirement(flag2, 1);
        Requirement requirement3 = new ResourceRequirement(servant, 5);

        List<SpecialAbility> abilities = new ArrayList<>();
        List<Requirement> requirements = new ArrayList<>();

        abilities.add(discountAbility1);
        abilities.add(discountAbility2);
        abilities.add(conversionAbility);
        abilities.add(storageAbility);
        abilities.add(craftingAbility);
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

        ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

        Requirement flagRequirement = new FlagRequirement(new BaseFlag(FlagColor.BLUE), 1);
        SpecialAbility conversion = new ConversionAbility(MarbleColor.YELLOW,
                new ConversionActuator(Collections.singletonList(servant), 0));

        List<SpecialAbility> abilities = new ArrayList<>();
        List<Requirement> requirements = new ArrayList<>();
        List<SpecialAbility> abilitiesSizeZero = new ArrayList<>();
        List<Requirement> requirementsSizeZero = new ArrayList<>();

        abilities.add(conversion);
        requirements.add(flagRequirement);

        assertThrows(IllegalArgumentException.class, () -> new LeaderCard(-2, "Giorgio", 4, abilities, requirements));
        assertThrows(IllegalArgumentException.class, ()-> new LeaderCard(2, "S", 5, abilities, requirements));
        assertThrows(NullPointerException.class, ()-> new LeaderCard(4, null, 7, abilities, requirements));
        assertThrows(IllegalArgumentException.class, ()-> new LeaderCard(3, "Andrea", -5, abilities, requirements));
        assertThrows(NullPointerException.class, ()-> new LeaderCard(4, "Caterina", 7, null, requirements));
        assertThrows(NullPointerException.class, ()-> new LeaderCard(5, "Matilde", 5, abilities, null));
        assertThrows(IllegalArgumentException.class, ()-> new LeaderCard(3, "EmptyAbilities", 4, abilitiesSizeZero, requirements));
        assertThrows(IllegalArgumentException.class, ()-> new LeaderCard(2, "EmptyRequirements", 3, abilities, requirementsSizeZero));

    }

    @Test
    public void activateMethodAddsAbility() throws RequirementsNotSatisfiedException, AlreadyActiveException {
        List<SpecialAbility> abilities = new ArrayList<>();
        List<Requirement> requirements = new ArrayList<>();
        ConversionActuator conversionActuator = new ConversionActuator(Collections.singletonList(ResourceTypeSingleton.getInstance().getServantResource()), 0);
        SpecialAbility conversionAbility = new ConversionAbility(MarbleColor.YELLOW, conversionActuator);
        Requirement flagRequirement = new FlagRequirement(new BaseFlag(FlagColor.BLUE), 1);
        abilities.add(conversionAbility);
        requirements.add(flagRequirement);
        LeaderCard leaderCard = new LeaderCard(3, "Genoveffa", 7, abilities, requirements);

        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("Player", 0, b);
        player.getBoard().getFlagHolder().addFlag(new LevelFlag(FlagColor.BLUE, 2));

        leaderCard.activate(player);

        assertEquals(player.getBoard().getConversionHolder().getActuatorsFromColor(MarbleColor.YELLOW), Collections.singletonList(conversionActuator));
    }

    @Test
    public void activateMethodChangesStatus() throws RequirementsNotSatisfiedException, AlreadyActiveException {
        List<SpecialAbility> abilities = new ArrayList<>();
        List<Requirement> requirements = new ArrayList<>();
        SpecialAbility conversionAbility = new ConversionAbility(MarbleColor.YELLOW,
                new ConversionActuator(Collections.singletonList(ResourceTypeSingleton.getInstance().getServantResource()), 0));
        Requirement flagRequirement = new FlagRequirement(new BaseFlag(FlagColor.BLUE), 1);
        abilities.add(conversionAbility);
        requirements.add(flagRequirement);
        LeaderCard leaderCard = new LeaderCard(3, "Genoveffa", 7, abilities, requirements);
        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("Player", 2, b);
        player.getBoard().getFlagHolder().addFlag(new LevelFlag(FlagColor.BLUE, 2));

        leaderCard.activate(player);

        assertTrue(leaderCard.isActive());
    }

    @Test
    public void exceptionOnActivateMethod(){
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        Requirement resourceRequirement = new ResourceRequirement(gold, 5);
        Shelf shelf = new Shelf("shelf1", gold, 2);
        SpecialAbility storageAbility = new StorageAbility(shelf);
        List<SpecialAbility> abilities = new ArrayList<>();
        List<Requirement> requirements = new ArrayList<>();
        abilities.add(storageAbility);
        requirements.add(resourceRequirement);
        LeaderCard leaderCard = new LeaderCard(5, "Eren", 9, abilities, requirements);

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


        assertDoesNotThrow(()-> player1.getBoard().getStorage().getChest().addResources(gold, 6));

        assertThrows(NullPointerException.class, ()-> leaderCard.activate(null));
        assertThrows(RequirementsNotSatisfiedException.class, ()-> leaderCard.activate(player));
        assertThrows(AlreadyActiveException.class, ()-> {leaderCard.activate(player1); leaderCard.activate(player1);});
    }

    @Test
    public void isSatisfiedMethodTest() {
        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player1 = new Player("Player", 0, b);


        Storage storage1 = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production1 = new Production(3);
        FaithHolder faithHolder1 = new FaithHolder(3);

        Board b1 = new Board(storage1, production1, faithHolder1);
        Player player2 = new Player("Mikasa", 0, b1);


        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        Requirement resourceRequirement = new ResourceRequirement(gold, 5);
        Shelf shelf = new Shelf("shelf1", gold, 2);
        SpecialAbility storageAbility = new StorageAbility(shelf);
        List<SpecialAbility> abilities = new ArrayList<>();
        List<Requirement> requirements = new ArrayList<>();
        abilities.add(storageAbility);
        requirements.add(resourceRequirement);
        player2.getBoard().getStorage().getChest().addResources(gold, 7);
        LeaderCard leaderCard = new LeaderCard(5, "Eren", 9, abilities, requirements);

        assertFalse(leaderCard.isSatisfied(player1));
        assertTrue(leaderCard.isSatisfied(player2));
    }

    @Test
    public void exceptionOnIsSatisfiedMethod(){
        ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
        Requirement resourceRequirement = new ResourceRequirement(gold, 5);
        Shelf shelf = new Shelf("shelf1", gold, 2);
        SpecialAbility storageAbility = new StorageAbility(shelf);
        List<SpecialAbility> abilities = new ArrayList<>();
        List<Requirement> requirements = new ArrayList<>();
        abilities.add(storageAbility);
        requirements.add(resourceRequirement);
        LeaderCard leaderCard = new LeaderCard(5, "Eren", 9, abilities, requirements);

        assertThrows(NullPointerException.class, ()-> leaderCard.isSatisfied(null));
    }

    @Test
    public void activateMultipleAbilities() throws RequirementsNotSatisfiedException, AlreadyActiveException {
        List<SpecialAbility> abilities = new ArrayList<>();
        List<Requirement> requirements = new ArrayList<>();
        ConversionActuator conversionActuator = new ConversionActuator(Collections.singletonList(ResourceTypeSingleton.getInstance().getServantResource()), 0);
        SpecialAbility conversionAbility = new ConversionAbility(MarbleColor.YELLOW, conversionActuator);
        SpecialAbility storageAbility = new StorageAbility(new Shelf("shelf1", ResourceTypeSingleton.getInstance().getServantResource(), 2));
        SpecialAbility craftingAbility = new CraftingAbility(new Crafting(new HashMap<>(){{
            put(ResourceTypeSingleton.getInstance().getGoldResource(), 1);
            put(ResourceTypeSingleton.getInstance().getServantResource(), 1);
        }}, new HashMap<>(){{
            put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
        }}, 2));
        Requirement flagRequirement = new FlagRequirement(new BaseFlag(FlagColor.BLUE), 1);
        abilities.add(conversionAbility);
        abilities.add(storageAbility);
        abilities.add(craftingAbility);
        requirements.add(flagRequirement);
        LeaderCard leaderCard = new LeaderCard(3, "Genoveffa", 7, abilities, requirements);

        Storage storage = new Storage(new BaseStorage("Chest"), new BaseStorage("Hand"), new BaseStorage("MarketBasket"),
                Arrays.asList(new Shelf("BottomShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 3),
                        new Shelf("MiddleShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 2),
                        new Shelf("TopShelf", ResourceTypeSingleton.getInstance().getAnyResource(), 1)));

        Production production = new Production(3);
        FaithHolder faithHolder = new FaithHolder(3);

        Board b = new Board(storage, production, faithHolder);
        Player player = new Player("Player", 0, b);

        player.getBoard().getFlagHolder().addFlag(new LevelFlag(FlagColor.BLUE, 2));

        leaderCard.activate(player);
    }



}
