package it.polimi.ingsw.utils;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.GameParameters;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.server.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class GameUtilities {

    /**
     * Automatically assigns player random leaders to discard and picked resources.
     * @param gameContext the context of the game
     * @return list of messages to send to the clients
     */
    public static List<Message> automatedPick(GameContext gameContext){
        List<Message> messages = new ArrayList<>();
        Random random = new Random();

        //for each player checks if they have already picked. If not, picks randomly
        for(Player p : gameContext.getGameModel().getPlayers()){
            List<Integer> leadersToDiscard = new ArrayList<>();
            if(!gameContext.getAlreadyPickedPlayers().contains(p)){

                //randomly generating leaders to discard
                for(int i=0; i<gameContext.getGameConfig().getAmountOfLeadersToDiscard();) {
                    Integer randomNumber = random.nextInt(p.getBoard().getLeaderCards().size());
                    if (!leadersToDiscard.contains(randomNumber)) {
                        leadersToDiscard.add(randomNumber);
                        i++;
                    }
                }

                //Randomly chooses resources
                Map<ResourceSingle, Integer> chosenResources = new HashMap<>();
                int playerOrder = gameContext.getGameModel().getPlayers().indexOf(p);
                switch (playerOrder) {
                    case 0: break;
                    case 1: chosenResources.put(ResourceTypeSingleton.getInstance().getRandomResourceSingle(), gameContext
                            .getGameConfig().getSecondPlayerAmountOfResourcesOnStart());
                            break;
                    case 2: chosenResources.put(ResourceTypeSingleton.getInstance().getRandomResourceSingle(), gameContext
                            .getGameConfig().getThirdPlayerAmountOfResourcesOnStart());
                            break;
                    default: chosenResources.put(ResourceTypeSingleton.getInstance().getRandomResourceSingle(), gameContext
                            .getGameConfig().getFourthPlayerAmountOfResourcesOnStart());
                }

                try {
                    messages.addAll(doPreliminaryPick(gameContext, p.getUsername(), leadersToDiscard, chosenResources));
                }catch (IllegalActionException e){
                    Logger.log("Logic failed in automated pick", Logger.Severity.ERROR);
                }
            }
        }
        return messages;
    }

    /**
     * the player chooses the leaders to discard and, depending on the player's order, picks resources and adds faith points.
     * @param gameContext the context of the game
     * @param player the player picking
     * @param leadersToDiscard the leaders to discard, by index not ID
     * @param chosenResources the chosen resources
     * @return the messages to send to the clients
     * @throws IllegalActionException if action cannot be performed
     */
    public static List<Message> doPreliminaryPick(GameContext gameContext, String player, List<Integer> leadersToDiscard,
                                                  Map<ResourceSingle, Integer> chosenResources) throws IllegalActionException{

        Player pickingPlayer = gameContext.getGameModel().getPlayerById(player);

        //checking if amount of leaders to discard is correct
        if(leadersToDiscard.size()!= gameContext.getGameConfig().getAmountOfLeadersToDiscard())
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
                    .map(l -> pickingPlayer.getBoard().getLeaderCards().get(l).getId())
                    .collect(Collectors.toList());
        }catch(IndexOutOfBoundsException e){
            throw new IllegalActionException("Selected an invalid card index");
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
            case 0: if(amountOfResources!=gameContext.getGameConfig().getFirstPlayerAmountOfResourcesOnStart())
                throw new IllegalActionException("First player must get the correct amount of resources on start");
                faithPoints = gameContext.getGameConfig().getFirstPlayerAmountOfFaithPointsOnStart();
                break;
            case 1: if(amountOfResources!=gameContext.getGameConfig().getSecondPlayerAmountOfResourcesOnStart())
                throw new IllegalActionException("Second player must get the correct amount of resources on start");
                faithPoints = gameContext.getGameConfig().getSecondPlayerAmountOfFaithPointsOnStart();
                break;
            case 2: if(amountOfResources!=gameContext.getGameConfig().getThirdPlayerAmountOfResourcesOnStart())
                throw new IllegalActionException("Third player must get the correct amount of resources on start");
                faithPoints = gameContext.getGameConfig().getThirdPlayerAmountOfFaithPointsOnStart();
                break;
            case 3: if(amountOfResources!=gameContext.getGameConfig().getFourthPlayerAmountOfResourcesOnStart())
                throw new IllegalActionException("Fourth player must get the correct amount of resources on start");
                faithPoints = gameContext.getGameConfig().getFourthPlayerAmountOfFaithPointsOnStart();
        }

        //discard leaders
        try {
            pickingPlayer.getBoard().removeLeaderCardsByIndex(leadersToDiscard);
        }catch (IndexOutOfBoundsException e){
            throw new IllegalActionException(e.getMessage());
        }

        //adding player to list of players who picked
        gameContext.addPlayerWhoPicked(pickingPlayer);

        List<PayloadComponent> globalChanges = new ArrayList<>();
        List<PayloadComponent> secretChanges = new ArrayList<>();
        List<PayloadComponent> coveredCardChanges = new ArrayList<>();

        List<String> allUsernames = gameContext.getGameModel().getPlayers()
                .stream()
                .map(Player::getUsername)
                .collect(Collectors.toList());

        List<String> otherUsernames = allUsernames.stream()
                .filter(s -> !s.equals(pickingPlayer.getUsername()))
                .collect(Collectors.toList());

        //adding faith points according to the player's order
        if(faithPoints != 0)
            globalChanges.addAll(gameContext.getGameModel().getFaithPath().executeMovement(faithPoints, pickingPlayer));

        //adding resources to the player's hand, according to the player's order
        for(Map.Entry<ResourceSingle, Integer> entry : chosenResources.entrySet()){
            pickingPlayer.getBoard().getStorage().getHand().addResources(entry.getKey(), entry.getValue());
        }

        //adding the payload for the added resources
        Map<String, Integer> chosenResourcesRaw = chosenResources.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey().toString().toLowerCase(), Map.Entry::getValue));
        if(chosenResourcesRaw.size() > 0)
            globalChanges.add(PayloadFactory.changeResources(pickingPlayer.getUsername(), new RawStorage(pickingPlayer
                    .getBoard().getStorage().getHand().getId(), chosenResourcesRaw)));

        //adding the payload for the discarded leader card (the secret one)
        for(Integer i : leaderIdToDiscard){
            secretChanges.add(PayloadFactory.discardLeaderCard(pickingPlayer.getUsername(), i));
        }

        //adding the payload for the discarded leader card (the covered one)
        coveredCardChanges.add(PayloadFactory.changeCoveredLeaderCard(pickingPlayer.getUsername(), -leaderIdToDiscard.size()));


        return Arrays.asList(
                new Message(allUsernames, globalChanges),
                new Message(Collections.singletonList(pickingPlayer.getUsername()), secretChanges),
                new Message(otherUsernames, coveredCardChanges)
        );
    }

    /**
     * calculates next connected player, starting from current player and, if necessary, going on from the first player
     * of the list back to the current player.
     * Returns null if all player are simultaneously disconnected.
     * @param gameContext the context of the game
     * @return next connected player or null
     */
    public static Player calculateNextConnectedPlayer(GameContext gameContext){
        List<Player> players = gameContext.getGameModel().getPlayers();

        //next player is set to null
        Player nextPlayer = null;
        int index = players.indexOf(gameContext.getCurrentPlayer());

        //scan list of player for the next connected player. Once it finishes is restarts from first player to current player.
        for(int i = 0;  i < players.size(); i++){
            if(players.get((i+index+1) % players.size()).isConnected()) {
                nextPlayer = players.get((i+index+1) % players.size());
                break;
            }
        }

        return nextPlayer;
    }

    /**
     * @param gameContext the context of the game
     * @return true if round restarts, false if it does not or if there are no connected players
     */
    public static boolean doesRoundStartAgain(GameContext gameContext){
        List<Player> players = gameContext.getGameModel().getPlayers();
        Player currentPlayer = gameContext.getCurrentPlayer();
        Player nextConnectedPlayer = calculateNextConnectedPlayer(gameContext);

        if(nextConnectedPlayer == null)
            return false;

        int currentPlayerIndex = players.indexOf(currentPlayer);
        int nextPlayerIndex = players.indexOf(nextConnectedPlayer);

        return nextPlayerIndex <= currentPlayerIndex;
    }

    /**
     * Returns true iff p1 comes strictly before p2 in the order of player.
     * @param gameContext the game context
     * @param p1 a player
     * @param p2 another player
     * @return true iff p1 comes strictly before p2 in the order of player.
     */
    public static boolean comesFirst(GameContext gameContext, Player p1, Player p2){
        List<Player> players = gameContext.getGameModel().getPlayers();
        int p1Index = players.indexOf(p1);
        int p2Index = players.indexOf(p2);

        return p1Index < p2Index;
    }

    /**
     * Returns the number of connected players
     * @param gameContext the game context
     * @return the number of connected players
     */
    public static int numOfConnectedPlayers(GameContext gameContext){
        return (int) gameContext.getGameModel().getPlayers().stream().filter(Player::isConnected).count();
    }

}
