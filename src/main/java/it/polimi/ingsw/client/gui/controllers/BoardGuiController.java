package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.beans.*;
import it.polimi.ingsw.client.gui.dialogs.*;
import it.polimi.ingsw.client.gui.events.*;
import it.polimi.ingsw.client.gui.nodes.*;
import it.polimi.ingsw.client.gui.updaters.*;
import it.polimi.ingsw.client.model.ClientBaseStorage;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.client.model.ClientShelf;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.common.payload_components.groups.actions.*;
import it.polimi.ingsw.parser.raw.RawStorage;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BoardGuiController extends BaseController {
    @FXML
    public AnchorPane board;
    @FXML
    public ShopBox shop;
    @FXML
    public MarketBox market;
    @FXML
    public FaithPath faithPath;
    @FXML
    public MenuBox buttons;
    @FXML
    public TextArea messages;

    public ScoreboardBox scoreboard;

    public PlayerNode currentPlayerNode;
    private Map<String, PlayerNode> playerNodes;

    private String ownedUsername;

    public void initialize() {
        scoreboard = new ScoreboardBox();
        board.getChildren().add(scoreboard);

        board.addEventFilter(ConfirmEvent.CONFIRM_EVENT, this::sendConfirmPayload);
        board.addEventFilter(BackEvent.BACK_EVENT, this::sendBackPayload);
        board.addEventFilter(PreliminaryPickEvent.PRELIMINARY_PICK_EVENT_EVENT, this::onPreliminaryPickPressed);
        board.addEventFilter(ShopCardSelectionEvent.SHOP_CARD_SELECTION_EVENT, this::onShopCardSelection);
        board.addEventFilter(ConversionSelectionEvent.CONVERSION_SELECTION_EVENT, this::sendConversionOptionPayload);
        board.addEventFilter(MarketPickEvent.MARKET_PICK_EVENT, this::sendMarketPickPayload);
        board.addEventFilter(CraftingSelectionEvent.CRAFTING_SELECTION_EVENT, this::sendCraftingSelectionPayload);
        board.addEventFilter(LeaderInteractionEvent.LEADER_INTERACTION_EVENT, this::sendLeaderInteractionPayload);
        board.addEventFilter(ChangedCurrentPlayerEvent.CHANGED_CURRENT_PLAYER_EVENT, this::onChangedCurrentPlayer);
        board.addEventFilter(SwitchPlayerEvent.SWITCH_PLAYER_EVENT, this::onSwitchedShownPlayer);
        board.addEventFilter(SelectPlayEvent.SELECT_PLAY_EVENT_EVENT, this::sendSelectPlayPayload);
        board.addEventFilter(ResourceSelectionEvent.RESOURCE_SELECTION_EVENT, this::sendResourceSelectionPayload);
        board.addEventFilter(ResourceTransferEvent.RESOURCE_TRANSFER_EVENT, this::onStartResourceTransfer);
        board.addEventFilter(OutputSelectionEvent.OUTPUT_SELECTION_EVENT, this::onStartOutputSelection);
    }

    public void boardSetup() {
        playerNodes = new HashMap<>();

        for(ClientPlayer p : getModel().getPlayers()) {
            PlayerNode playerNode = new PlayerNode();
            playerNode.setLayoutX(14d);
            playerNode.setLayoutY(342d);
            playerNode.setPickOnBounds(false);
            playerNodes.put(p.getUsername(), playerNode);
        }

        scoreboard.setNames(FXCollections.observableList(getModel().getPlayers().stream().map(ClientPlayer::getUsername).collect(Collectors.toList())));
        AnchorPane.setLeftAnchor(scoreboard, 20d);
        AnchorPane.setTopAnchor(scoreboard, 20d);

        //--------------------------------------------------OFFLINE DEBUG------------------------------------//
        if(getServerHandler() == null) {
            getModel().getPersonalData().setPossibleActions(Set.of(PossibleActions.PRELIMINARY_PICK, PossibleActions.CONFIRM_TIDY));
            getModel().setCurrentPlayer(getModel().getPlayers().get(0));
            getModel().getPlayers().get(0).getCupboard().get(1).changeResources(new RawStorage("delta", new HashMap<>(){{put("gold", 2);}}));
            getModel().getPlayers().get(0).getCupboard().get(2).changeResources(new RawStorage("delta", new HashMap<>(){{put("shield", 1);}}));
            getModel().getPlayers().get(0).getCupboard().get(0).changeResources(new RawStorage("delta", new HashMap<>(){{put("servant", 1);}}));
            getModel().getPlayers().get(0).addLeaderShelf(new ClientShelf("Leader1", "servant", 2));
            getModel().getPlayers().get(0).getLeaderShelves().get(0).changeResources(new RawStorage("delta", new HashMap<>(){{put("servant", 1);}}));
            getModel().getPlayers().get(0).getHand().changeResources(new RawStorage("delta", new HashMap<>(){{put("gold", 2);put("servant", 1);}}));
            getModel().getPlayers().get(0).getMarketBasket().changeResources(new RawStorage("delta", new HashMap<>(){{put("gold", 2);put("servant", 1);}}));
            getModel().getPlayers().get(1).getCupboard().get(2).changeResources(new RawStorage("delta", new HashMap<>(){{put("shield", 1);}}));
        }

        ownedUsername = getServerHandler().getUsername();
        setActivePlayer(getServerHandler().getUsername());


        new ShopGuiUpdater(shop, getModel().getShop());
        new MarketGuiUpdater(market, getModel().getMarket());
        new MenuGuiUpdater(buttons, getModel().getPersonalData());
        new TurnGuiUpdater(buttons, scoreboard, getModel());
        new PersonalDataGuiUpdater(messages, getSceneManager().getStage(), getModel().getPersonalData());

        for(int i = 0; i < getModel().getPlayers().size(); i++) {
            new FaithPathGuiUpdater(faithPath, getModel().getPlayers().get(i).getFaithPath(), i + 1);
            new FlagHolderGuiUpdater(scoreboard, getModel().getPlayers().get(i).getFlagHolder(), i + 1);
            new PlayerGuiUpdater(scoreboard, playerNodes.get(getModel().getPlayers().get(i).getUsername()).getCupboard(), getModel().getPlayers().get(i), i + 1);
            new StorageGuiUpdater(playerNodes.get(getModel().getPlayers().get(i).getUsername()).getChest(), getModel().getPlayers().get(i).getChest());
            new StorageGuiUpdater(playerNodes.get(getModel().getPlayers().get(i).getUsername()).getHand(), getModel().getPlayers().get(i).getHand());
            new StorageGuiUpdater(playerNodes.get(getModel().getPlayers().get(i).getUsername()).getBasket(), getModel().getPlayers().get(i).getMarketBasket());
            new ProductionGuiUpdater(playerNodes.get(getModel().getPlayers().get(i).getUsername()).getProduction(), getModel().getPlayers().get(i).getProduction());
            new LeaderCardsGuiUpdater(playerNodes.get(getModel().getPlayers().get(i).getUsername()).getLeaders(), getModel().getPlayers().get(i).getLeaderCards());
        }

        if(getModel().getPlayers().size() == 1)
            new FaithPathGuiUpdater(faithPath, getModel().getPlayers().get(0).getFaithPath(), 0);
    }

    public void setActivePlayer(String username) {
        if(currentPlayerNode == null) {
            currentPlayerNode = playerNodes.get(username);
            board.getChildren().add(0, currentPlayerNode);
            scoreboard.setSpectatedPlayer(getModel().getPlayers().indexOf(getModel().getPlayerByName(username)) + 1);
        }
        else {
            currentPlayerNode = playerNodes.get(username);
            board.getChildren().set(0, currentPlayerNode);
            scoreboard.setSpectatedPlayer(getModel().getPlayers().indexOf(getModel().getPlayerByName(username)) + 1);
        }
    }

    private void changePlayerNodeControlsStatus(PlayerNode playerNode, boolean disable){
        playerNode.setAreControlsDisable(disable);
    }

    private void changeGlobalNodesControlsStatus(boolean disable){
        shop.setAreControlsDisabled(disable);
        market.setAreControlsDisabled(disable);
        buttons.setAreControlsDisabled(disable);
    }

    private String getIdFromContainerBean(ResourceContainerBean containerBean){
        if(containerBean.getSourceType().equals(PlayerNode.ContainerType.HAND))
            return getModel().getPlayerByName(ownedUsername).getHand().getStorage().getId();
        if(containerBean.getSourceType().equals(PlayerNode.ContainerType.CHEST))
            return getModel().getPlayerByName(ownedUsername).getChest().getStorage().getId();
        if(containerBean.getSourceType().equals(PlayerNode.ContainerType.MARKET))
            return getModel().getPlayerByName(ownedUsername).getMarketBasket().getStorage().getId();
        if(containerBean.getSourceType().equals(PlayerNode.ContainerType.BASE))
            return getModel().getPlayerByName(ownedUsername).getCupboard().get(containerBean.getIndex()).getStorage().getId();
        if(containerBean.getSourceType().equals(PlayerNode.ContainerType.LEADER))
            return getModel().getPlayerByName(ownedUsername).getLeaderShelves().get(containerBean.getIndex()).getStorage().getId();
        return null;
    }


    //UI-Related event handlers
    private void onShopCardSelection(ShopCardSelectionEvent event){
        ShopSelectionBean bean = event.getCardSelectionBean();
        bean.setUpgradableSlotsCount(getModel().getPlayers().get(0).getProduction().getUpgradableCraftingNumber());
        CustomDialog dialog = new ChooseCraftingDialog(getSceneManager().getStage(), bean, this::sendCardSelectionPayload);
        dialog.openDialog();
    }

    private void onPreliminaryPickPressed(PreliminaryPickEvent evt){
        CustomDialog preliminaryDialog = new PreliminaryPickDialog(getSceneManager().getStage(), this::sendPreliminaryPickPayload);
        preliminaryDialog.openDialog();
    }

    private void onStartResourceTransfer(ResourceTransferEvent evt){
        String id = getIdFromContainerBean(evt.getBean());

        List<ClientShelf> baseShelves = getModel().getPlayerByName(ownedUsername).getCupboard();
        List<ClientShelf> leaderShelves = getModel().getPlayerByName(ownedUsername).getLeaderShelves();
        ClientBaseStorage hand = getModel().getPlayerByName(ownedUsername).getHand();
        ClientBaseStorage marketBasket = getModel().getPlayerByName(ownedUsername).getMarketBasket();

        if(id != null){
            //the source can only be the hand, a shelf or the market basket
            Map<String, Integer> possibleResources;
            List<String> destinationsId = new ArrayList<>();
            if(evt.getBean().getSourceType().equals(PlayerNode.ContainerType.HAND)){
                possibleResources = hand.getStorage().getResources();
                destinationsId.addAll(baseShelves.stream().map(s -> s.getStorage().getId()).collect(Collectors.toList()));
                List<String> extendedLeaderIds = new ArrayList<>();
                for(int i = 0; i < leaderShelves.size(); i++){
                    extendedLeaderIds.add(leaderShelves.get(i).getStorage().getId() + "$Leader " + (i + 1));
                }
                destinationsId.addAll(extendedLeaderIds);
            }else if(evt.getBean().getSourceType().equals(PlayerNode.ContainerType.MARKET)){
                possibleResources = marketBasket.getStorage().getResources();
                destinationsId.addAll(baseShelves.stream().map(s -> s.getStorage().getId()).collect(Collectors.toList()));

                List<String> extendedLeaderIds = new ArrayList<>();
                for(int i = 0; i < leaderShelves.size(); i++){
                    extendedLeaderIds.add(leaderShelves.get(i).getStorage().getId() + "$Leader " + (i + 1));
                }
                destinationsId.addAll(extendedLeaderIds);

            }else if(evt.getBean().getSourceType().equals(PlayerNode.ContainerType.BASE)){
                possibleResources = baseShelves.get(evt.getBean().getIndex()).getStorage().getResources();
                destinationsId.addAll(baseShelves.stream().map(s -> s.getStorage().getId()).filter(x -> !x.equals(id)).collect(Collectors.toList()));
                List<String> extendedLeaderIds = new ArrayList<>();
                for(int i = 0; i < leaderShelves.size(); i++){
                    extendedLeaderIds.add(leaderShelves.get(i).getStorage().getId() + "$Leader " + (i + 1));
                }
                destinationsId.addAll(extendedLeaderIds);
                destinationsId.add(hand.getStorage().getId());

            }else if(evt.getBean().getSourceType().equals(PlayerNode.ContainerType.LEADER)){
                possibleResources = leaderShelves.get(evt.getBean().getIndex()).getStorage().getResources();
                destinationsId.addAll(baseShelves.stream().map(s -> s.getStorage().getId()).collect(Collectors.toList()));
                List<String> extendedLeaderIds = new ArrayList<>();
                for(int i = 0; i < leaderShelves.size(); i++){
                    if(!leaderShelves.get(i).getStorage().getId().equals(id))
                        extendedLeaderIds.add(leaderShelves.get(i).getStorage().getId() + "$Leader " + (i + 1));
                }
                destinationsId.addAll(extendedLeaderIds);
                destinationsId.add(hand.getStorage().getId());
            }else{
                return;
            }

            if(!possibleResources.isEmpty()){
                //distinguish between transfer action and collect from basket action

                Consumer<ResourceTransferBean> consumer = (evt.getBean().getSourceType().equals(PlayerNode.ContainerType.MARKET)) ? this::sendCollectFromBasket : this::sendResourcesTransferPayload;
                ResourceTransferBean transferBean = new ResourceTransferBean();
                transferBean.setSourceId(id);
                CustomDialog resourceTransferDialog = new ResourcesTransferDialog(getSceneManager().getStage(), destinationsId, possibleResources,
                                                                                  transferBean, consumer);
                resourceTransferDialog.openDialog();
            }
        }
    }

    private void onStartOutputSelection(OutputSelectionEvent event){
        CustomDialog dialog = new OutputSelectionDialog(getSceneManager().getStage(), event.getBean(), this::sendOutputSelectionPayload);
        dialog.openDialog();
    }

    private void onChangedCurrentPlayer(ChangedCurrentPlayerEvent event){

        if(ownedUsername.equals(event.getUsername())){
            changePlayerNodeControlsStatus(playerNodes.get(ownedUsername), false);
            changeGlobalNodesControlsStatus(false);
        }else{
            changePlayerNodeControlsStatus(playerNodes.get(ownedUsername), true);
            changeGlobalNodesControlsStatus(true);
        }
    }

    private void onSwitchedShownPlayer(SwitchPlayerEvent event){
        setActivePlayer(event.getUsername());
        changeGlobalNodesControlsStatus(!event.getUsername().equals(ownedUsername));
    }



    //Action sending events
    private void sendCardSelectionPayload(ShopSelectionBean bean){
        if(getServerHandler() != null){
            getServerHandler().sendPayload(new SelectCardFromShopActionPayloadComponent(ownedUsername, bean.getRow(), bean.getCol(), bean.getUpgradableIndex() -1));
        }
        System.out.println("Selected card (" + bean.getRow() + ", " + bean.getCol() + ") and upgradable crafting " + bean.getUpgradableIndex());
    }

    private void sendConversionOptionPayload(ConversionSelectionEvent event){
        if(getServerHandler() != null){
            getServerHandler().sendPayload(new SelectConversionsActionPayloadComponent(ownedUsername, event.getBean().getSelectedConversions()));
        }
        System.out.println("Selected options: " + event.getBean().getSelectedConversions());
    }

    private void sendMarketPickPayload(MarketPickEvent event){
        if(getServerHandler() != null){
            getServerHandler().sendPayload(new BuyFromMarketActionPayloadComponent(ownedUsername, event.getBean().isRow(), event.getBean().getIndex()));
        }
        System.out.println("Market Pick: " + event.getBean().isRow() + " " + event.getBean().getIndex());
    }

    private void sendCraftingSelectionPayload(CraftingSelectionEvent event){
        if(getServerHandler() != null){
            getServerHandler().sendPayload(new SelectCraftingActionPayloadComponent(ownedUsername, event.getBean().getCraftingType(), event.getBean().getIndex()));
        }
        System.out.println("Crafting Selected: " + event.getBean().getCraftingType() + " " + event.getBean().getIndex());
    }

    private void sendResourceSelectionPayload(ResourceSelectionEvent selectionEvent){
        String id = getIdFromContainerBean(selectionEvent.getBean());
        String resource = selectionEvent.getBean().getResource();
        int amount = selectionEvent.getBean().getAmount();
        if(id != null){
            if(getServerHandler() != null){
                getServerHandler().sendPayload(new SelectResourcesActionPayloadComponent(ownedUsername, id, resource, amount));
            }
            System.out.println("Select resources " + id + " " + resource + " " + amount);
        }
    }

    private void sendResourcesTransferPayload(ResourceTransferBean bean){
        if(getServerHandler() != null){
            getServerHandler().sendPayload(new ResourcesMoveActionPayloadComponent(ownedUsername, bean.getSourceId(), bean.getTargetDestination(), bean.getChoseResource(), bean.getAmountToTransfer()));
        }
        System.out.println("Resources move" + bean);
    }

    private void sendCollectFromBasket(ResourceTransferBean bean){
        if(getServerHandler() != null){
            getServerHandler().sendPayload(new MoveFromBasketToShelfActionPayloadComponent(ownedUsername,  bean.getChoseResource(), bean.getAmountToTransfer(), bean.getTargetDestination()));
        }
        System.out.println("Collect from basket: " + bean);
    }

    private void sendOutputSelectionPayload(OutputSelectionBean bean){
        if(getServerHandler() != null){
            getServerHandler().sendPayload(new SelectCraftingOutputActionPayloadComponent(ownedUsername, bean.getSelection()));
        }
        System.out.println("Output selection: " + bean.getSelection());
    }

    private void sendLeaderInteractionPayload(LeaderInteractionEvent event){
        boolean isActivate = event.getBean().isActivate();
        int index = event.getBean().getIndex();

        int id;
        try {
            id = getModel().getPlayerByName(ownedUsername).getLeaderCards().getLeaderCards().get(index).getId();
        }catch(RuntimeException e){
            id = -1;
        }

        if(isActivate) {
            if(getServerHandler() != null){
                getServerHandler().sendPayload(new ActivateLeaderActionPayloadComponent(ownedUsername, id));
            }
            System.out.println("Activate Leader with id " + id + " (index: " + index + ")");
        }
        else {
            if(getServerHandler() != null){
                getServerHandler().sendPayload(new DiscardLeaderActionPayloadComponent(ownedUsername, id));
            }
            System.out.println("Discarded Leader with id " + id + " (index: " + index + ")");
        }
    }

    private void sendConfirmPayload(ConfirmEvent event){
        Set<PossibleActions> possibleActions = getModel().getPersonalData().getPossibleActions();
        if(possibleActions.contains(PossibleActions.CONFIRM)){
            if(getServerHandler() != null){
                getServerHandler().sendPayload(new ConfirmActionPayloadComponent(ownedUsername));
            }
            System.out.println("Confirmed");
        }else if(possibleActions.contains(PossibleActions.CONFIRM_TIDY)){
            if(getServerHandler() != null){
                getServerHandler().sendPayload(new ConfirmTidyActionPayloadComponent(ownedUsername));
            }
            System.out.println("ConfirmedTidy");
        }
    }

    private void sendPreliminaryPickPayload(PreliminaryPickBean bean){
        if(getServerHandler() != null)
            getServerHandler().sendPayload(new PreliminaryPickActionPayloadComponent(ownedUsername, bean.getLeaderIndexes(), bean.getSelectedResources()));
        System.out.println("PreliminaryPick: " + bean.getLeaderIndexes() + " " + bean.getSelectedResources());
    }

    private void sendSelectPlayPayload(SelectPlayEvent event){
        if(getServerHandler() != null)
            getServerHandler().sendPayload(new SelectPlayActionPayloadComponent(ownedUsername, event.getPlay()));
        System.out.println("Selected play: " + event.getPlay());
    }

    private void sendBackPayload(BackEvent event){
        if(getServerHandler() != null)
            getServerHandler().sendPayload(new BackActionPayloadComponent(ownedUsername));
        System.out.println("Back");
    }

}
