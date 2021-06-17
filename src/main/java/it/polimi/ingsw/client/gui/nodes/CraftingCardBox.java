package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawCrafting;
import it.polimi.ingsw.parser.raw.RawCraftingCard;
import it.polimi.ingsw.parser.raw.RawStorage;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


public class CraftingCardBox extends VBox {

    private final StringProperty craftingCardJSON;
    private final ObjectProperty<RawCraftingCard> rawCraftingCard;

    private HResourceContainer cost;
    private FlagBox leftFlagBox;
    private FlagBox rightFlagBox;
    private CraftingBox craftingBox;
    private PointsBox victoryPoints;


    public CraftingCardBox(){
        attachElements();

        String defaultJSON = "{\"id\":1,\"color\":\"GREEN\",\"level\":1,\"points\":1,\"cost\":{\"shield\":2},\"crafting\":{\"input\":{\"stone\":1},\"output\":{},\"faith_output\":1}}";
        RawCraftingCard defaultRawCraftingCard;
        try {
            defaultRawCraftingCard = JSONParser.parseToRaw(defaultJSON, RawCraftingCard.class);
        } catch (ParserException | IllegalRawConversionException e) {
            throw new IllegalArgumentException("Conversion from JSON to RawCrafting failed unexpectedly");
        }

        this.craftingCardJSON = new SimpleStringProperty(this, "craftingCardJSON", defaultJSON);
        this.rawCraftingCard = new SimpleObjectProperty<>(this, "rawCraftingCard", defaultRawCraftingCard);

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

    private void attachElements(){
        //cost
        cost = new HResourceContainer();
        cost.setAlignment(Pos.BASELINE_CENTER);
        cost.setContainerJSON("{\"id\":\"container\",\"resources\":{\"gold\":1}}");
        cost.setHideIfEmpty(true);
        cost.setShowResourceIfZero(false);
        cost.setShowX(true);
        cost.setStyle("-fx-border-color: #5B3A29; -fx-border-insets: 5; -fx-border-style: hidden hidden solid none; -fx-border-width: 2;");
        cost.setPadding(new Insets(0d, 0d, 5d, 0d));

        //hBox with left flag, region, right flag
        HBox hBox = new HBox();

        leftFlagBox = new FlagBox();
        leftFlagBox.setAlignment(Pos.CENTER_LEFT);
        leftFlagBox.setPrefHeight(58d);
        leftFlagBox.setPrefWidth(0d);
        leftFlagBox.setShowAmount(false);
        leftFlagBox.setShowX(false);
        leftFlagBox.setPadding(new Insets(0d, 0d, 0d, 20d));
        HBox.setMargin(leftFlagBox, new Insets(0, 0, 0, 0));

        Region region1 = new Region();
        region1.setPrefHeight(58d);
        region1.setPrefWidth(44d);
        HBox.setHgrow(region1, Priority.ALWAYS);

        rightFlagBox = new FlagBox();
        rightFlagBox.setAlignment(Pos.CENTER);
        rightFlagBox.setPrefHeight(58d);
        rightFlagBox.setPrefWidth(39d);
        rightFlagBox.setShowAmount(false);
        rightFlagBox.setShowX(false);
        rightFlagBox.setPadding(new Insets(0d, 0d, 0d, 20d));

        hBox.getChildren().addAll(leftFlagBox, region1, rightFlagBox);

        //crafting box
        craftingBox = new CraftingBox();
        craftingBox.setCraftingJSON("{\"input\":{\"any\":1},\"output\":{\"any\":1, \"gold\":1, \"shield\":1, \"stone\":1, \"servant\":1},\"faith_output\":2}");
        craftingBox.setStyle("-fx-border-color: #5B3A29; -fx-border-width: 2;");
        VBox.setMargin(craftingBox, new Insets(10d, 10d, 10d, 10d));
        craftingBox.setPadding(new Insets(0d, 4d, 4d, 0d));

        //region
        Region region2 = new Region();
        VBox.setVgrow(region2, Priority.ALWAYS);

        //points box
        victoryPoints = new PointsBox();
        victoryPoints.setPrefWidth(343d);
        victoryPoints.setPadding(new Insets(0d, 0d, 5d, 0d));

        //putting it all together
        this.getChildren().addAll(cost, hBox, craftingBox, region2, victoryPoints);
        this.setPrefHeight(548d);
        this.setPrefWidth(343d);
        this.setSpacing(20d);
        this.setStyle("-fx-border-color: #CB1704; -fx-border-insets: 5; -fx-border-width: 4; -fx-background-color: #fff0d0;");
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

    public String getCraftingCardJSON(){return craftingCardJSON.get();}


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
