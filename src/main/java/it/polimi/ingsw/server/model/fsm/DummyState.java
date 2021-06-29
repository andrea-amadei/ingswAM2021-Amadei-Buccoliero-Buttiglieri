package it.polimi.ingsw.server.model.fsm;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.server.model.actions.ActivateLeaderAction;

import java.util.Collections;
import java.util.List;

public class DummyState extends State{

    public DummyState(GameContext gameContext) {
        super(gameContext);
    }

    @Override
    public List<Message> handleAction(ActivateLeaderAction activateLeaderAction) {
        List<Message> messages = Collections.singletonList(new Message(Collections.singletonList("Bob"), Collections.singletonList(new InfoPayloadComponent("Hi"))));
        setNextState(new DummyState(getGameContext()));
        return messages;
    }

}
