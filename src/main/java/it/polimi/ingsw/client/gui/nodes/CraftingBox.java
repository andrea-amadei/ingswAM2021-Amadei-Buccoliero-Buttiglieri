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
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;

public class CraftingBox extends VBox {
    private final StringProperty craftingJSON;
    private final ObjectProperty<RawCrafting> rawCrafting;

    private final IntegerProperty level;

    @FXML
    private Label levelLabel;

    @FXML
    private VResourceContainer input;
    @FXML
    private VResourceContainer output;
    @FXML
    private ResourceBox faith;

    public CraftingBox() {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLoader.getResource("jfx/custom/CraftingBox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException("Unable to load custom element '" + getClass().getSimpleName() + "': " + exception);
        }

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
        this.rawCrafting.set(rawCrafting);
        update();
    }

    public void setLevel(int level) {
        this.level.set(level);
        update();
    }
}
