package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.InfoPayload;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.PayloadComponent;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameParameters;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.GameContext;

import java.util.*;
import java.util.stream.Collectors;

public class PreliminaryPickAction implements Action{

    private final String player;
    private final List<Integer> leadersToDiscard;
    private final Map<ResourceSingle, Integer> chosenResources;

    /**
     * PreliminaryPickAction constructor.
     * @param player the player performing the action
     * @param leadersToDiscard the list of to be discarded leaders' IDs
     * @param chosenResources the resources chosen by the player
     * @throws NullPointerException iff pointer to player, leadersToDiscard or chosenResources is null
     * @throws IllegalArgumentException iff IDs of leaders or amount of chosen resources are negative.
     */
    public PreliminaryPickAction(String player, List<Integer> leadersToDiscard, Map<ResourceSingle, Integer> chosenResources){
        if(player == null || leadersToDiscard == null || chosenResources == null)
            throw new NullPointerException();

        for(Integer i : leadersToDiscard)
            if(i<0)
                throw new IllegalArgumentException("IDs of leaders to discard must be between 0 and total amount of leaders");
        for(Map.Entry<ResourceSingle, Integer> entry : chosenResources.entrySet())
            if(entry.getValue()<0)
                throw new IllegalArgumentException("Amount of chosen resources cannot be negative");

        this.player = player;
        this.leadersToDiscard = leadersToDiscard;
        this.chosenResources = chosenResources;
    }

    /**
     * Action discards selected leaders, and potentially adds faith points and moves chosen resources to the player's hand.
     * @param gameContext the current context of the game
     * @return a list of messages that contains information about the change applied to the model
     * @throws IllegalActionException if the action cannot be performed on the model
     * @throws NullPointerException   if gameContext is null
     */
    @Override
    public List<Message> execute(GameContext gameContext) throws IllegalActionException {
        if(gameContext == null)
            throw new NullPointerException();

        GameModel model = gameContext.getGameModel();
        Player currentPlayer;
        List<String> destinations = model.getPlayers()
                .stream()
                .map(Player::getUsername)
                .collect(Collectors.toList());

        currentPlayer = gameContext.getCurrentPlayer();
        if(!currentPlayer.getUsername().equals(player))
            throw new IllegalActionException("Player must wait for their turn to perform this action");

        //checking if amount of leaders to discard is correct
        if(leadersToDiscard.size()!= GameParameters.AMOUNT_OF_LEADERS_TO_DISCARD)
            throw new IllegalActionException("Must discard correct amount of leaders");

        //discard leaders
        try {
            currentPlayer.getBoard().removeLeaderCardsByIndex(leadersToDiscard);
        }catch (IndexOutOfBoundsException e){
            throw new IllegalActionException(e.getMessage());
        }

        //assessing the player's order
        int playerOrder = 0;
        for(String playerID : gameContext.getGameModel().getPlayerNames()){
            if(playerID.equals(player))
                break;
            playerOrder++;
        }

        //checking the amount of resources to add is correct, according to the player's order
        int amountOfResources = 0;
        int faithPoints = 0;
        for(Map.Entry<ResourceSingle, Integer> entry : chosenResources.entrySet()){
            amountOfResources += entry.getValue();
        }

        switch (playerOrder){
            case 0: if(amountOfResources!=GameParameters.FIRST_PLAYER_AMOUNT_OF_RESOURCES_ON_START)
                        throw new IllegalActionException("First player must get the correct amount of resources on start");
                    faithPoints = GameParameters.FIRST_PLAYER_AMOUNT_OF_FAITH_POINTS_ON_START;
                    break;
            case 1: if(amountOfResources!=GameParameters.SECOND_PLAYER_AMOUNT_OF_RESOURCES_ON_START)
                        throw new IllegalActionException("Second player must get the correct amount of resources on start");
                    faithPoints = GameParameters.SECOND_PLAYER_AMOUNT_OF_FAITH_POINTS_ON_START;
                    break;
            case 2: if(amountOfResources!=GameParameters.THIRD_PLAYER_AMOUNT_OF_RESOURCES_ON_START)
                        throw new IllegalActionException("Third player must get the correct amount of resources on start");
                    faithPoints = GameParameters.THIRD_PLAYER_AMOUNT_OF_FAITH_POINTS_ON_START;
                    break;
            case 3: if(amountOfResources!=GameParameters.FOURTH_PLAYER_AMOUNT_OF_RESOURCES_ON_START)
                        throw new IllegalActionException("Fourth player must get the correct amount of resources on start");
                    faithPoints = GameParameters.FOURTH_PLAYER_AMOUNT_OF_FAITH_POINTS_ON_START;
        }

        //adding faith points according to the player's order
        if(faithPoints != 0)
            gameContext.getGameModel().getFaithPath().executeMovement(faithPoints, currentPlayer);

        //adding resources to the player's hand, according to the player's order
        for(Map.Entry<ResourceSingle, Integer> entry : chosenResources.entrySet()){
            currentPlayer.getBoard().getStorage().getHand().addResources(entry.getKey(), entry.getValue());
        }

        //returning the payload
        List<PayloadComponent> payloadComponents = new ArrayList<>();

        payloadComponents.add(new InfoPayload(player + " discarded leaders.")) ;
        for(Map.Entry<ResourceSingle, Integer> entry : chosenResources.entrySet()){
            payloadComponents.add(new InfoPayload(player + " added " + entry.getValue() + " " + entry.getKey() +
                    " resources to their hand."));
        }
        payloadComponents.add(new InfoPayload(player + " got " + faithPoints + " faith points."));
        return Collections.singletonList(new Message(destinations, payloadComponents));
    }
}
