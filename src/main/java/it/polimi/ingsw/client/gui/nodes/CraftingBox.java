package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawCrafting;
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.controlsfx.glyphfont.Glyph;

import java.io.IOException;
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
        attachElements();

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

        update();
    }

    private void attachElements(){
        levelLabel = new Label();
        levelLabel.setText("Level: 1");
        levelLabel.setFont(new Font(22d));

        HBox hBox = new HBox();
        hBox.setSpacing(20d);

        input = new VResourceContainer();
        input.setAnyAccepted(true);
        input.setShowResourceIfZero(false);
        input.setShowX(false);

        Label label = new Label();
        Glyph glyph = new Glyph();
        glyph.setFontFamily("FontAwesome");
        glyph.setIcon("ARROW_RIGHT");
        glyph.setScaleX(1.5);
        glyph.setScaleY(1.5);
        label.setGraphic(glyph);

        output = new VResourceContainer();
        output.setAnyAccepted(true);
        output.setShowResourceIfZero(false);
        output.setShowX(false);

        hBox.getChildren().addAll(input, label, output);

        faith = new ResourceBox();
        faith.setAlignment(Pos.BOTTOM_RIGHT);
        faith.setResource("faith");
        faith.setShowIfZero(false);
        faith.setShowX(false);

        this.getChildren().addAll(levelLabel, hBox, faith);
        this.setSpacing(20d);

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
}
