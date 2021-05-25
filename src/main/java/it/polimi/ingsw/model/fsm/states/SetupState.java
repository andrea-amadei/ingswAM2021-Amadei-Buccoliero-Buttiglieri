package it.polimi.ingsw.model.fsm.states;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.updates.SetInitialConfigurationUpdatePayloadComponent;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.gamematerials.ResourceSingle;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.Shop;
import it.polimi.ingsw.model.actions.StartGameAction;
import it.polimi.ingsw.model.fsm.GameContext;
import it.polimi.ingsw.model.fsm.State;
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.utils.PayloadFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        //TODO: distribute the leader cards to the players


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


        launchInterrupt(new StartGameAction(), ActionQueue.Priority.SERVER_ACTION.ordinal());
        //send the global message
        messages.add(new Message(getGameContext().getGameModel().getPlayerNames(), globalPayload));

        return messages;
    }

    @Override
    public List<Message> handleAction(StartGameAction startGameAction) throws FSMTransitionFailedException {
        setNextState(new MenuState(getGameContext()));
        return new ArrayList<>();
    }
}
