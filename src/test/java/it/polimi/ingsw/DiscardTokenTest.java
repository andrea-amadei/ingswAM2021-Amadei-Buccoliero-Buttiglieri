package it.polimi.ingsw;
import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.server.model.basetypes.FlagColor;
import it.polimi.ingsw.server.model.global.GameModel;
import it.polimi.ingsw.server.model.actions.*;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.fsm.StateMachine;
import it.polimi.ingsw.server.model.fsm.states.MenuState;
import it.polimi.ingsw.server.model.lorenzo.DiscardToken;
import it.polimi.ingsw.server.model.lorenzo.Token;
import it.polimi.ingsw.server.ServerBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiscardTokenTest {

    private GameModel gameModel;
    private GameContext gameContext;
    private StateMachine stateMachine;
    private ActionQueue actionQueue;

    @BeforeEach
    public void init() throws ParserException {
        List<String> usernames = Collections.singletonList("Ernestino");

        String config = "{\"max_username_length\":30,\"min_username_length\":2,\"max_card_level\":3,\"min_card_level\":1,\"amount_of_leaders_to_discard\":2,\"first_player_amount_of_faith_points_on_start\":0,\"second_player_amount_of_faith_points_on_start\":0,\"third_player_amount_of_faith_points_on_start\":1,\"fourth_player_amount_of_faith_points_on_start\":1,\"first_player_amount_of_resources_on_start\":0,\"second_player_amount_of_resources_on_start\":1,\"third_player_amount_of_resources_on_start\":1,\"fourth_player_amount_of_resources_on_start\":2,\"base_cupboard_shelf_names\":[\"TopShelf\",\"MiddleShelf\",\"BottomShelf\"],\"base_cupboard_shelf_types\":[\"any\",\"any\",\"any\"],\"base_cupboard_shelf_sizes\":[1,2,3],\"hand_id\":\"Hand\",\"basket_id\":\"MarketBasket\",\"chest_id\":\"Chest\",\"market_rows\":3,\"market_columns\":4,\"marble_per_color\":{\"BLUE\":2,\"WHITE\":4,\"GREY\":2,\"RED\":1,\"PURPLE\":2,\"YELLOW\":2},\"upgradable_crafting_number\":3,\"faith_checkpoint_number\":3}";
        String leaders = "{\"cards\":[{\"id\":1,\"name\":\"aaa\",\"points\":2,\"requirements\":[{\"type\":\"flag\",\"color\":\"YELLOW\",\"amount\":1},{\"type\":\"flag\",\"color\":\"GREEN\",\"amount\":1}],\"special_abilities\":[{\"type\":\"discount\",\"resource\":\"servant\",\"amount\":1}]},{\"id\":2,\"name\":\"aaa\",\"points\":2,\"requirements\":[{\"type\":\"flag\",\"color\":\"BLUE\",\"amount\":1},{\"type\":\"flag\",\"color\":\"PURPLE\",\"amount\":1}],\"special_abilities\":[{\"type\":\"discount\",\"resource\":\"shield\",\"amount\":1}]},{\"id\":3,\"name\":\"aaa\",\"points\":2,\"requirements\":[{\"type\":\"flag\",\"color\":\"GREEN\",\"amount\":1},{\"type\":\"flag\",\"color\":\"BLUE\",\"amount\":1}],\"special_abilities\":[{\"type\":\"discount\",\"resource\":\"stone\",\"amount\":1}]},{\"id\":4,\"name\":\"aaa\",\"points\":2,\"requirements\":[{\"type\":\"flag\",\"color\":\"YELLOW\",\"amount\":1},{\"type\":\"flag\",\"color\":\"PURPLE\",\"amount\":1}],\"special_abilities\":[{\"type\":\"discount\",\"resource\":\"gold\",\"amount\":1}]},{\"id\":5,\"name\":\"bbb\",\"points\":3,\"requirements\":[{\"type\":\"resource\",\"resource\":\"gold\",\"amount\":5}],\"special_abilities\":[{\"type\":\"storage\",\"storage_name\":\"leader_5\",\"accepted_types\":\"stone\",\"amount\":2}]},{\"id\":6,\"name\":\"bbb\",\"points\":3,\"requirements\":[{\"type\":\"resource\",\"resource\":\"stone\",\"amount\":5}],\"special_abilities\":[{\"type\":\"storage\",\"storage_name\":\"leader_6\",\"accepted_types\":\"servant\",\"amount\":2}]},{\"id\":7,\"name\":\"bbb\",\"points\":3,\"requirements\":[{\"type\":\"resource\",\"resource\":\"servant\",\"amount\":5}],\"special_abilities\":[{\"type\":\"storage\",\"storage_name\":\"leader_7\",\"accepted_types\":\"shield\",\"amount\":2}]},{\"id\":8,\"name\":\"bbb\",\"points\":3,\"requirements\":[{\"type\":\"resource\",\"resource\":\"shield\",\"amount\":5}],\"special_abilities\":[{\"type\":\"storage\",\"storage_name\":\"leader_8\",\"accepted_types\":\"gold\",\"amount\":2}]},{\"id\":9,\"name\":\"ccc\",\"points\":5,\"requirements\":[{\"type\":\"flag\",\"color\":\"YELLOW\",\"amount\":2},{\"type\":\"flag\",\"color\":\"BLUE\",\"amount\":1}],\"special_abilities\":[{\"type\":\"conversion\",\"from\":\"WHITE\",\"to\":[\"servant\"],\"faith_output\":0}]},{\"id\":10,\"name\":\"ccc\",\"points\":5,\"requirements\":[{\"type\":\"flag\",\"color\":\"GREEN\",\"amount\":2},{\"type\":\"flag\",\"color\":\"PURPLE\",\"amount\":1}],\"special_abilities\":[{\"type\":\"conversion\",\"from\":\"WHITE\",\"to\":[\"shield\"],\"faith_output\":0}]},{\"id\":11,\"name\":\"ccc\",\"points\":5,\"requirements\":[{\"type\":\"flag\",\"color\":\"BLUE\",\"amount\":2},{\"type\":\"flag\",\"color\":\"YELLOW\",\"amount\":1}],\"special_abilities\":[{\"type\":\"conversion\",\"from\":\"WHITE\",\"to\":[\"stone\"],\"faith_output\":0}]},{\"id\":12,\"name\":\"ccc\",\"points\":5,\"requirements\":[{\"type\":\"flag\",\"color\":\"PURPLE\",\"amount\":2},{\"type\":\"flag\",\"color\":\"GREEN\",\"amount\":1}],\"special_abilities\":[{\"type\":\"conversion\",\"from\":\"WHITE\",\"to\":[\"gold\"],\"faith_output\":0}]},{\"id\":13,\"name\":\"ddd\",\"points\":4,\"requirements\":[{\"type\":\"level_flag\",\"color\":\"YELLOW\",\"level\":2,\"amount\":1}],\"special_abilities\":[{\"type\":\"crafting\",\"crafting\":{\"input\":{\"shield\":1},\"output\":{\"any\":1},\"faith_output\":1}}]},{\"id\":14,\"name\":\"ddd\",\"points\":4,\"requirements\":[{\"type\":\"level_flag\",\"color\":\"BLUE\",\"level\":2,\"amount\":1}],\"special_abilities\":[{\"type\":\"crafting\",\"crafting\":{\"input\":{\"servant\":1},\"output\":{\"any\":1},\"faith_output\":1}}]},{\"id\":15,\"name\":\"ddd\",\"points\":4,\"requirements\":[{\"type\":\"level_flag\",\"color\":\"PURPLE\",\"level\":2,\"amount\":1}],\"special_abilities\":[{\"type\":\"crafting\",\"crafting\":{\"input\":{\"stone\":1},\"output\":{\"any\":1},\"faith_output\":1}}]},{\"id\":16,\"name\":\"ddd\",\"points\":4,\"requirements\":[{\"type\":\"level_flag\",\"color\":\"GREEN\",\"level\":2,\"amount\":1}],\"special_abilities\":[{\"type\":\"crafting\",\"crafting\":{\"input\":{\"gold\":1},\"output\":{\"any\":1},\"faith_output\":1}}]}]}";
        String crafting = "{\"shop\":[{\"id\":1,\"color\":\"GREEN\",\"level\":1,\"points\":1,\"cost\":{\"shield\":2},\"crafting\":{\"input\":{\"stone\":1},\"output\":{},\"faith_output\":1}},{\"id\":2,\"color\":\"PURPLE\",\"level\":1,\"points\":1,\"cost\":{\"servant\":2},\"crafting\":{\"input\":{\"stone\":1},\"output\":{},\"faith_output\":1}},{\"id\":3,\"color\":\"BLUE\",\"level\":1,\"points\":1,\"cost\":{\"gold\":2},\"crafting\":{\"input\":{\"shield\":1},\"output\":{},\"faith_output\":1}},{\"id\":4,\"color\":\"YELLOW\",\"level\":1,\"points\":1,\"cost\":{\"stone\":2},\"crafting\":{\"input\":{\"servant\":1},\"output\":{},\"faith_output\":1}},{\"id\":17,\"color\":\"GREEN\",\"level\":2,\"points\":5,\"cost\":{\"shield\":4},\"crafting\":{\"input\":{\"stone\":1},\"output\":{},\"faith_output\":2}},{\"id\":18,\"color\":\"PURPLE\",\"level\":2,\"points\":5,\"cost\":{\"servant\":4},\"crafting\":{\"input\":{\"gold\":1},\"output\":{},\"faith_output\":2}},{\"id\":19,\"color\":\"BLUE\",\"level\":2,\"points\":5,\"cost\":{\"gold\":4},\"crafting\":{\"input\":{\"servant\":1},\"output\":{},\"faith_output\":2}},{\"id\":20,\"color\":\"YELLOW\",\"level\":2,\"points\":5,\"cost\":{\"stone\":4},\"crafting\":{\"input\":{\"shield\":1},\"output\":{},\"faith_output\":2}},{\"id\":33,\"color\":\"GREEN\",\"level\":3,\"points\":9,\"cost\":{\"shield\":6},\"crafting\":{\"input\":{\"gold\":2},\"output\":{\"stone\":3},\"faith_output\":2}},{\"id\":34,\"color\":\"PURPLE\",\"level\":3,\"points\":9,\"cost\":{\"servant\":6},\"crafting\":{\"input\":{\"stone\":2},\"output\":{\"gold\":3},\"faith_output\":2}},{\"id\":35,\"color\":\"BLUE\",\"level\":3,\"points\":9,\"cost\":{\"gold\":6},\"crafting\":{\"input\":{\"servant\":2},\"output\":{\"shield\":3},\"faith_output\":2}},{\"id\":36,\"color\":\"YELLOW\",\"level\":3,\"points\":9,\"cost\":{\"stone\":6},\"crafting\":{\"input\":{\"shield\":2},\"output\":{\"servant\":3},\"faith_output\":2}}],\"base\":[{\"input\":{\"any\":2},\"output\":{\"any\":1},\"faith_output\":0}]}";
        String faith = "{\"tiles\":[{\"order\":0,\"x\":1,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":1,\"x\":2,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":2,\"x\":3,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":3,\"x\":3,\"y\":2,\"points\":1,\"pope_group\":0,\"pope_check\":false},{\"order\":4,\"x\":3,\"y\":3,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":5,\"x\":4,\"y\":3,\"points\":0,\"pope_group\":1,\"pope_check\":false},{\"order\":6,\"x\":5,\"y\":3,\"points\":1,\"pope_group\":1,\"pope_check\":false},{\"order\":7,\"x\":6,\"y\":3,\"points\":0,\"pope_group\":1,\"pope_check\":false},{\"order\":8,\"x\":7,\"y\":3,\"points\":0,\"pope_group\":1,\"pope_check\":true},{\"order\":9,\"x\":8,\"y\":3,\"points\":2,\"pope_group\":0,\"pope_check\":false},{\"order\":10,\"x\":8,\"y\":2,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":11,\"x\":8,\"y\":1,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":12,\"x\":9,\"y\":1,\"points\":2,\"pope_group\":2,\"pope_check\":false},{\"order\":13,\"x\":10,\"y\":1,\"points\":0,\"pope_group\":2,\"pope_check\":false},{\"order\":14,\"x\":11,\"y\":1,\"points\":0,\"pope_group\":2,\"pope_check\":false},{\"order\":15,\"x\":12,\"y\":1,\"points\":3,\"pope_group\":2,\"pope_check\":false},{\"order\":16,\"x\":13,\"y\":1,\"points\":0,\"pope_group\":2,\"pope_check\":true},{\"order\":17,\"x\":13,\"y\":2,\"points\":0,\"pope_group\":0,\"pope_check\":false},{\"order\":18,\"x\":13,\"y\":3,\"points\":3,\"pope_group\":0,\"pope_check\":false},{\"order\":19,\"x\":14,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":20,\"x\":15,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":21,\"x\":16,\"y\":3,\"points\":4,\"pope_group\":3,\"pope_check\":false},{\"order\":22,\"x\":17,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":23,\"x\":18,\"y\":3,\"points\":0,\"pope_group\":3,\"pope_check\":false},{\"order\":24,\"x\":19,\"y\":3,\"points\":4,\"pope_group\":3,\"pope_check\":true}],\"groups\":[{\"group\":1,\"points\":2},{\"group\":2,\"points\":3},{\"group\":3,\"points\":4}]}";

        gameModel = ServerBuilder.buildModel(config, crafting, faith, leaders, usernames,true, new Random(3));
        gameContext = new GameContext(gameModel, true);
        actionQueue = new ActionQueue();

        stateMachine = new StateMachine(actionQueue, gameContext, new MenuState(gameContext));
        gameModel.getFaithPath().setListener(stateMachine);
        for(Token t : gameModel.getLorenzoTokens())
            t.setListener(stateMachine);

        gameContext.setCurrentPlayer(gameModel.getPlayerById("Ernestino"));
        gameContext.setPlayerMoved(true);
    }

    @Test
    public void tokenRemovesCards() throws InterruptedException {
        gameModel.setTopToken(new DiscardToken(FlagColor.GREEN, 2));
        stateMachine.executeAction(new ConfirmAction("Ernestino"));
        Action action = actionQueue.pop();
        stateMachine.executeAction(action);
        Action action1 = actionQueue.pop();

        assertTrue(action1 instanceof NextTurnAction);
        assertThrows(NoSuchElementException.class, ()-> gameModel.getShop().getTopCard(1, FlagColor.GREEN));
        assertThrows(NoSuchElementException.class, ()-> gameModel.getShop().getTopCard(2, FlagColor.GREEN));
    }

    @Test
    public void tokenTriggersEndGame() throws InterruptedException {
        gameModel.setTopToken(new DiscardToken(FlagColor.GREEN, 2));
        stateMachine.executeAction(new ConfirmAction("Ernestino"));
        Action action = actionQueue.pop();
        stateMachine.executeAction(action);
        Action action1 = actionQueue.pop();
        stateMachine.executeAction(action1);

        assertTrue(stateMachine.getCurrentState() instanceof MenuState);

        gameContext.setPlayerMoved(true);
        Token t = new DiscardToken(FlagColor.GREEN, 2);
        t.setListener(stateMachine);
        gameModel.setTopToken(t);
        stateMachine.executeAction(new ConfirmAction("Ernestino"));
        Action action2 = actionQueue.pop();

        assertTrue(action2 instanceof LorenzoMoveAction);

        stateMachine.executeAction(action2);
        Action action3 = actionQueue.pop();

        assertTrue(action3 instanceof EndGameAction);

    }

}
