package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.updates.SetInitialConfigurationUpdatePayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.gamematerials.LevelFlag;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shop;
import it.polimi.ingsw.model.actions.StartGameAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.model.leader.LeaderCard;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Extends State and represents a concrete state of the state machine.
 * In this state the game has left the lobby as all players are in game. The model is created and messages
 * are sent to the clients. Players do not have choices.
 */
public class SetupState extends State {
    /**
     * A new State is created with the provided game context. The nextState and the interruptListener
     * are both initially null
     *
     * @param gameContext the context of the game
     * @throws NullPointerException if gameContext is null
     */
    public SetupState(GameContext gameContext) {
        super(gameContext);
    }

    /**
     * This method will be executed every time this state is entered from a different state
     * Json files, shop, market and leaders are sent to all the clients.
     * The current player is set.
     * @return the list of messages to be sent to the client
     */
    @Override
    public List<Message> onEntry() {
        List<Message> messages = super.onEntry();
        List<PayloadComponent> globalPayload = new ArrayList<>();

        //Sending the configuration files
        globalPayload.add(new SetInitialConfigurationUpdatePayloadComponent(
                getGameContext().getConfigJson(),
                getGameContext().getCraftingJson(),
                getGameContext().getFaithJson(),
                getGameContext().getLeadersJson(),
                getGameContext().getGameModel().getPlayerNames())
        );

        //TODO: shuffle players first
        //Setting up the current player
        getGameContext().setCurrentPlayer(getGameContext().getGameModel().getPlayers().get(0));
        globalPayload.add(PayloadFactory.changeCurrentPlayer(getGameContext().getCurrentPlayer().getUsername()));

        //send the market configuration
        globalPayload.add(PayloadFactory.changeMarket(getGameContext().getGameModel().getMarket().toRaw()));

        //send the shop configuration
        Shop shop = getGameContext().getGameModel().getShop();
        for(int i = 0; i < shop.getRowSize(); i++){
            for(int j = 0; j < shop.getColumnSize(); j++){
                globalPayload.add(PayloadFactory.changeShop(j, i, shop.getTopCard(i, j).getId()));
            }
        }


        //TODO: remove this hack (debug only)

        for(String username : getGameContext().getGameModel().getPlayerNames()){
            Map<ResourceSingle, Integer> debugResources = new HashMap<>();
            debugResources.put(ResourceTypeSingleton.getInstance().getGoldResource(), 10);
            debugResources.put(ResourceTypeSingleton.getInstance().getServantResource(), 10);
            debugResources.put(ResourceTypeSingleton.getInstance().getShieldResource(), 10);
            debugResources.put(ResourceTypeSingleton.getInstance().getStoneResource(), 10);

            for(Map.Entry<ResourceSingle, Integer> entry : debugResources.entrySet()){
                getGameContext().getGameModel().getPlayerById(username).getBoard().getStorage().getChest().addResources(entry.getKey(), entry.getValue());
            }
            globalPayload.add(
                    PayloadFactory.changeResources(username,
                            new RawStorage(getGameContext().getGameModel().getPlayerById(username).getBoard().getStorage().getChest().getId(),
                                    debugResources.entrySet().stream()
                                            .collect(Collectors.toMap(e -> e.getKey().getId(), Map.Entry::getValue))))
            );
        }

        //TODO: remove this hack (debug only)

        LevelFlag green = new LevelFlag(FlagColor.GREEN, 2);
        LevelFlag yellow = new LevelFlag(FlagColor.YELLOW, 2);
        LevelFlag blue = new LevelFlag(FlagColor.BLUE, 2);
        for(String username : getGameContext().getGameModel().getPlayerNames()){
            Player p = getGameContext().getGameModel().getPlayerById(username);
            p.getBoard().getFlagHolder().addFlag(green);
            p.getBoard().getFlagHolder().addFlag(yellow);
            p.getBoard().getFlagHolder().addFlag(blue);
            globalPayload.add(PayloadFactory.addFlag(username, green.toRaw()));
            globalPayload.add(PayloadFactory.addFlag(username, yellow.toRaw()));
            globalPayload.add(PayloadFactory.addFlag(username, blue.toRaw()));
        }

        for(String username : getGameContext().getGameModel().getPlayerNames()){
            Crafting baseCrafting;
            try {
                baseCrafting = JSONParser.parseBaseCrafting(getGameContext().getCraftingJson()).get(0);
                getGameContext().getGameModel().getPlayerById(username).getBoard().getProduction().addBaseCrafting(baseCrafting);
                globalPayload.add(PayloadFactory.addCrafting(username, baseCrafting.toRaw(), Production.CraftingType.BASE,
                        getGameContext().getGameModel().getPlayerById(username).getBoard().getProduction().getAllBaseCrafting().size() - 1));
            } catch (ParserException e) {
                Logger.log("Failed to load the base crafting. Trying to play nevertheless");
            }

        }

        //send the global message
        messages.add(new Message(getGameContext().getGameModel().getPlayerNames(), globalPayload));


        //shuffling the cards
        List<LeaderCard> leaderCards = getGameContext().getGameModel().getLeaderCards();
        Collections.shuffle(leaderCards, new Random(1));

        int i = 0;
        List<PayloadComponent> specificPlayer;
        List<PayloadComponent> otherPlayers;

        for(String username : getGameContext().getGameModel().getPlayerNames()){
            specificPlayer = new ArrayList<>();
            otherPlayers = new ArrayList<>();

            //adding the cards to the player in the model
            Player p = getGameContext().getGameModel().getPlayerById(username);
            for(int j = 0; j < getGameContext().getGameConfig().getAmountOfLeaderPerPlayer(); j++){
                p.getBoard().addLeaderCard(leaderCards.get(i));
                specificPlayer.add(PayloadFactory.addLeaderCard(username, p.getBoard().getLeaderCards().get(j).getId()));
                i++;
            }
            //inform all the other clients of the increase in covered cards
            otherPlayers.add(PayloadFactory.changeCoveredLeaderCard(username, getGameContext().getGameConfig().getAmountOfLeaderPerPlayer()));

            //sending the payloads to the clients
            messages.add(new Message(Collections.singletonList(username), specificPlayer));
            messages.add(new Message(getGameContext().getGameModel().getPlayerNames().stream().filter(x -> !x.equals(username)).collect(Collectors.toList()), otherPlayers));
        }

        launchInterrupt(new StartGameAction(), ActionQueue.Priority.SERVER_ACTION.ordinal());


        return messages;
    }

    /**
     * The game starts.
     * @param startGameAction the action that starts the game.
     * @return the list of messages to send to the clients.
     * @throws FSMTransitionFailedException iff the action cannot be performed.
     */
    @Override
    public List<Message> handleAction(StartGameAction startGameAction) throws FSMTransitionFailedException {
        setNextState(new PreliminaryPickState(getGameContext()));
        return new ArrayList<>();
    }
}
