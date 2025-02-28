package it.polimi.ingsw.server.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.common.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.faithpath.FaithPath;
import it.polimi.ingsw.server.model.faithpath.FaithPathGroup;
import it.polimi.ingsw.server.model.faithpath.FaithPathTile;
import it.polimi.ingsw.server.model.global.Player;
import it.polimi.ingsw.server.model.fsm.ActionHandler;
import it.polimi.ingsw.server.model.fsm.GameContext;
import it.polimi.ingsw.server.model.holder.FaithHolder;
import it.polimi.ingsw.common.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class implements interface Action. This action manages the triggering of a pope check and gives players
 * the correct amount of faith points depending on their position on the faith path.
 */
public class PopeCheckAction implements Action{

    private final List<Integer> newPopeCheckOrders;

    public PopeCheckAction(List<Integer> newPopeCheckOrders){
        this.newPopeCheckOrders = newPopeCheckOrders;
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
    * For each new popeCheck, for each player, if the player is not in the same group of the popeCheck tile and
    * has not yet passed that checkpoint, the corresponding checkpoint card is discarded. Otherwise the card is
    * activated and the player gets the corresponding victory points.
    * Lastly, the new popeChecks are added to the list of reached popeChecks
    * @param gameContext the current context of the game
    * @return a list of messages that contains information about the change applied to the model
    * @throws IllegalActionException if the action cannot be performed on the model
    * @throws NullPointerException   if gameContext is null
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {
        if(gameContext == null)
            throw new NullPointerException();

        List<PayloadComponent> payloadComponents = new ArrayList<>();

        FaithPath faithPath = gameContext.getGameModel().getFaithPath();
        List<Integer> alreadyTriggeredPopeCheckOrders = faithPath.getAlreadyTriggeredPopeChecks();
        List<FaithPathTile> tiles = faithPath.getTiles();

        //for each new popeCheck, we find its order in the track, its index and its group
        for(int i = 0; i < newPopeCheckOrders.size(); i++){
            int popeTileOrder = newPopeCheckOrders.get(i);
            int checkpointIndex = i + alreadyTriggeredPopeCheckOrders.size();
            int checkpointGroup = tiles.get(popeTileOrder).getPopeGroup();
            int checkPointVictoryPoints = faithPath.getFaithGroupList()
                                                   .stream()
                                                   .filter(g -> g.getGroup() == (checkpointGroup))
                                                   .map(FaithPathGroup::getPoints)
                                                   .findAny()
                                                   .orElse(0);
            //for each player, we check if it needs to activate the popeCard. In this case, victory points are added.
            //If the player needs to discard its popeCard, so it is done.
            for(Player p : gameContext.getGameModel().getPlayers()){
                int playerFaith = p.getBoard().getFaithHolder().getFaithPoints();
                int playerGroup = tiles.get(playerFaith).getPopeGroup();
                if(playerGroup == checkpointGroup || playerFaith > popeTileOrder){
                    p.getBoard().getFaithHolder().setPopeCardActive(checkpointIndex);
                    payloadComponents.add(
                            PayloadFactory.changePopeCard(p.getUsername(), FaithHolder.CheckpointStatus.ACTIVE, checkpointIndex));

                    p.addPoints(checkPointVictoryPoints);
                    payloadComponents.add(PayloadFactory.addPoints(p.getUsername(), checkPointVictoryPoints));
                }else{
                    p.getBoard().getFaithHolder().setPopeCardInactive(checkpointIndex);
                    payloadComponents.add(
                            PayloadFactory.changePopeCard(p.getUsername(), FaithHolder.CheckpointStatus.INACTIVE, checkpointIndex));
                }
            }
        }

        //add new popeChecks to the list of triggered popeChecks
        for(int order : newPopeCheckOrders)
            faithPath.addTriggeredPopeCheckOrder(order);

        //if this is the last pope check
        if(newPopeCheckOrders.get(newPopeCheckOrders.size()-1).equals(tiles.size()-1)){
            //if Lorenzo reached the end, set the flag associated to the winning status of Lorenzo
            if(gameContext.getGameModel().getFaithPath().getLorenzoFaith() == tiles.get(tiles.size()-1).getOrder()){
                gameContext.setLorenzoWon(true);
            }
            if(gameContext.isSinglePlayer()){
                gameContext.setHardEnd();
            }else{
                gameContext.startCountdown();
            }
        }

        //send the message to everyone
        List<String> destinations = gameContext.getGameModel().getPlayerNames();
        return Collections.singletonList(new Message(destinations, payloadComponents));
    }

    /**
     * Returns the sender of this action
     *
     * @return the sender of this action
     */
    @Override
    public String getSender() {
        return "AI";
    }

    /**
     * Checks if all attributes are set and have meaningful values.
     * In case they are not, this throws the appropriate RuntimeException.
     * It needs to be used since this class can be created by deserialization
     */
    @Override
    public void checkFormat() {

    }
}
