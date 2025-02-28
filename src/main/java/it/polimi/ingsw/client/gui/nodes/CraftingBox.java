package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.common.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.JSONSerializer;
import it.polimi.ingsw.common.parser.raw.RawCrafting;
import it.polimi.ingsw.common.parser.raw.RawStorage;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.controlsfx.glyphfont.Glyph;

import java.util.HashMap;

public class CraftingBox extends VBox {
    private final StringProperty craftingJSON;
    private final ObjectProperty<RawCrafting> rawCrafting;

    private final IntegerProperty level;

    private Label levelLabel;
    private VResourceContainer input;
    private VResourceContainer output;
    private ResourceBox faith;

    public CraftingBox() {


        String defaultJSON = "{\"input\":{\"any\":1},\"output\":{\"any\":1},\"faith_output\":0}";
        RawCrafting defaultRawCrafting;
        try {
            defaultRawCrafting = JSONParser.parseToRaw(defaultJSON, RawCrafting.class);
        } catch (ParserException | IllegalRawConversionException e) {
            throw new IllegalArgumentException("Conversion from JSON to RawCrafting failed unexpectedly");
        }

        this.craftingJSON = new SimpleStringProperty(this, "craftingJSON", defaultJSON);
        this.rawCrafting = new SimpleObjectProperty<>(this, "rawCrafting", defaultRawCrafting);
        this.level = new SimpleIntegerProperty(this, "level", 0);

        this.craftingJSON.addListener((observableValue, oldValue, newValue) -> {
            try {
                setRawCrafting(JSONParser.parseToRaw(newValue, RawCrafting.class));
            } catch (ParserException | IllegalRawConversionException e) {
                throw new IllegalArgumentException("Conversion from JSON to RawCrafting failed unexpectedly");
            }
        });

        this.rawCrafting.addListener((observableValue, oldValue, newValue) -> setCraftingJSON(newValue.toString()));
        attachElements();
    }

    public CraftingBox(RawCrafting rawCrafting){
        String defaultJSON = JSONSerializer.toJson(rawCrafting);

        this.craftingJSON = new SimpleStringProperty(this, "craftingJSON", defaultJSON);
        this.rawCrafting = new SimpleObjectProperty<>(this, "rawCrafting", rawCrafting);
        this.level = new SimpleIntegerProperty(this, "level", 0);

        this.craftingJSON.addListener((observableValue, oldValue, newValue) -> {
            try {
                setRawCrafting(JSONParser.parseToRaw(newValue, RawCrafting.class));
            } catch (ParserException | IllegalRawConversionException e) {
                throw new IllegalArgumentException("Conversion from JSON to RawCrafting failed unexpectedly");
            }
        });

        this.rawCrafting.addListener((observableValue, oldValue, newValue) -> setCraftingJSON(newValue.toString()));
        attachElements();

    }

    private void attachElements(){
        RawStorage inputStorage;
        boolean inputStorageVisible;

        RawStorage outputStorage;
        boolean outputStorageVisible;

        int faithValue = getRawCrafting().getFaithOutput();

        if(getRawCrafting().getInput().size() == 0) {
            inputStorage = new RawStorage("input", new HashMap<>() {{ put("any", 1); }});
            inputStorageVisible = false;
        }
        else {
            inputStorage = new RawStorage("input", getRawCrafting().getInput());
            inputStorageVisible = true;
        }

        if(getRawCrafting().getOutput().size() == 0) {
            outputStorage = new RawStorage("output", new HashMap<>() {{ put("any", 1); }});
            outputStorageVisible = false;
        }
        else {
            outputStorage = new RawStorage("output", getRawCrafting().getOutput());
            outputStorageVisible = true;
        }

        levelLabel = new Label();
        levelLabel.setText("Level: " + levelProperty().get());
        levelLabel.setFont(new Font(22d));
        levelLabel.setVisible(getLevel() > 0);

        HBox hBox = new HBox();
        hBox.setSpacing(20d);

        input = new VResourceContainer(inputStorage, false, false, true, true);
        input.setVisible(inputStorageVisible);

        Label label = new Label();
        Glyph glyph = new Glyph("FontAwesome", "ARROW_RIGHT");
        glyph.setScaleX(1.5);
        glyph.setScaleY(1.5);
        label.setGraphic(glyph);

        output = new VResourceContainer(outputStorage, false, false, true, true);
        output.setVisible(outputStorageVisible);
        hBox.getChildren().addAll(input, label, output);
        hBox.setAlignment(Pos.CENTER);

        faith = new ResourceBox("faith", faithValue, true, false, true);
        faith.setAlignment(Pos.BOTTOM_RIGHT);
        faith.setPadding(new Insets(20, 0, 0, 0));

        this.getChildren().addAll(levelLabel, hBox, faith);

    }

    private void update() {
        RawStorage inputStorage;
        boolean inputStorageVisible;

        RawStorage outputStorage;
        boolean outputStorageVisible;

        int faithValue = getRawCrafting().getFaithOutput();

        if(getRawCrafting().getInput().size() == 0) {
            inputStorage = new RawStorage("input", new HashMap<>() {{ put("any", 1); }});
            inputStorageVisible = false;
        }
        else {
            inputStorage = new RawStorage("input", getRawCrafting().getInput());
            inputStorageVisible = true;
        }

        if(getRawCrafting().getOutput().size() == 0) {
            outputStorage = new RawStorage("output", new HashMap<>() {{ put("any", 1); }});
            outputStorageVisible = false;
        }
        else {
            outputStorage = new RawStorage("output", getRawCrafting().getOutput());
            outputStorageVisible = true;
        }

        input.setRawStorage(inputStorage);
        input.setVisible(inputStorageVisible);
        output.setRawStorage(outputStorage);
        output.setVisible(outputStorageVisible);
        faith.setAmount(faithValue);

        levelLabel.setText("Level: " + getLevel());
        levelLabel.setVisible(getLevel() > 0);
    }

    /* PROPERTIES */
    public StringProperty craftingJSONProperty() {
        return craftingJSON;
    }

    public ObjectProperty<RawCrafting> rawCraftingProperty() {
        return rawCrafting;
    }

    public IntegerProperty levelProperty() {
        return level;
    }


    /* GETTERS */
    public String getCraftingJSON() {
        return craftingJSON.get();
    }

    public RawCrafting getRawCrafting() {
        return rawCrafting.get();
    }

    public int getLevel() {
        return level.get();
    }


    /* SETTERS */
    public void setCraftingJSON(String craftingJSON) {
        this.craftingJSON.set(craftingJSON);
        update();
    }

    public void setRawCrafting(RawCrafting rawCrafting) {
        if(!rawCraftingProperty().get().getInput().equals(rawCrafting.getInput())
        || !rawCraftingProperty().get().getOutput().equals(rawCrafting.getOutput())
        || rawCraftingProperty().get().getFaithOutput() != rawCrafting.getFaithOutput()) {
            this.rawCrafting.set(rawCrafting);
            update();
        }
    }

    public void setLevel(int level) {
        if(levelProperty().get() != level) {
            this.level.set(level);
            update();
        }
    }

    public void setUpgradableRawCrafting(RawCrafting rawCrafting, int level){
        if(!rawCraftingProperty().get().getInput().equals(rawCrafting.getInput())
        || !rawCraftingProperty().get().getOutput().equals(rawCrafting.getOutput())
        || rawCraftingProperty().get().getFaithOutput() != rawCrafting.getFaithOutput()
        || levelProperty().get() != level){
            this.rawCrafting.set(rawCrafting);
            this.level.set(level);
            update();
        }
    }
}
