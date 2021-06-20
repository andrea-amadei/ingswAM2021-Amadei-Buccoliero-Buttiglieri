package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.nodes.*;
import it.polimi.ingsw.client.model.ConversionOption;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.gamematerials.MarbleColor;
import it.polimi.ingsw.model.fsm.states.ConversionSelectionState;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.production.CraftingCard;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawCrafting;
import it.polimi.ingsw.parser.raw.RawCraftingCard;
import it.polimi.ingsw.parser.raw.RawLeaderCard;
import it.polimi.ingsw.parser.raw.RawMarket;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.application.Platform;
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

        shopBox.setScaleX(0.5);
        shopBox.setScaleY(0.5);
        shopBox.setTranslateX(-250);
        shopBox.setTranslateY(-412);


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
                }}
        );

        List<MarbleColor> testSelectedMarbles = Arrays.asList(MarbleColor.BLUE, MarbleColor.YELLOW);

        market.setup(3, 4);
        market.setRawMarket(new Market().toRaw());
        market.updateConversions(testOptions, testSelectedMarbles);
        market.setTranslateY(600d);


        board.getChildren().addAll(shopBox, market);
    }
}
