package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.FXMLCachedLoaders;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

public class VResourceContainer extends VBox implements ResourceContainer {
    private final StringProperty containerJSON;
    private final ObjectProperty<RawStorage> rawStorage;

    private final BooleanProperty hideIfEmpty;
    private final BooleanProperty showResourceIfZero;
    private final BooleanProperty showX;

    private final BooleanProperty anyAccepted;

    private final VBox box;

    private final List<String> ACCEPTED_RESOURCES = new ArrayList<>(Arrays.asList("gold", "servant", "shield", "stone"));
    private final Map<String, ResourceBox> resourceBoxes;

    public VResourceContainer() {
        box = this;
        this.setSpacing(5d);

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

        resourceBoxes = new HashMap<>();
        resourceBoxes.put("gold", new ResourceBox("gold", 0, true, showResourceIfZero.get(), true));
        resourceBoxes.put("servant", new ResourceBox("servant", 0, true, showResourceIfZero.get(), true));
        resourceBoxes.put("stone", new ResourceBox("stone", 0, true, showResourceIfZero.get(), true));
        resourceBoxes.put("shield", new ResourceBox("shield", 0, true, showResourceIfZero.get(), true));
        resourceBoxes.put("any", new ResourceBox("any", 0, true, showResourceIfZero.get(), true));
        update();
    }

    public VResourceContainer(RawStorage rawStorage, boolean hideIfEmpty, boolean showResourceIfZero, boolean showX, boolean anyAccepted){
        box = this;
        this.setSpacing(5d);
        this.containerJSON = new SimpleStringProperty(this, "containerJSON", JSONSerializer.toJson(rawStorage));
        this.rawStorage = new SimpleObjectProperty<>(this, "rawStorage", rawStorage);

        this.hideIfEmpty = new SimpleBooleanProperty(this, "hideIfEmpty", hideIfEmpty);
        this.showResourceIfZero = new SimpleBooleanProperty(this, "showResourceIfZero", showResourceIfZero);
        this.showX = new SimpleBooleanProperty(this, "showX", showX);
        this.anyAccepted = new SimpleBooleanProperty(this, "anyAccepted", anyAccepted);

        this.containerJSON.addListener((observableValue, oldValue, newValue) -> {

            try {
                setRawStorage(JSONParser.parseToRaw(newValue, RawStorage.class));
            } catch (IllegalRawConversionException | ParserException e) {
                throw new IllegalArgumentException("Conversion from JSON to RawStorage failed unexpectedly");
            }
        });


        this.rawStorage.addListener((observableValue, oldValue, newValue) -> setContainerJSON(newValue.toString()));

        resourceBoxes = new HashMap<>();
        resourceBoxes.put("gold", new ResourceBox("gold", 0, true, showResourceIfZero, true));
        resourceBoxes.put("servant", new ResourceBox("servant", 0, true, showResourceIfZero, true));
        resourceBoxes.put("stone", new ResourceBox("stone", 0, true, showResourceIfZero, true));
        resourceBoxes.put("shield", new ResourceBox("shield", 0, true, showResourceIfZero, true));
        resourceBoxes.put("any", new ResourceBox("any", 0, true, showResourceIfZero, true));

        update();
    }



    private void update() {
        List<String> resources = new ArrayList<>(ACCEPTED_RESOURCES);
        int tot = 0;

        if(isAnyAccepted())
            resources.add("any");

        List<ResourceBox> visibleNodes = new ArrayList<>();
        for(String resource : resources) {
            if(getRawStorage().getResources().containsKey(resource) && getRawStorage().getResources().get(resource) != 0) {
                ResourceBox toBeSeen = resourceBoxes.get(resource);
                toBeSeen.setAmount(getRawStorage().getResources().get(resource));
                toBeSeen.setShowX(isShowX());
                visibleNodes.add(toBeSeen);
                tot++;
            }
            else if(isShowResourceIfZero()) {
                ResourceBox toBeSeen = resourceBoxes.get(resource);
                toBeSeen.setAmount(0);
                toBeSeen.setShowX(isShowX());
                visibleNodes.add(toBeSeen);
            }
        }

        box.getChildren().setAll(visibleNodes);

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
    }

    public void setRawStorage(RawStorage rawStorage) {
        if(!rawStorageProperty().get().getResources().equals(rawStorage.getResources())) {
            this.rawStorage.set(rawStorage);
            update();
        }
    }

    public void setHideIfEmpty(boolean hideIfEmpty) {
        if(hideIfEmptyProperty().get() != hideIfEmpty) {
            this.hideIfEmpty.set(hideIfEmpty);
            update();
        }
    }

    public void setShowResourceIfZero(boolean showResourceIfZero) {
        if(showResourceIfZeroProperty().get() != showResourceIfZero) {
            this.showResourceIfZero.set(showResourceIfZero);
            update();
        }
    }

    public void setShowX(boolean showX) {
        if(showXProperty().get() != showX) {
            this.showX.set(showX);
            update();
        }
    }

    public void setAnyAccepted(boolean anyAccepted) {
        if(anyAcceptedProperty().get() != anyAccepted) {
            this.anyAccepted.set(anyAccepted);
            update();
        }
    }
}
