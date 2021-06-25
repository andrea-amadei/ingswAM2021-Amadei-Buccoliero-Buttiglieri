package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.beans.PreliminaryPickBean;
import it.polimi.ingsw.client.gui.beans.ShopSelectionBean;
import it.polimi.ingsw.client.gui.dialogs.ChooseCraftingDialog;
import it.polimi.ingsw.client.gui.dialogs.CustomDialog;
import it.polimi.ingsw.client.gui.dialogs.PreliminaryPickDialog;
import it.polimi.ingsw.client.gui.events.*;
import it.polimi.ingsw.client.gui.nodes.*;
import it.polimi.ingsw.client.gui.updaters.*;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.common.payload_components.groups.actions.*;
import it.polimi.ingsw.model.actions.SelectPlayAction;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.util.*;
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
        board.addEventFilter(PreliminaryPickEvent.PRELIMINARY_PICK_EVENT_EVENT, this::onPreliminaryPickPressed);
        board.addEventFilter(ShopCardSelectionEvent.SHOP_CARD_SELECTION_EVENT, this::onShopCardSelection);
        board.addEventFilter(ConversionSelectionEvent.CONVERSION_SELECTION_EVENT, this::sendConversionOptionPayload);
        board.addEventFilter(MarketPickEvent.MARKET_PICK_EVENT, this::sendMarketPickPayload);
        board.addEventFilter(CraftingSelectionEvent.CRAFTING_SELECTION_EVENT, this::sendCraftingSelectionPayload);
        board.addEventFilter(LeaderInteractionEvent.LEADER_INTERACTION_EVENT, this::sendLeaderInteractionPayload);
        board.addEventFilter(ChangedCurrentPlayerEvent.CHANGED_CURRENT_PLAYER_EVENT, this::onChangedCurrentPlayer);
        board.addEventFilter(SwitchPlayerEvent.SWITCH_PLAYER_EVENT, this::onSwitchedShownPlayer);
        board.addEventFilter(SelectPlayEvent.SELECT_PLAY_EVENT_EVENT, this::sendSelectPlayPayload);
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
        }
        // show the self board
        // TODO: change to:
        // setActivePlayer(getModel().getPersonalData().getUsername());
        String selfUsername = getModel().getPersonalData().getUsername();
        if(selfUsername == null || selfUsername.equals("Unknown"))
            selfUsername = getModel().getPlayers().get(0).getUsername();
        setActivePlayer(selfUsername);

        ownedUsername = selfUsername;

        /*getModel().setCurrentPlayer(getModel().getPlayers().get(1));
        getModel().getPlayers().get(1).getLeaderCards().changeCoveredCardsNumber(2);
        try {
            getModel().getPlayers().get(1).getLeaderCards().addLeaderCard(JSONParser.parseToRaw("{\"id\":2,\"name\":\"aaa\",\"points\":2,\"requirements\":[{\"type\":\"flag\",\"color\":\"BLUE\",\"amount\":1},{\"type\":\"flag\",\"color\":\"PURPLE\",\"amount\":1}],\"special_abilities\":[{\"type\":\"discount\",\"resource\":\"shield\",\"amount\":1}]}", RawLeaderCard.class));
        } catch (ParserException | IllegalRawConversionException e) {
            e.printStackTrace();
        }*/

        // TODO: ADD ALL GLOBAL UPDATERS
        new ShopGuiUpdater(shop, getModel().getShop());
        new MarketGuiUpdater(market, getModel().getMarket());
        new MenuGuiUpdater(buttons, getModel().getPersonalData());
        new TurnGuiUpdater(buttons, getModel());
        new PersonalDataGuiUpdater(messages, getSceneManager().getStage(), getModel().getPersonalData());

        // TODO: ADD ALL PLAYER SPECIFIC UPDATERS
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

    }

    public void setActivePlayer(String username) {
        if(currentPlayerNode == null) {
            currentPlayerNode = playerNodes.get(username);
            board.getChildren().add(0, currentPlayerNode);
        }
        else {
            currentPlayerNode = playerNodes.get(username);
            board.getChildren().set(0, currentPlayerNode);
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

    private void onChangedCurrentPlayer(ChangedCurrentPlayerEvent event){
        String selfUsername = getModel().getPersonalData().getUsername();

        //TODO: remove this, we only use it for testing without connection to server
        if(selfUsername == null || selfUsername.equals("Unknown")){
            selfUsername = getModel().getPlayers().get(0).getUsername();
        }

        if(selfUsername.equals(event.getUsername())){
            changePlayerNodeControlsStatus(playerNodes.get(selfUsername), false);
            changeGlobalNodesControlsStatus(false);
        }else{
            changePlayerNodeControlsStatus(playerNodes.get(selfUsername), true);
            changeGlobalNodesControlsStatus(true);
        }
    }

    private void onSwitchedShownPlayer(SwitchPlayerEvent event){
        setActivePlayer(event.getUsername());
    }



    //Action sending events
    private void sendCardSelectionPayload(ShopSelectionBean bean){
        if(getServerHandler() != null){
            getServerHandler().sendPayload(new SelectCardFromShopActionPayloadComponent(ownedUsername, bean.getRow(), bean.getCol(), bean.getUpgradableIndex()));
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

    private void sendLeaderInteractionPayload(LeaderInteractionEvent event){
        boolean isActivate = event.getBean().isActivate();
        int index = event.getBean().getIndex();

        int id;
        try {
            id = getModel().getPlayers().get(0).getLeaderCards().getLeaderCards().get(index).getId();
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

}
