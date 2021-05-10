package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameParameters;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.ActionHandler;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.utils.PayloadFactory;

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
        this.player = player;
        this.leadersToDiscard = leadersToDiscard;
        this.chosenResources = chosenResources;
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


        currentPlayer = gameContext.getCurrentPlayer();
        if(!currentPlayer.getUsername().equals(player))
            throw new IllegalActionException("Player must wait for their turn to perform this action");

        //checking if amount of leaders to discard is correct
        if(leadersToDiscard.size()!= GameParameters.AMOUNT_OF_LEADERS_TO_DISCARD)
            throw new IllegalActionException("Must discard correct amount of leaders");

        //assessing the player is not discarding the same leader twice
        Set<Integer> checkUniqueSet = new HashSet<>();
        boolean canContinue = true;
        for(Integer i : leadersToDiscard){
            canContinue = checkUniqueSet.add(i);
        }
        if(!canContinue)
            throw new IllegalActionException("Cannot discard the same leader twice");

        //get the list of leader id to discard (useful for conformity with the PayloadComponent)
        List<Integer> leaderIdToDiscard;
        try {
            leaderIdToDiscard = leadersToDiscard
                    .stream()
                    .map(l -> currentPlayer.getBoard().getLeaderCards().get(l).getId())
                    .collect(Collectors.toList());
        }catch(IndexOutOfBoundsException e){
            throw new IllegalActionException("Selected an invalid card index");
        }
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

        List<PayloadComponent> globalChanges = new ArrayList<>();
        List<PayloadComponent> secretChanges = new ArrayList<>();
        List<PayloadComponent> coveredCardChanges = new ArrayList<>();

        List<String> allUsernames = model.getPlayers()
                .stream()
                .map(Player::getUsername)
                .collect(Collectors.toList());

        List<String> otherUsernames = allUsernames.stream()
                                                  .filter(s -> !s.equals(currentPlayer.getUsername()))
                                                  .collect(Collectors.toList());

        //adding faith points according to the player's order
        if(faithPoints != 0)
            globalChanges.addAll(gameContext.getGameModel().getFaithPath().executeMovement(faithPoints, currentPlayer));

        //adding resources to the player's hand, according to the player's order
        for(Map.Entry<ResourceSingle, Integer> entry : chosenResources.entrySet()){
            currentPlayer.getBoard().getStorage().getHand().addResources(entry.getKey(), entry.getValue());
        }

        //adding the payload for the added resources
        Map<String, Integer> chosenResourcesRaw = chosenResources.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey().toString().toLowerCase(), Map.Entry::getValue));
        if(chosenResourcesRaw.size() > 0)
            globalChanges.add(PayloadFactory.changeResources(currentPlayer.getUsername(), new RawStorage("Hand", chosenResourcesRaw)));

        //adding the payload for the discarded leader card (the secret one)
        for(Integer i : leaderIdToDiscard){
            secretChanges.add(PayloadFactory.discardLeaderCard(currentPlayer.getUsername(), i));
        }

        //adding the payload for the discarded leader card (the covered one)
        coveredCardChanges.add(PayloadFactory.changeCoveredLeaderCard(currentPlayer.getUsername(), -leaderIdToDiscard.size()));


        return Arrays.asList(
                new Message(allUsernames, globalChanges),
                new Message(Collections.singletonList(currentPlayer.getUsername()), secretChanges),
                new Message(otherUsernames, coveredCardChanges)
        );
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
        if(player == null || leadersToDiscard == null || chosenResources == null)
            throw new NullPointerException();

        for(Integer i : leadersToDiscard)
            if(i<0)
                throw new IllegalArgumentException("IDs of leaders to discard must be between 0 and total amount of leaders");

        for(Map.Entry<ResourceSingle, Integer> entry : chosenResources.entrySet())
            if(entry.getValue()<0)
                throw new IllegalArgumentException("Amount of chosen resources cannot be negative");
    }
}
