package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.model.actions.SelectCardFromShopAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;

import java.util.Collections;
import java.util.List;

public class ShopState extends State {
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public ShopState(GameContext gameContext) {
        super(gameContext);
    }

    /**
     * the player selects the card they intend to buy.
     * Moved property is not yet set to true. Next state is ShopResourceSelectionState.
     * @param selectCardFromShopAction the action to execute
     * @return the list of massages to send to the clients
     * @throws NullPointerException iff the pointer to selectCardFromShop is null
     * @throws FSMTransitionFailedException iff action cannot be executed
     */
    @Override
    public List<Message> handleAction(SelectCardFromShopAction selectCardFromShopAction)throws FSMTransitionFailedException{
        if(selectCardFromShopAction == null)
            throw new NullPointerException();
        List<Message> messages;

        try{
            messages = selectCardFromShopAction.execute(getGameContext());
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }
        setNextState(new ShopResourceSelectionState(getGameContext()));
        return messages;
    }

    /**
     * The player returns to the menu state.
     * Moved property is not yet set to true. Next state is MenuState.
     * @param backAction the action to execute
     * @return the list of messages to send to the clients
     * @throws NullPointerException iff the pointer to backAction is null
     * @throws FSMTransitionFailedException iff the action cannot be executed
     */
    @Override
    public List<Message> handleAction(BackAction backAction)throws FSMTransitionFailedException{
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
     * This method will be executed every time this state is entered.
     * Informs the current player of the next available actions.
     * @return the list of messages to send to the client
     */
    @Override
    public List<Message> onEntry() {
        List<Message> messages = super.onEntry();
        /*
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()),
                Collections.singletonList(new InfoPayload("Possible Actions: Back, SelectCardFromShop"))));
         */

        //TODO: add the possible actions
        return messages;
    }

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "ShopState";
    }
}
