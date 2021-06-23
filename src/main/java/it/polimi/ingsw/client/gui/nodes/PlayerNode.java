package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.FXMLCachedLoaders;
import it.polimi.ingsw.client.model.ClientLeaderCards;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.leader.*;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.parser.raw.RawLeaderCard;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PlayerNode extends AnchorPane {

    @FXML
    private CupboardBox cupboard;
    @FXML
    private HResourceContainer chest;
    @FXML
    private VResourceContainer hand;
    @FXML
    private VResourceContainer basket;
    @FXML
    private ProductionBox production;
    @FXML
    private LeaderCardSlotsBox leaders;

    public PlayerNode() {
        FXMLLoader fxmlLoader;
        String fileName = "jfx/custom/PlayerNode.fxml";
        if(FXMLCachedLoaders.getInstance().isLoaderContained(fileName)) {
            fxmlLoader = FXMLCachedLoaders.getInstance().getLoader(fileName);
        }else{
            fxmlLoader = new FXMLLoader(ResourceLoader.getResource(fileName));
            FXMLCachedLoaders.getInstance().addLoader(fileName, fxmlLoader);
        }
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Unable to load custom element '" + getClass().getSimpleName() + "': " + exception);
        }
    }

    public CupboardBox getCupboard() {
        return cupboard;
    }

    public HResourceContainer getChest() {
        return chest;
    }

    public VResourceContainer getHand() {
        return hand;
    }

    public VResourceContainer getBasket() {
        return basket;
    }

    public ProductionBox getProduction() {
        return production;
    }

    public LeaderCardSlotsBox getLeaders() {
        return leaders;
    }
}
