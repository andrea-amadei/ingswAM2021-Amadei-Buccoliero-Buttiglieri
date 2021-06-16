package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawCrafting;
import it.polimi.ingsw.parser.raw.RawCraftingCard;
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CraftingCardBox extends VBox {

    private final StringProperty craftingCardJSON;
    private final ObjectProperty<RawCraftingCard> rawCraftingCard;

    @FXML
    private HResourceContainer cost;

    @FXML
    private FlagBox leftFlagBox;

    @FXML
    private FlagBox rightFlagBox;

    @FXML
    private CraftingBox craftingBox;

    @FXML
    private PointsBox victoryPoints;


    public CraftingCardBox(){

        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLoader.getResource("jfx/custom/CraftingCardBox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException("Unable to load custom element '" + getClass().getSimpleName() + "': " + exception);
        }

        String defaultJSON = "{\"id\":1,\"color\":\"GREEN\",\"level\":1,\"points\":1,\"cost\":{\"shield\":2},\"crafting\":{\"input\":{\"stone\":1},\"output\":{},\"faith_output\":1}}";
        RawCraftingCard defaultRawCraftingCard;
        try {
            defaultRawCraftingCard = JSONParser.parseToRaw(defaultJSON, RawCraftingCard.class);
        } catch (ParserException | IllegalRawConversionException e) {
            throw new IllegalArgumentException("Conversion from JSON to RawCrafting failed unexpectedly");
        }

        this.craftingCardJSON = new SimpleStringProperty(this, "craftingCardJSON", defaultJSON);
        this.rawCraftingCard = new SimpleObjectProperty<>(this, "rawCrafting", defaultRawCraftingCard);

        this.craftingCardJSON.addListener((observableValue, oldValue, newValue) -> {
            try {
                setRawCraftingCard(JSONParser.parseToRaw(newValue, RawCraftingCard.class));
            } catch (ParserException | IllegalRawConversionException e) {
                throw new IllegalArgumentException("Conversion from JSON to RawCrafting failed unexpectedly");
            }
        });

        this.rawCraftingCard.addListener((observableValue, oldValue, newValue) -> setCraftingCardJSON(newValue.toString()));

        update();
    }

    private void update(){
        RawStorage rawStorage = new RawStorage("cost", rawCraftingCard.get().getCost());
        String flagColor = rawCraftingCard.get().getFlag().name().toLowerCase();
        int flagLevel = rawCraftingCard.get().getLevel();
        RawCrafting rawCrafting = rawCraftingCard.get().getCrafting();
        int points = rawCraftingCard.get().getPoints();

        cost.setRawStorage(rawStorage);
        leftFlagBox.setFlag(flagColor);
        leftFlagBox.setLevel(flagLevel);
        rightFlagBox.setFlag(flagColor);
        rightFlagBox.setLevel(flagLevel);
        craftingBox.setRawCrafting(rawCrafting);
        victoryPoints.setPoints(points);

    }

    //properties
    public StringProperty craftingCardJSONProperty(){
        return craftingCardJSON;
    }

    public ObjectProperty<RawCraftingCard> rawCraftingCardProperty(){
        return rawCraftingCard;
    }


    //getters
    public RawCraftingCard getRawCraftingCard() {
        return rawCraftingCard.get();
    }

    //setters
    public void setCraftingCardJSON(String craftingCardJSON) {
        this.craftingCardJSON.set(craftingCardJSON);
        update();
    }

    public void setRawCraftingCard(RawCraftingCard rawCraftingCard) {
        this.rawCraftingCard.set(rawCraftingCard);
        update();
    }
}
