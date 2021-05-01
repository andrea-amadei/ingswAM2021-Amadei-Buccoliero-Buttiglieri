package it.polimi.ingsw;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.DiscardLeaderAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.leader.*;
import it.polimi.ingsw.model.market.ConversionActuator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscardLeaderActionTest {

    private GameContext gameContext;
    private final ResourceSingle gold = ResourceTypeSingleton.getInstance().getGoldResource();
    private final ResourceSingle servant = ResourceTypeSingleton.getInstance().getServantResource();

    @BeforeEach
    public void init(){
        Player player1 = new Player("Ernestino", 0);
        Player player2 = new Player("Pollo", 1);
        GameModel model = new GameModel(Arrays.asList(player1, player2));
        gameContext = new GameContext(model);

        //Leader Lorenzo has a flag requirement and a discount ability
        BaseFlag flag1 = new BaseFlag(FlagColor.PURPLE);
        Requirement flagRequirement = new FlagRequirement(flag1, 2);
        SpecialAbility discountAbility = new DiscountAbility(4, servant);
        List<Requirement> requirements = new ArrayList<>();
        List<SpecialAbility> abilities = new ArrayList<>();
        requirements.add(flagRequirement);
        abilities.add(discountAbility);
        LeaderCard leaderCard1 = new LeaderCard(1, "Lorenzo", 6, abilities, requirements);

        //Ernestino has leader card Lorenzo
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Ernestino").getBoard()
        .getLeaderCards().add(leaderCard1));

        //Leader Gallina has a resource requirement and a conversion ability
        Requirement resourceRequirement = new ResourceRequirement(gold, 5);
        ConversionActuator actuator = new ConversionActuator(Collections.singletonList(gold), 1);
        SpecialAbility conversionAbility = new ConversionAbility(MarbleColor.BLUE, actuator);
        List<Requirement> requirements1 = new ArrayList<>();
        List<SpecialAbility> abilities1 = new ArrayList<>();
        requirements1.add(resourceRequirement);
        abilities1.add(conversionAbility);
        LeaderCard leaderCard2 = new LeaderCard(2, "Gallina", 3, abilities1, requirements1);

        //Pollo has leader card Gallina
        assertDoesNotThrow(()-> gameContext.getGameModel().getPlayerById("Pollo").getBoard()
        .getLeaderCards().add(leaderCard2));
    }

    @Test
    public void correctExecutionOfExecuteMethod(){
        Action action = new DiscardLeaderAction("Ernestino", 1);
        List<Message> messages;

        try{
            messages = action.execute(gameContext);
        }catch(IllegalActionException e){
            throw new RuntimeException();
        }

        assertEquals(0, gameContext.getGameModel().getPlayerById("Ernestino").getBoard()
        .getLeaderCards().size());
        assertEquals(1, gameContext.getGameModel().getPlayerById("Ernestino").getBoard()
        .getFaithHolder().getFaithPoints());
    }

    @Test
    public void nullPlayer(){
        assertThrows(NullPointerException.class, ()-> new DiscardLeaderAction(null, 1));
    }

    @Test
    public void invalidLeaderID(){
        assertThrows(IllegalArgumentException.class, ()-> new DiscardLeaderAction("Pollo", 0));
        assertThrows(IllegalArgumentException.class, ()-> new DiscardLeaderAction("Pollo", -3));
    }

    @Test
    public void invalidPlayer(){
        Action action = new DiscardLeaderAction("FakePlayer", 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void discardingNonOwnedLeader(){
        Action action = new DiscardLeaderAction("Pollo", 1);
        assertThrows(IllegalActionException.class, ()-> action.execute(gameContext));
    }

    @Test
    public void nullGameContext(){
        Action action = new DiscardLeaderAction("Pollo", 2);
        assertThrows(NullPointerException.class, ()-> action.execute(null));
    }

}
