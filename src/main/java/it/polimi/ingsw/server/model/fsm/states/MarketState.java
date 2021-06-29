package it.polimi.ingsw.server.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.actions.BackAction;
import it.polimi.ingsw.server.model.actions.BuyFromMarketAction;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.fsm.State;
import it.polimi.ingsw.common.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Extends State and represents a concrete state of the state machine.
 * In this state the player has already chosen their move for the turn will be a visit to the market.
 * They can go back to the Menu and chose another path, or select from the market grid the row or column
 * they intend to buy.
 */
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

        List<Message> messages;
        try{
            messages = buyFromMarketAction.execute(getGameContext());
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        getGameContext().setPlayerMoved(true);

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
        List<PayloadComponent> payload = new ArrayList<>();

        payload.add(new InfoPayloadComponent("You chose the Market: pick a row or a column"));
        payload.add(PayloadFactory.possibleActions(
                new HashSet<>(){{
                    add(PossibleActions.BACK);
                    add(PossibleActions.BUY_FROM_MARKET);
                }}
        ));
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()),
                payload));

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
