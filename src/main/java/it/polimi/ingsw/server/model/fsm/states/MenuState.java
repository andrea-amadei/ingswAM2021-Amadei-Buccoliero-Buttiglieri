package it.polimi.ingsw.server.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.actions.*;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.fsm.State;
import it.polimi.ingsw.common.utils.PayloadFactory;

import java.util.*;

/**
 * Extends State and represents a concrete state of the state machine.
 * In this state, if the player has not yet chosen what path they will be following for their turn (shop, market or crafting),
 * they can chose one. Otherwise, they can confirm and and their turn.
 * In both cases they can reorganize resources between their storages (this step is mandatory if they just entered the
 * state and somehow still have resources in their hand), discard or activate leader cards.
 */
public class MenuState extends State {

    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public MenuState(GameContext gameContext) {
        super(gameContext);
    }

    /**
     * The player tries to activate a leader card. The action will fail if the card cannot be activated or if it is
     * already activated.
     * If the action succeeds, next state is this state.
     *
     * @param activateLeaderAction the action to be executed
     * @return the list of messages to be sent to the client
     * @throws NullPointerException         if the action is null
     * @throws FSMTransitionFailedException if the action cannot be handled or failed
     */
    @Override
    public List<Message> handleAction(ActivateLeaderAction activateLeaderAction) throws FSMTransitionFailedException {
        if(activateLeaderAction == null)
            throw new NullPointerException();
        List<Message> messages;
        try{
            messages = new ArrayList<>(activateLeaderAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(this);

        return messages;
    }


    /**
     * The player confirms that the turn ended.
     * The transition is invalid if the player hasn't moved and if the player has no resources in hand.
     * If the transition is valid, next state is EndTurnState.
     *
     * @param confirmAction the action to be executed
     * @return the list of messages to be sent to the client
     * @throws NullPointerException         if the action is null
     * @throws FSMTransitionFailedException if the action cannot be handled or failed
     */
    @Override
    public List<Message> handleAction(ConfirmAction confirmAction) throws FSMTransitionFailedException {
        if(confirmAction == null)
            throw new NullPointerException();

        //check if player has moved
        if(!getGameContext().hasPlayerMoved())
            throw new FSMTransitionFailedException("Cannot end turn if player has not moved.");

        //check if player has emptied their hand
        if(isHandNotEmpty())
            throw new FSMTransitionFailedException("Cannot end turn if player has not emptied their hand");

        List<Message> messages;
        try{
            messages = new ArrayList<>(confirmAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(new EndTurnState(getGameContext()));

        return messages;
    }

    /**
     * The player tidies the storage.
     * If the action can be performed, next state remains this state
     * @param resourcesMoveAction the action to be executed
     * @return the list of messages to be sent to the client
     * @throws NullPointerException         if the action is null
     * @throws FSMTransitionFailedException if the action cannot be handled or failed
     */
    @Override
    public List<Message> handleAction(ResourcesMoveAction resourcesMoveAction) throws FSMTransitionFailedException {
        if(resourcesMoveAction == null)
            throw new NullPointerException();

        List<Message> messages;
        try{
            messages = new ArrayList<>(resourcesMoveAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(this);

        return messages;
    }

    /**
     * The player selects the next state.
     * The transition is valid only if the player hasn't already moved and if the hand is empty
     * If the action can be performed, next state is the state specified by the action
     * @param selectPlayAction the action to be executed
     * @return the list of messages to be sent to the client
     * @throws NullPointerException         if the action is null
     * @throws FSMTransitionFailedException if the action cannot be handled or failed
     */
    @Override
    public List<Message> handleAction(SelectPlayAction selectPlayAction) throws FSMTransitionFailedException {
        if(selectPlayAction == null)
            throw new NullPointerException();

        List<Message> messages;
        try{
            messages = new ArrayList<>(selectPlayAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        if(getGameContext().hasPlayerMoved() || isHandNotEmpty())
            throw new FSMTransitionFailedException("Cannot proceed if another move has already been performed or if the" +
                    "hand is not empty");

        switch(selectPlayAction.getPlay()){
            case MARKET:
                setNextState(new MarketState(getGameContext()));
                break;
            case SHOP:
                setNextState(new ShopState(getGameContext()));
                break;
            case CRAFTING:
                setNextState(new CraftingState(getGameContext()));
                break;
        }

        return messages;

    }

    /**
     * The player discards a leader card and gets the faith points
     * If the action can be performed, next state remains this
     * @param discardLeaderAction the action to be executed
     * @return the list of messages to be sent to the client
     * @throws NullPointerException         if the action is null
     * @throws FSMTransitionFailedException if the action cannot be handled or failed
     */
    @Override
    public List<Message> handleAction(DiscardLeaderAction discardLeaderAction) throws FSMTransitionFailedException {
        if(discardLeaderAction == null)
            throw new NullPointerException();

        List<Message> messages;
        try{
            messages = new ArrayList<>(discardLeaderAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(this);

        return messages;

    }

    /**
     * This method will be executed every time this state is entered from a different state
     *
     * @return the list of messages to be sent to the client
     */
    @Override
    public List<Message> onEntry() {

        List<PayloadComponent> payload = new ArrayList<>();

        Set<PossibleActions> possibleActions = new HashSet<>();

        StringBuilder sb = new StringBuilder();
        sb.append("Available actions:\n");

        //the player can make their move
        if (!getGameContext().hasPlayerMoved() && getGameContext().getCurrentPlayer().getBoard().getStorage().getHand().getAllResources().isEmpty()) {
            sb.append("Craft resources, Buy a development card, Buy resources from market\n");
            possibleActions.add(PossibleActions.SELECT_PLAY);
        } else {
            sb.append("End turn\n");
            possibleActions.add(PossibleActions.CONFIRM);
        }

        //the player can activate or discard a leader
        if (getGameContext().getCurrentPlayer().getBoard().getLeaderCards().stream().anyMatch(c -> !c.isActive())) {
            sb.append("Activate a leader card, Discard a leader card\n");
            possibleActions.add(PossibleActions.DISCARD_LEADER);
            possibleActions.add(PossibleActions.ACTIVATE_LEADER);
        }

        //the player can move resources
        sb.append("Move resources");
        possibleActions.add(PossibleActions.RESOURCE_MOVE);


        payload.add(new InfoPayloadComponent(sb.toString()));

        payload.add(PayloadFactory.possibleActions(possibleActions));

        return Collections.singletonList(
                new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()), payload));

    }


    /**
     * return true if the player has resources in their hand, false otherwise.
     *
     * @return true if the player has resources in their hand, false otherwise.
     */
    private boolean isHandNotEmpty(){
        return getGameContext().getCurrentPlayer().getBoard().getStorage().getHand().totalAmountOfResources() != 0;
    }
}
