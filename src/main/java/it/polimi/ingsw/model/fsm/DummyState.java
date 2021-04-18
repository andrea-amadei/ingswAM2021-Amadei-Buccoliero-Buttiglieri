package it.polimi.ingsw.model.fsm;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.model.actions.ActivateLeaderAction;

import java.util.Collections;
import java.util.List;

public class DummyState extends State{

    public DummyState(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    public List<Message> handleAction(ActivateLeaderAction activateLeaderAction) {
        List<Message> messages = Collections.singletonList(new Message(Collections.singletonList("Bob"), Collections.singletonList(new InfoPayload("Hi"))));
        setNextState(new DummyState(getGameContext()));
        return messages;
    }

}
