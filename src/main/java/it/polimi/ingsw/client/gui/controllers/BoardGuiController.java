package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.nodes.*;
import it.polimi.ingsw.client.gui.updaters.FaithPathGuiUpdater;
import it.polimi.ingsw.client.gui.updaters.MarketGuiUpdater;
import it.polimi.ingsw.client.gui.updaters.PersonalDataGuiUpdater;
import it.polimi.ingsw.client.gui.updaters.ShopGuiUpdater;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.client.model.ConversionOption;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.FlagColor;
import it.polimi.ingsw.gamematerials.LevelFlag;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.model.fsm.states.ConversionSelectionState;
import it.polimi.ingsw.model.holder.FaithHolder;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.*;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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


    public ScoreboardBox scoreboard;

    public PlayerNode currentPlayerNode;
    private Map<String, PlayerNode> playerNodes;

    public void initialize() {
        scoreboard = new ScoreboardBox();
        board.getChildren().add(scoreboard);

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

        // TODO: change to
        // setActivePlayer(getModel().getPersonalData().getUsername());
        setActivePlayer(getModel().getPlayers().get(0).getUsername());

        // TODO: ADD ALL GLOBAL UPDATERS
        // new ShopGuiUpdater(shop, getModel().getShop());
        new MarketGuiUpdater(market, getModel().getMarket());

        // TODO: ADD ALL PLAYER SPECIFIC UPDATERS
        System.out.println(getModel().getPlayers().size());
        for(int i = 0; i < getModel().getPlayers().size(); i++) {
            new FaithPathGuiUpdater(faithPath, getModel().getPlayers().get(i).getFaithPath(), i + 1);
            new PersonalDataGuiUpdater(scoreboard, getModel().getPersonalData());
        }
    }

    public void setActivePlayer(String username) {
        AnchorPane pane = ((AnchorPane) getSceneManager().getActiveScene().getRoot());

        if(currentPlayerNode == null) {
            currentPlayerNode = new PlayerNode();
            pane.getChildren().add(1, currentPlayerNode);
        }
        else {
            currentPlayerNode = playerNodes.get(username);
            pane.getChildren().set(1, currentPlayerNode);
        }

        currentPlayerNode.setLayoutX(14d);
        currentPlayerNode.setLayoutY(342d);
    }
}
