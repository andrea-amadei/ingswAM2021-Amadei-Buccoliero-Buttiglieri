package it.polimi.ingsw;

import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.basetypes.*;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.actions.Action;
import it.polimi.ingsw.server.model.actions.ActivateLeaderAction;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.leader.*;
import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.server.ServerBuilder;
import it.polimi.ingsw.common.utils.ResourceLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ActivateLeaderActionTest {

    private GameContext gameContext;
    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

    @BeforeEach
    public void init() throws ParserException {

        List<String> usernames = Arrays.asList("Ernestino", "Ermenegildo");
        String config = ResourceLoader.loadFile("cfg/config.json");
        String crafting = ResourceLoader.loadFile("cfg/crafting.json");
        String faith = ResourceLoader.loadFile("cfg/faith.json");
        String leaders = ResourceLoader.loadFile("cfg/leaders.json");

        GameModel model  = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames, false, new Random(3));

        gameContext = new GameContext(model, false);

        Player player1 = model.getPlayerById("Ernestino");
        Player player2 = model.getPlayerById("Ermenegildo");

        //creating leader cards
        BaseFlag flag1 = new BaseFlag(FlagColor.PURPLE);
        Shelf shelf = new Shelf("ExtraShelf", gold, 1);
        SpecialAbility discountAbility = new DiscountAbility(4, servant);
        SpecialAbility storageAbility = new StorageAbility(shelf);
        Requirement flagRequirement = new FlagRequirement(flag1, 2);
        Requirement resourceRequirement = new ResourceRequirement(gold, 5);
        Requirement resourceRequirement1 = new ResourceRequirement(servant, 1);
        List<SpecialAbility> abilities = new ArrayList<>();
        List<SpecialAbility> abilities1 = new ArrayList<>();
        List<Requirement> requirements = new ArrayList<>();
        List<Requirement> requirements1 = new ArrayList<>();
        List<Requirement> requirements2 = new ArrayList<>();
        abilities.add(discountAbility);
        abilities1.add(storageAbility);
        requirements.add(flagRequirement);
        requirements1.add(resourceRequirement);
        requirements2.add(resourceRequirement1);
        LeaderCard leaderCard1 = new LeaderCard(1, "Lorenzo", 6, abilities, requirements);
        LeaderCard leaderCard2 = new LeaderCard(2, "Pollo", 1, abilities, requirements1);
        LeaderCard leaderCard3 = new LeaderCard(3, "Gallina", 2, abilities1, requirements2);

        //adding leader cards to players
        assertDoesNotThrow(()-> player1.getBoard().getLeaderCards().add(leaderCard1));
        assertDoesNotThrow(()-> player2.getBoard().getLeaderCards().add(leaderCard2));
        assertDoesNotThrow(()-> player1.getBoard().getFlagHolder().addFlag(new LevelFlag(FlagColor.PURPLE, 2)));
        assertDoesNotThrow(()-> player1.getBoard().getFlagHolder().addFlag(new LevelFlag(FlagColor.PURPLE, 2)));
        assertDoesNotThrow(()-> player2.getBoard().getStorage().getChest().addResources(servant, 3));
        assertDoesNotThrow(()-> player2.getBoard().getLeaderCards().add(leaderCard3));

        //Ernestino has Lorenzo and he satisfies the requirements
        //Ermenegildo has Pollo and does not satisfy the requirements
        //Ermenegildo has Gallina and tries to activate the card twice
    }

    @Test
    public void correctExecutionOfExecuteMethod(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        Action action = new ActivateLeaderAction("Ernestino", 1);

        try{
            action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

        assertEquals(4, gameContext.getGameModel().getPlayerById("Ernestino").getBoard().getDiscountHolder().totalDiscountByResource(servant));
    }

    @Test
    public void nullPlayer(){
        assertThrows(NullPointerException.class, ()-> new ActivateLeaderAction(null, 1));
    }

    @Test
    public void invalidLeaderID(){
        assertThrows(IllegalArgumentException.class, ()-> new ActivateLeaderAction("Ermenegildo", 0));
        assertThrows(IllegalArgumentException.class, ()-> new ActivateLeaderAction("Ermenegildo", -2));
    }

    @Test
    public void invalidPlayer(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ernestino"));
        Action action = new ActivateLeaderAction("Teodolinda", 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void activatingNonExistentLeader(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ermenegildo"));
        Action action = new ActivateLeaderAction("Ermenegildo", 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void activatingLeaderWithoutSatisfyingRequirements(){
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ermenegildo"));
        Action action = new ActivateLeaderAction("Ermenegildo", 2);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void activatingAnAlreadyActiveLeaderCard() throws IllegalActionException {
        gameContext.setCurrentPlayer(gameContext.getGameModel().getPlayerById("Ermenegildo"));
        Action action = new ActivateLeaderAction("Ermenegildo", 3);
        action.execute(gameContext);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void nullGameContext(){
        Action action = new ActivateLeaderAction("Ermenegildo", 3);
        assertThrows(NullPointerException.class, ()-> action.execute(null));
    }

}
