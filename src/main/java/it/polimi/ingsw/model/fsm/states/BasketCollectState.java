package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.common.payload_components.groups.PossibleActionsPayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.actions.ConfirmAction;
import it.polimi.ingsw.model.actions.MoveFromBasketToShelfAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.*;
import java.util.stream.Collectors;

public class BasketCollectState extends State {

    private boolean alreadyVisited = false;
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public BasketCollectState(GameContext gameContext) {
        super(gameContext);
    }


    /**
     * The player moves some resources from the basket to the deposits.
     * After the execution, the state doesn't change.
     * @param moveFromBasketToShelfAction the action to be performed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if moveFromBasketToShelfAction is null
     * @throws FSMTransitionFailedException if the action cannot be performed
     */
    @Override
    public List<Message> handleAction(MoveFromBasketToShelfAction moveFromBasketToShelfAction) throws FSMTransitionFailedException {
        if(moveFromBasketToShelfAction == null)
            throw new NullPointerException();

        List<Message> messages;

        try {
            messages = moveFromBasketToShelfAction.execute(getGameContext());
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        setNextState(this);

        return messages;
    }

    /**
     * The player confirms that he/she doesn't want to collect more resources from the marketBasket. All remaining
     * resources are discarded and the other players get a faith point for each discarded resources.
     * Note: PopeCheck interrupt might happen here.
     * New state is MenuState
     * @param confirmAction the action to be performed
     * @return the list of messages that need to be sent to the clients
     * @throws NullPointerException if confirmAction is null
     * @throws FSMTransitionFailedException if the action cannot be performed
     */
    @Override
    public List<Message> handleAction(ConfirmAction confirmAction) throws FSMTransitionFailedException {
        if(confirmAction == null)
            throw new NullPointerException();

        List<Message> messages;

        try {
            messages = new ArrayList<>(confirmAction.execute(getGameContext()));
        }catch(IllegalActionException e){
            throw new FSMTransitionFailedException(e.getMessage());
        }

        Player currentPlayer = getGameContext().getCurrentPlayer();
        List<Player> otherPlayers;
        GameModel model = getGameContext().getGameModel();
        FaithPath faithPath = model.getFaithPath();


        otherPlayers = model.getPlayers().stream()
                .filter(x->!x.getUsername().equals(currentPlayer.getUsername()))
                .collect(Collectors.toList());

        List<PayloadComponent> payload = new ArrayList<>();

        int droppedResources = currentPlayer.getBoard().getStorage().getMarketBasket().totalAmountOfResources();

        if(droppedResources > 0) {
            //Computing the delta resources
            Map<String, Integer> deltaResources = currentPlayer.getBoard().getStorage().getMarketBasket()
                    .getAllResources().entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> e.getKey().getId().toLowerCase(), e -> -e.getValue()));

            //Add the delta resources to the payload
            payload.add(PayloadFactory.changeResources(currentPlayer.getUsername(),
                    new RawStorage(currentPlayer.getBoard().getStorage().getMarketBasket().getId(), deltaResources)));

            currentPlayer.getBoard().getStorage().getMarketBasket().reset();

            //adding the amount of faith to the other players
            for (Player p : otherPlayers)
                payload.addAll(faithPath.executeMovement(droppedResources, p));

            //adding the amount of faith to Lorenzo
            payload.addAll(faithPath.executeLorenzoMovement(droppedResources));
        }

        //build the message
        List<String> targets = model.getPlayers().stream().map(Player::getUsername).collect(Collectors.toList());

        messages.add(new Message(targets, payload));
        setNextState(new MenuState(getGameContext()));

        return messages;
    }

    /**
     * This method will be executed every time this state is entered from a different state.
     * It informs the current player of the possible actions to be performed
     * @return the list of messages to be sent to the client
     */
    @Override
    public List<Message> onEntry() {
        List<Message> messages = super.onEntry();

        List<PayloadComponent> payload = new ArrayList<>();

        if(!alreadyVisited){
            payload.add(new InfoPayloadComponent("Collect resources from the basket. When you are ready confirm"));
            alreadyVisited = true;
        }

        payload.add(new PossibleActionsPayloadComponent(
            new HashSet<>(){{
                add(PossibleActions.MOVE_FROM_BASKET_TO_SHELF);
                add(PossibleActions.CONFIRM);
            }}
        ));
        messages.add(new Message(Collections.singletonList(getGameContext().getCurrentPlayer().getUsername()), payload));

        return messages;
    }

    /**
     * Returns a string representation of the object.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "BasketCollectState";
    }
}
