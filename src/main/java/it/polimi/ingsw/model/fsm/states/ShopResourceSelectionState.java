package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.actions.BackAction;
import it.polimi.ingsw.model.actions.BuyFromShopAction;
import it.polimi.ingsw.model.actions.SelectResourcesAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;

import java.util.Collections;
import java.util.List;

public class ShopResourceSelectionState extends State {
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public ShopResourceSelectionState(GameContext gameContext) {
        super(gameContext);
    }

    /**
     * The player returns to the shop state.
     * Moved property is not yet set to true. Any previous election of a card or resources is undone.
     * Next state is ShopState.
     * @param backAction the action to execute
     * @return the list of messages to send to the clients
     * @throws NullPointerException iff the pointer to backAction is null
     * @throws FSMTransitionFailedException iff the action cannot be executed
     */
    @Override
    public List<Message> handleAction(BackAction backAction)throws FSMTransitionFailedException {
        if (backAction == null)
            throw new NullPointerException();
        List<Message> messages;

        getGameContext().getGameModel().getShop().resetSelectedCard();
        getGameContext().getCurrentPlayer().getBoard().getStorage().resetSelection();

        try{
            messages = backAction.execute(getGameContext());
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }
        setNextState(new ShopState(getGameContext()));
        return messages;
    }

    /**
     * The player selects the resources they want to use to buy the selected card.
     * Moved property is not yet set to true. Next state is ShopResourceSelectionState.
     * @param selectResourcesAction the action to execute
     * @return the list of messages to send to the clients
     * @throws NullPointerException iff the pointer to selectResourcesAction is null
     * @throws FSMTransitionFailedException iff the action cannot be executed
     */
    @Override
    public List<Message> handleAction(SelectResourcesAction selectResourcesAction) throws FSMTransitionFailedException{
        if(selectResourcesAction == null)
            throw new NullPointerException();
        List<Message> messages;

        try{
            messages = selectResourcesAction.execute(getGameContext());
        }catch (IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }
        setNextState(new ShopResourceSelectionState(getGameContext()));
        return messages;
    }

    /**
     * The player uses the previously selected resources to buy the card.
     * Moved property is now set to true. Next state is MenuState.
     * @param buyFromShopAction the action to execute
     * @return the list of messages to send to the clients
     * @throws NullPointerException iff the pointer to buyFromShopAction is null
     * @throws FSMTransitionFailedException iff the action cannot be executed
     */
    @Override
    public List<Message> handleAction(BuyFromShopAction buyFromShopAction) throws  FSMTransitionFailedException{
        if(buyFromShopAction == null)
            throw new NullPointerException();
        List<Message> messages;

        try{
            messages = buyFromShopAction.execute(getGameContext());
        }catch (IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        getGameContext().setPlayerMoved(true);
        getGameContext().getGameModel().getShop().resetSelectedCard();
        getGameContext().getCurrentPlayer().getBoard().getStorage().resetSelection();

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
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()),
                Collections.singletonList(new InfoPayload("Possible Actions: Back, SelectResources, BuyFromShop"))));

        return messages;
    }

    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "ShopResourceSelectionState";
    }
}
