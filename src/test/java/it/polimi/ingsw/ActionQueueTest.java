package it.polimi.ingsw;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.actions.BackAction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActionQueueTest {

    @Test
    public void createEmpty(){
        ActionQueue actionQueue = new ActionQueue();
        assertTrue(actionQueue.isEmpty());
    }

    @Test
    public void correctOrderWithDifferentPriorities(){
        ActionQueue actionQueue = new ActionQueue();
        Action action1 = new BackAction("A");
        Action action2 = new BackAction("B");
        Action action3 = new BackAction("C");

        actionQueue.addAction(action3, 2);
        actionQueue.addAction(action2, 1);
        actionQueue.addAction(action1, 0);

        assertEquals(action1, actionQueue.pop());
        assertEquals(action2, actionQueue.pop());
        assertEquals(action3, actionQueue.pop());
    }

    @Test
    public void correctOrderWithSamePriorities(){
        ActionQueue actionQueue = new ActionQueue();
        Action action1 = new BackAction("A");
        Action action2 = new BackAction("B");
        Action action3 = new BackAction("C");

        actionQueue.addAction(action3, 0);
        actionQueue.addAction(action2, 0);
        actionQueue.addAction(action1, 0);

        assertEquals(action3, actionQueue.pop());
        assertEquals(action2, actionQueue.pop());
        assertEquals(action1, actionQueue.pop());
    }
}
