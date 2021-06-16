package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HResourceContainer extends HBox {
    private final StringProperty containerJSON;
    private final ObjectProperty<RawStorage> rawStorage;

    private final BooleanProperty hideIfEmpty;
    private final BooleanProperty showResourceIfZero;
    private final BooleanProperty showX;

    private final BooleanProperty anyAccepted;

    @FXML
    private HBox box;

    private final List<String> ACCEPTED_RESOURCES = new ArrayList<>(Arrays.asList("gold", "servant", "shield", "stone"));

    public HResourceContainer() {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLoader.getResource("jfx/custom/HResourceContainer.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Unable to load custom element '" + getClass().getSimpleName() + "': " + exception);
        }

        this.containerJSON = new SimpleStringProperty(this, "containerJSON", "{\"id\":\"container\",\"resources\":{}}");
        this.rawStorage = new SimpleObjectProperty<>(this, "rawStorage", new RawStorage("container", new HashMap<>()));

        this.hideIfEmpty = new SimpleBooleanProperty(this, "hideIfEmpty", false);
        this.showResourceIfZero = new SimpleBooleanProperty(this, "showResourceIfZero", true);
        this.showX = new SimpleBooleanProperty(this, "showX", true);
        this.anyAccepted = new SimpleBooleanProperty(this, "anyAccepted", false);

        this.containerJSON.addListener((observableValue, oldValue, newValue) -> {
            try {
                setRawStorage(JSONParser.parseToRaw(newValue, RawStorage.class));
            } catch(IllegalRawConversionException | ParserException e) {
                throw new IllegalArgumentException("Conversion from JSON to RawStorage failed unexpectedly");
            }
        });

        this.rawStorage.addListener((observableValue, oldValue, newValue) -> setContainerJSON(newValue.toString()));

        update();
    }

    private void update() {
        box.getChildren().clear();

        List<String> resources = new ArrayList<>(ACCEPTED_RESOURCES);
        int tot = 0;

        if(isAnyAccepted())
            resources.add("any");

        for(String resource : resources) {
            if(getRawStorage().getResources().containsKey(resource) && getRawStorage().getResources().get(resource) != 0) {
                box.getChildren().add(new ResourceBox(resource, getRawStorage().getResources().get(resource), true, true, isShowX()));
                tot++;
            }
            else if(isShowResourceIfZero()) {
                box.getChildren().add(new ResourceBox(resource, 0, true, true, isShowX()));
            }
        }

        box.setVisible(!isHideIfEmpty() || tot != 0);
    }

    /* PROPERTIES */
    public StringProperty containerJSONProperty() {
        return containerJSON;
    }

    public ObjectProperty<RawStorage> rawStorageProperty() {
        return rawStorage;
    }

    public BooleanProperty hideIfEmptyProperty() {
        return hideIfEmpty;
    }

    public BooleanProperty showResourceIfZeroProperty() {
        return showResourceIfZero;
    }

    public BooleanProperty showXProperty() {
        return showX;
    }

    public BooleanProperty anyAcceptedProperty() {
        return anyAccepted;
    }


    /* GETTERS */
    public String getContainerJSON() {
        return containerJSON.get();
    }

    public RawStorage getRawStorage() {
        return rawStorage.get();
    }

    public boolean isHideIfEmpty() {
        return hideIfEmpty.get();
    }

    public boolean isShowResourceIfZero() {
        return showResourceIfZero.get();
    }

    public boolean isShowX() {
        return showX.get();
    }

    public boolean isAnyAccepted() {
        return anyAccepted.get();
    }


    /* SETTERS */

    public void setContainerJSON(String containerJSON) {
        this.containerJSON.set(containerJSON);
        update();
    }

    public void setRawStorage(RawStorage rawStorage) {
        this.rawStorage.set(rawStorage);
        update();
    }

    public void setHideIfEmpty(boolean hideIfEmpty) {
        this.hideIfEmpty.set(hideIfEmpty);
        update();
    }

    public void setShowResourceIfZero(boolean showResourceIfZero) {
        this.showResourceIfZero.set(showResourceIfZero);
        update();
    }

    public void setShowX(boolean showX) {
        this.showX.set(showX);
        update();
    }

    public void setAnyAccepted(boolean anyAccepted) {
        this.anyAccepted.set(anyAccepted);
        update();
    }
}
