package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.nodes.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardGuiController extends BaseController {
    public AnchorPane board;


    //public MarketBox market;

    public void initialize() {

        /*List<List<ConversionOption>> testOptions = new ArrayList<>();
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
                }}
        );

        List<MarbleColor> testSelectedMarbles = Arrays.asList(MarbleColor.BLUE, MarbleColor.YELLOW);

        market.setup(3, 4);
        market.setScaleX(0.5);
        market.setScaleY(0.5);
        market.setRawMarket(new Market().toRaw());
        market.updateConversions(new ArrayList<>(), new ArrayList<>());

        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(()->{
                RawMarket testMarket = new Market().toRaw();
                market.setRawMarket(testMarket);
                market.updateConversions(testOptions, testSelectedMarbles);
            });
        }).start();

        new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(()->{
                RawMarket testMarket = new Market().toRaw();
                market.setRawMarket(testMarket);
                market.updateConversions(new ArrayList<>(), new ArrayList<>());
            });
        }).start();

        new Thread(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(()->{
                RawMarket testMarket = new Market().toRaw();
                market.setRawMarket(testMarket);
                market.updateConversions(testOptions, testSelectedMarbles);
            });
        }).start();*/

        /*LeaderCardBox leader;
        RawLeaderCard rawLeaderCard;
        try {
            rawLeaderCard = JSONParser.parseToRaw("{\"id\":2,\"name\":\"aaa\",\"points\":2,\"requirements\":[{\"type\":\"flag\",\"color\":\"BLUE\",\"amount\":1},{\"type\":\"flag\",\"color\":\"PURPLE\",\"amount\":1}],\"special_abilities\":[{\"type\":\"discount\",\"resource\":\"shield\",\"amount\":1}]}", RawLeaderCard.class);
        } catch (ParserException | IllegalRawConversionException e) {
            throw new NullPointerException(e.getMessage());
        }
        long startTime = System.nanoTime();
        for(int i = 0; i < 10; i++) {
            leader = new LeaderCardBox(rawLeaderCard, false);
            leader.setIsCovered(false);
            leader.setRawLeaderCard(rawLeaderCard);
            board.getChildren().add(leader);
            board.getChildren().add(new ShopBox());
        }
        long elapsedTime = System.nanoTime() - startTime;

        System.out.println("execution in milliseconds for full cards: " + elapsedTime / 1000000);

        startTime = System.nanoTime();
        leader = new LeaderCardBox();
        elapsedTime = System.nanoTime() - startTime;

        System.out.println("execution in milliseconds for emtpy card: " + elapsedTime / 1000000);
         */

        ShopBox shopBox = new ShopBox();
        MarketBox market = new MarketBox();

        shopBox.setScaleX(0.6);
        shopBox.setScaleY(0.6);
        AnchorPane.setLeftAnchor(shopBox, 1100d);
        AnchorPane.setTopAnchor(shopBox, -50d);


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
        AnchorPane.setLeftAnchor(market, 680d);
        AnchorPane.setTopAnchor(market, 175d);
        market.setScaleX(0.8);
        market.setScaleY(0.8);
        market.setPrefWidth(678d);
        market.setPrefHeight(465d);

        FaithPath faithPath = new FaithPath();

        faithPath.setPlayer1CheckpointsStatusProperty(FXCollections.observableArrayList(FaithHolder.CheckpointStatus.ACTIVE, FaithHolder.CheckpointStatus.INACTIVE, FaithHolder.CheckpointStatus.UNREACHED));
        faithPath.setPlayer2CheckpointsStatusProperty(FXCollections.observableArrayList(FaithHolder.CheckpointStatus.INACTIVE, FaithHolder.CheckpointStatus.INACTIVE, FaithHolder.CheckpointStatus.UNREACHED));
        faithPath.setPlayer3CheckpointsStatusProperty(FXCollections.observableArrayList(FaithHolder.CheckpointStatus.INACTIVE, FaithHolder.CheckpointStatus.INACTIVE, FaithHolder.CheckpointStatus.UNREACHED));
        faithPath.setPlayer4CheckpointsStatusProperty(FXCollections.observableArrayList(FaithHolder.CheckpointStatus.INACTIVE, FaithHolder.CheckpointStatus.INACTIVE, FaithHolder.CheckpointStatus.UNREACHED));
        faithPath.setPlayer1PositionsProperty(4);
        faithPath.setPlayer2PositionsProperty(7);
        faithPath.setPlayer3PositionsProperty(7);
        faithPath.setPlayer4PositionsProperty(8);


        AnchorPane.setLeftAnchor(faithPath, 550d);
        AnchorPane.setTopAnchor(faithPath, 0d);
        faithPath.setScaleX(0.7);
        faithPath.setScaleY(0.7);

        ScoreboardBox scoreboardBox = new ScoreboardBox();
        scoreboardBox.setPlayerFlagsProperty(0, Arrays.asList(
                new RawLevelFlag(new LevelFlag(FlagColor.BLUE, 1)),
                new RawLevelFlag(new LevelFlag(FlagColor.GREEN, 1)),
                new RawLevelFlag(new LevelFlag(FlagColor.YELLOW, 1)),
                new RawLevelFlag(new LevelFlag(FlagColor.PURPLE, 1)),
                new RawLevelFlag(new LevelFlag(FlagColor.BLUE, 2)),
                new RawLevelFlag(new LevelFlag(FlagColor.GREEN, 2)),
                new RawLevelFlag(new LevelFlag(FlagColor.YELLOW, 2))
        ));
        AnchorPane.setTopAnchor(scoreboardBox, 20d);
        AnchorPane.setLeftAnchor(scoreboardBox, 20d);

        CupboardBox cupboardBox = new CupboardBox();
        cupboardBox.setBaseResources(FXCollections.observableArrayList("gold", "stone", "shield"));
        cupboardBox.setBaseAmounts(FXCollections.observableArrayList(0, 1, 3));
        cupboardBox.addLeaderShelf("shield");
        cupboardBox.setLeaderShelf(0, "shield", 1);
        AnchorPane.setTopAnchor(cupboardBox, 200d);
        AnchorPane.setLeftAnchor(cupboardBox, -20d);
        cupboardBox.setScaleX(0.9);
        cupboardBox.setScaleY(0.9);

        VResourceContainer chest = new VResourceContainer();
        AnchorPane.setTopAnchor(chest, 300d);
        AnchorPane.setLeftAnchor(chest, 20d);
        chest.setScaleX(0.7);
        chest.setScaleY(0.7);

        board.getChildren().addAll(shopBox, market, faithPath, scoreboardBox, cupboardBox, chest);
    }
}
