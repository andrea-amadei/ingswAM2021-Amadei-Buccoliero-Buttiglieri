package it.polimi.ingsw;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.fsm.StateMachine;
import it.polimi.ingsw.model.holder.DiscountHolder;
import it.polimi.ingsw.server.DummyBuilder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DummyBuilderTest {

    @Test
    public void modelCreation(){
        GameModel model = DummyBuilder.buildDummyModel(Arrays.asList("Alice", "Bob", "Carlo"), new Random());
        assertNotNull(model);
        assertEquals(Arrays.asList("Alice", "Bob", "Carlo"), model.getPlayerNames());
    }

    @Test
    public void controllerCreation(){
        StateMachine stateMachine = DummyBuilder.buildController(
                Arrays.asList("Alice", "Bob", "Carlo"),
                new Random(),
                false,
                new ActionQueue()
        );

        assertNotNull(stateMachine);
    }
}
