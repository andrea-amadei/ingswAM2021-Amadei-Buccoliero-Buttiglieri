package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.model.actions.BuyFromMarketAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;

import java.util.Collections;
import java.util.List;

public class MarketState extends State {
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public MarketState(GameContext gameContext) {
        super(gameContext);
    }

    /**
     * The player selects the desired row/col in the market.
     * If the action can be performed, this method returns the list of messages that need to be sent to the client
     * and sets the moved property to true in the game context. The new state will be ConversionSelectionState
     * @param buyFromMarketAction the action to be executed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if buyFromMarketAction is null
     * @throws FSMTransitionFailedException if the action cannot be executed
     */
    @Override
    public List<Message> handleAction(BuyFromMarketAction buyFromMarketAction) throws FSMTransitionFailedException {
        if(buyFromMarketAction == null)
            throw new NullPointerException();
        //TODO: do we really need to check here if the action target is the current player? We may want to check that
        //      directly in the action

        List<Message> messages;
        try{
            messages = buyFromMarketAction.execute(getGameContext());
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        getGameContext().setPlayerMoved(true);

        //Just to show how to append a new message to send to the user.
        //TODO: delete this
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()),
                Collections.singletonList(new InfoPayload("Hi!"))));

        //setting the new state
        setNextState(new ConversionSelectionState(getGameContext()));
        return messages;
    }


    /**
     * The player wants to go back in the menu state.
     * If the action can be performed, this method returns the list of messages that need to be sent to the client
     * and sets the moved property remains false in the game context. The new state will be MenuState
     * @param backAction the action to be executed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if backAction is null
     * @throws FSMTransitionFailedException if the action cannot be executed
     */
    @Override
    public List<Message> handleAction(BackAction backAction) throws FSMTransitionFailedException {
        if(backAction == null)
            throw new NullPointerException();
        List<Message> messages;
        try{
            messages = backAction.execute(getGameContext());
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(new MenuState(getGameContext()));
        return messages;
    }

    /**
     * This method will be executed every time this state is entered from a different state.
     * Informs the current player of the possible actions to be performed
     * @return the list of messages to be sent to the client
     */
    @Override
    public List<Message> onEntry() {
        List<Message> messages = super.onEntry();
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()),
                Collections.singletonList(new InfoPayload("Possible Actions: Back, BuyFromMarket"))));

        return messages;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "MarketState";
    }
}
