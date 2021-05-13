package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.InfoPayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.holder.ConversionHolder;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class BuyFromMarketAction implements Action{

    private final String player;
    private final boolean isRow;
    private final int index;

    /**
     * Creates this action
     * @param player the player that wants to buy from market
     * @param isRow true if the player wants to select a row. False if the player wants to select a column
     * @param index the index of the row/col
     * @throws NullPointerException if player is null
     * @throws IndexOutOfBoundsException if index is negative
     */
    public BuyFromMarketAction(String player, boolean isRow, int index){
        this.player = player;
        this.isRow = isRow;
        this.index = index;
        checkFormat();
    }


    /**
     * Calls the appropriate method of the handler
     *
     * @param handler the handler that will execute this action
     * @return the list of messages to send to the client
     * @throws NullPointerException         if handler is null
     * @throws FSMTransitionFailedException if the state fails to execute this action
     */
    @Override
    public List<Message> acceptHandler(ActionHandler handler) throws FSMTransitionFailedException {
        if(handler == null)
            throw new NullPointerException();
        return handler.handleAction(this);
    }

    /**
     * Executes the action on the provided game context
     *
     * @param gameContext the current context of the game
     * @return a list of messages that contains information about the change applied to the model
     * @throws IllegalActionException if the action cannot be performed on the model
     * @throws NullPointerException if the gameContext is null
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {
        if(gameContext == null)
            throw new NullPointerException();

        GameModel model = gameContext.getGameModel();
        Market market = model.getMarket();
        Player currentPlayer;

        if(!player.equals(gameContext.getCurrentPlayer().getUsername()))
            throw new IllegalActionException("It is not your turn");

        currentPlayer = gameContext.getCurrentPlayer();

        //try to execute the action
        try{
            if(isRow)
                market.pickRow(index);
            else
                market.pickCol(index);
        }catch(IndexOutOfBoundsException e){
            throw new IllegalActionException("The selected " + ((isRow) ? "row" : "col") + " index is out of bound.\nSelect another index");
        }

        //get the actuators to send to the currentPlayer
        List<Marble> selectedMarbles = market.getSelectedMarbles();
        ConversionHolder conversionHolder = currentPlayer.getBoard().getConversionHolder();
        List<List<ConversionActuator>> possibleConversions = new ArrayList<>();

        selectedMarbles.forEach(
                marble ->{
                    List<ConversionActuator> conversionActuators = conversionHolder.getActuatorsFromColor(marble.getColor());
                    if(conversionActuators.size() > 0)
                        possibleConversions.add(conversionActuators);
                    else
                        possibleConversions.add(Collections.singletonList(marble.getBaseConversionActuator()));
                }
        );

        List<Message> messages = new ArrayList<>();
        List<PayloadComponent> payload = new ArrayList<>();

        //Create the payload with the changes to the market
        PayloadComponent update = PayloadFactory.changeMarket(market.toRaw());

        //Create the payload with the possible conversions
        payload.add(PayloadFactory.changePossibleConversions(currentPlayer.getUsername(), selectedMarbles, possibleConversions));

        //Create the message to be sent to everyone
        List<String> to = model.getPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
        messages.add(new Message(to, Collections.singletonList(update)));

        //Create the message to be sent only to the current player
        messages.add(new Message(Collections.singletonList(player), payload));

        return messages;
    }

    /**
     * Returns the sender of this action
     *
     * @return the sender of this action
     */
    @Override
    public String getSender() {
        return player;
    }

    /**
     * Checks if all attributes are set and have meaningful values.
     * In case they are not, this throws the appropriate RuntimeException.
     * It needs to be used since this class can be created by deserialization
     */
    @Override
    public void checkFormat() {
        if(player == null)
            throw new NullPointerException();
        if(index < 0)
            throw new IndexOutOfBoundsException();
    }
}
