package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.beans.ShopSelectionBean;
import it.polimi.ingsw.client.gui.dialogs.ChooseCraftingDialog;
import it.polimi.ingsw.client.gui.dialogs.CustomDialog;
import it.polimi.ingsw.client.gui.events.*;
import it.polimi.ingsw.client.gui.nodes.*;
import it.polimi.ingsw.client.gui.updaters.*;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.client.model.ClientShelf;
import it.polimi.ingsw.client.model.ConversionOption;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.gamematerials.LevelFlag;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.fsm.states.ConversionSelectionState;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.*;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;
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

    public void initialize() {
        scoreboard = new ScoreboardBox();
        board.getChildren().add(scoreboard);

        board.addEventFilter(ShopCardSelectionEvent.SHOP_CARD_SELECTION_EVENT, this::onShopCardSelection);
        board.addEventFilter(ConversionSelectionEvent.CONVERSION_SELECTION_EVENT, this::sendConversionOptionPayload);
        board.addEventFilter(MarketPickEvent.MARKET_PICK_EVENT, this::sendMarketPickPayload);
        board.addEventFilter(CraftingSelectionEvent.CRAFTING_SELECTION_EVENT, this::sendCraftingSelectionPayload);
        board.addEventFilter(LeaderInteractionEvent.LEADER_INTERACTION_EVENT, this::sendLeaderInteractionPayload);
    }

    public void boardSetup() {
        playerNodes = new HashMap<>();

        for(ClientPlayer p : getModel().getPlayers()) {
            PlayerNode playerNode = new PlayerNode();
            playerNode.setLayoutX(14d);
            playerNode.setLayoutY(342d);
            playerNodes.put(p.getUsername(), playerNode);
        }

        scoreboard.setNames(FXCollections.observableList(getModel().getPlayers().stream().map(ClientPlayer::getUsername).collect(Collectors.toList())));
        AnchorPane.setLeftAnchor(scoreboard, 20d);
        AnchorPane.setTopAnchor(scoreboard, 20d);

        // TODO: change to:
        // setActivePlayer(getModel().getPersonalData().getUsername());
        setActivePlayer(getModel().getPlayers().get(0).getUsername());

        // TODO: ADD ALL GLOBAL UPDATERS
        new ShopGuiUpdater(shop, getModel().getShop());
        new MarketGuiUpdater(market, getModel().getMarket());

        // TODO: ADD ALL PLAYER SPECIFIC UPDATERS
        System.out.println(getModel().getPlayers().size());
        for(int i = 0; i < getModel().getPlayers().size(); i++) {
            new FaithPathGuiUpdater(faithPath, getModel().getPlayers().get(i).getFaithPath(), i + 1);
            new PersonalDataGuiUpdater(messages, getModel().getPersonalData());
            new FlagHolderGuiUpdater(scoreboard, getModel().getPlayers().get(i).getFlagHolder(), i + 1);
            new PlayerGuiUpdater(scoreboard, playerNodes.get(getModel().getPlayers().get(i).getUsername()).getCupboard(), getModel().getPlayers().get(i), i + 1);
            new StorageGuiUpdater(playerNodes.get(getModel().getPlayers().get(i).getUsername()).getChest(), getModel().getPlayers().get(i).getChest());
            new StorageGuiUpdater(playerNodes.get(getModel().getPlayers().get(i).getUsername()).getHand(), getModel().getPlayers().get(i).getHand());
            new StorageGuiUpdater(playerNodes.get(getModel().getPlayers().get(i).getUsername()).getBasket(), getModel().getPlayers().get(i).getMarketBasket());
            new ProductionGuiUpdater(playerNodes.get(getModel().getPlayers().get(i).getUsername()).getProduction(), getModel().getPlayers().get(i).getProduction());
        }

        changeGlobalNodesControlsStatus(false);
        changePlayerNodeControlsStatus(currentPlayerNode, false);
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
    }


    private void onShopCardSelection(ShopCardSelectionEvent event){
        ShopSelectionBean bean = event.getCardSelectionBean();
        bean.setUpgradableSlotsCount(getModel().getPlayers().get(0).getProduction().getUpgradableCraftingNumber());
        CustomDialog dialog = new ChooseCraftingDialog(getSceneManager().getStage(), bean, this::sendCardSelectionPayload);
        dialog.openDialog();
    }


    private void sendCardSelectionPayload(ShopSelectionBean bean){
        System.out.println("Selected card (" + bean.getRow() + ", " + bean.getCol() + ") and upgradable crafting " + bean.getUpgradableIndex());
    }

    private void sendConversionOptionPayload(ConversionSelectionEvent event){
        System.out.println("Selected options: " + event.getBean().getSelectedConversions());
    }

    private void sendMarketPickPayload(MarketPickEvent event){
        System.out.println("Market Pick: " + event.getBean().isRow() + " " + event.getBean().getIndex());
    }

    private void sendCraftingSelectionPayload(CraftingSelectionEvent event){
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

        if(isActivate)
            System.out.println("Activate Leader with id " + id + " (index: " + index + ")");
        else
            System.out.println("Discarded Leader with id " + id + " (index: " + index + ")");
    }

}
