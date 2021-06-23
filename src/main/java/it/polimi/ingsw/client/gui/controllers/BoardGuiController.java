package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.beans.ShopSelectionBean;
import it.polimi.ingsw.client.gui.dialogs.ChooseCraftingDialog;
import it.polimi.ingsw.client.gui.dialogs.CustomDialog;
import it.polimi.ingsw.client.gui.events.*;
import it.polimi.ingsw.client.gui.nodes.*;
import it.polimi.ingsw.client.gui.updaters.FaithPathGuiUpdater;
import it.polimi.ingsw.client.gui.updaters.MarketGuiUpdater;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.client.model.ConversionOption;
import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.gamematerials.LevelFlag;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.parser.raw.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.util.*;

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
    public ScoreboardBox scoreboard;

    public PlayerNode currentPlayerNode;
    private Map<String, PlayerNode> playerNodes;

    public void initialize() {
        List<List<ConversionOption>> testOptions = new ArrayList<>();
        testOptions.add(
                new ArrayList<>(){{
                    add(new ConversionOption(
                            Arrays.asList("gold", "shield"), 0
                    ));
                }}
        );
        testOptions.add(
                new ArrayList<>(){{
                    add(new ConversionOption(
                            Arrays.asList("servant"), 2
                    ));
                    add(new ConversionOption(
                            Arrays.asList("stone", "shield"), 1
                    ));
                    add(new ConversionOption(
                            Arrays.asList("stone", "shield"), 1
                    ));
                }}
        );
        testOptions.add(
                new ArrayList<>(){{
                    add(new ConversionOption(
                            Arrays.asList("gold", "shield"), 0
                    ));
                }}
        );
        testOptions.add(
                new ArrayList<>(){{
                    add(new ConversionOption(
                            Arrays.asList("gold", "shield"), 0
                    ));
                }}
        );

        List<MarbleColor> testSelectedMarbles = Arrays.asList(MarbleColor.BLUE, MarbleColor.YELLOW, MarbleColor.BLUE, MarbleColor.GREY);

        market.setup(3, 4);
        market.setRawMarket(new Market().toRaw());
        market.updateConversions(testOptions, testSelectedMarbles);

        scoreboard.setPlayerFlagsProperty(0, Arrays.asList(
                new RawLevelFlag(new LevelFlag(FlagColor.BLUE, 1)),
                new RawLevelFlag(new LevelFlag(FlagColor.GREEN, 1)),
                new RawLevelFlag(new LevelFlag(FlagColor.YELLOW, 1)),
                new RawLevelFlag(new LevelFlag(FlagColor.PURPLE, 1)),
                new RawLevelFlag(new LevelFlag(FlagColor.BLUE, 2)),
                new RawLevelFlag(new LevelFlag(FlagColor.GREEN, 2)),
                new RawLevelFlag(new LevelFlag(FlagColor.YELLOW, 2))
        ));

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

        // TODO: change to
        // setActivePlayer(getModel().getPersonalData().getUsername());
        setActivePlayer(getModel().getPlayers().get(0).getUsername());

        // TODO: ADD ALL GLOBAL UPDATERS
        // new ShopGuiUpdater(shop, getModel().getShop());
        new MarketGuiUpdater(market, getModel().getMarket());

        // TODO: ADD ALL PLAYER SPECIFIC UPDATERS
        for(int i = 0; i < getModel().getPlayers().size(); i++) {
            new FaithPathGuiUpdater(faithPath, getModel().getPlayers().get(i).getFaithPath(), i + 1);
        }

        changeGlobalNodesControlsStatus(false);
        changePlayerNodeControlsStatus(currentPlayerNode, false);

    }

    public void setActivePlayer(String username) {
        AnchorPane pane = ((AnchorPane) getSceneManager().getActiveScene().getRoot());

        if(currentPlayerNode == null) {
            currentPlayerNode = new PlayerNode();
            pane.getChildren().add(0, currentPlayerNode);
        }
        else {
            currentPlayerNode = playerNodes.get(username);
            pane.getChildren().set(0, currentPlayerNode);
        }

        currentPlayerNode.setLayoutX(14d);
        currentPlayerNode.setLayoutY(342d);
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
