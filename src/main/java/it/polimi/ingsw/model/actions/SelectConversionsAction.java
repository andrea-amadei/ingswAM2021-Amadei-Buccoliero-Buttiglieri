package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.exceptions.IllegalResourceTransferException;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.holder.ConversionHolder;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class implements interface Action. This action allows the player to select the conversions they want to apply
 * to the resources they just bought from the market.
 */
public class SelectConversionsAction implements Action{

    private final String player;
    private final List<Integer> actuatorsChoice;

    /**
     * Creates a new ConversionAction.
     * @param player the player that wants to select the conversions
     * @param actuatorsChoice the list of choices made by the player. In order to be executed, its length must
     *                        equal the length of the picked marbles.
     *                        Each element must be greater or equal to 0 and, in order to be executed, it must
     *                        be smaller then the number of possible conversions of the corresponding marble
     * @throws NullPointerException if either player or actuatorsChoice is null
     * @throws IllegalArgumentException if some values of actuatorsChoice are negative
     */
    public SelectConversionsAction(String player, List<Integer> actuatorsChoice){
        this.player = player;
        this.actuatorsChoice = actuatorsChoice;
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
     * The player chooses the conversions to be used. For each marble, the chosen ConversionActuator
     * adds the associated resources to the market basket and/or adds the associated faith points to the
     * player.
     *
     * @param gameContext the current context of the game
     * @return a list of messages that contains information about the change applied to the model
     * @throws IllegalActionException if the action cannot be performed on the model
     * @throws NullPointerException if gameContext is null
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

        ConversionHolder conversionHolder = currentPlayer.getBoard().getConversionHolder();
        FaithPath faithPath = model.getFaithPath();

        //check if the actuatorsChoice is valid
        List<Marble> selectedMarbles = market.getSelectedMarbles();
        if(actuatorsChoice.size() != selectedMarbles.size())
            throw new IllegalActionException("The sizes of actuatorsChoice and selectedMarble don't match");

        for(int i = 0; i < actuatorsChoice.size(); i++){
            List<ConversionActuator> playerConversionsPerMarble =
                    conversionHolder.getActuatorsFromColor(selectedMarbles.get(i).getColor());
            if(actuatorsChoice.get(i) < 0)
                throw new IllegalActionException("The choice cannot be negative (should have been already checked)");
            if(actuatorsChoice.get(i) > 0 && actuatorsChoice.get(i) >= playerConversionsPerMarble.size())
                throw new IllegalActionException("The choice index is too high");
        }

        //the selection is valid, we then proceed to apply all the conversions


        List<PayloadComponent> payload = new ArrayList<>();

        //empty the selection in the client model
        payload.add(PayloadFactory.changePossibleConversions(currentPlayer.getUsername(), new ArrayList<>(), new ArrayList<>()));

        //apply the selected conversions
        for(int i = 0; i < selectedMarbles.size(); i++){

            //apply the base conversion
            if(conversionHolder.getActuatorsFromColor(selectedMarbles.get(i).getColor()).size() == 0) {
                try {
                    payload.addAll(selectedMarbles.get(i).getBaseConversionActuator().actuateConversion(currentPlayer, faithPath));
                } catch (IllegalResourceTransferException e) {
                    Logger.log("Failed to choose the correct conversions, but others may have already been applied");
                    throw new IllegalActionException(e.getMessage());
                }
            }

            //apply the selected conversion
            else{
                try {
                    payload.addAll(conversionHolder.getActuatorsFromColor(selectedMarbles.get(i).getColor()).get(actuatorsChoice.get(i))
                            .actuateConversion(currentPlayer, faithPath));
                } catch (IllegalResourceTransferException e) {
                    Logger.log("Failed to choose the correct conversions, but others may have already been applied");
                    throw new IllegalActionException(e.getMessage());
                }
            }
        }

        Message message = new Message(gameContext.getGameModel().getPlayerNames(), payload);

        return Collections.singletonList(message);

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
        if(player == null || actuatorsChoice == null)
            throw new NullPointerException();
        if(actuatorsChoice.stream().anyMatch(x->x<0))
            throw new IllegalArgumentException("Actuator choices can't be negative");
    }

    /**
     * returns the actuator chosen by the player.
     * @return the actuator chosen by the player.
     */
    public List<Integer> getActuatorsChoice() {
        return actuatorsChoice;
    }
}
