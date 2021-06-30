package it.polimi.ingsw.configurator.nodes;

import it.polimi.ingsw.common.utils.Pair;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourceSelector extends VBox {

    private final IntegerProperty anyResources;
    private final IntegerProperty goldResources;
    private final IntegerProperty servantResources;
    private final IntegerProperty shieldResources;
    private final IntegerProperty stoneResources;

    private final BooleanProperty anyEnabled;
    private final StringProperty name;

    private Map<String, Pair<Label, TextField>> fields;

    public ResourceSelector() {
        this.anyResources = new SimpleIntegerProperty(this, "anyResources", 0);
        this.goldResources = new SimpleIntegerProperty(this, "goldResources", 0);
        this.servantResources = new SimpleIntegerProperty(this, "servantResources", 0);
        this.shieldResources = new SimpleIntegerProperty(this, "shieldResources", 0);
        this.stoneResources = new SimpleIntegerProperty(this, "stoneResources", 0);

        this.anyEnabled = new SimpleBooleanProperty(this, "anyEnabled", false);
        this.name = new SimpleStringProperty(this, "name", "Resources:");

        setup();
    }

    public ResourceSelector(String name, boolean anyEnabled) {
        this.anyResources = new SimpleIntegerProperty(this, "anyResources", 0);
        this.goldResources = new SimpleIntegerProperty(this, "goldResources", 0);
        this.servantResources = new SimpleIntegerProperty(this, "servantResources", 0);
        this.shieldResources = new SimpleIntegerProperty(this, "shieldResources", 0);
        this.stoneResources = new SimpleIntegerProperty(this, "stoneResources", 0);

        this.anyEnabled = new SimpleBooleanProperty(this, "anyEnabled", anyEnabled);
        this.name = new SimpleStringProperty(this, "name", name);

        setup();
    }

    private TextField createGenericTextField(IntegerProperty boundProperty) {
        TextField textField = new TextField("0");
        textField.setPrefHeight(25d);
        textField.setPrefWidth(50d);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*"))
                textField.setText(newValue.replaceAll("[^\\d]", ""));

            if(textField.getText().equals(""))
                boundProperty.set(0);
            else
                boundProperty.set(Integer.parseInt(textField.getText()));
        });

        return textField;
    }

    private Label createGenericLabel(String text) {
        Label label = new Label(text);
        label.setPrefWidth(50d);

        return label;
    }

    private void setup() {
        fields = new HashMap<>();

        if(isAnyEnabled())
            fields.put("any", new Pair<>(createGenericLabel("Any:"), createGenericTextField(anyResources)));

        fields.put("gold", new Pair<>(createGenericLabel("Gold:"), createGenericTextField(goldResources)));
        fields.put("servant", new Pair<>(createGenericLabel("Servant:"), createGenericTextField(servantResources)));
        fields.put("shield", new Pair<>(createGenericLabel("Shield:"), createGenericTextField(shieldResources)));
        fields.put("stone", new Pair<>(createGenericLabel("Stone:"), createGenericTextField(stoneResources)));

        anyResources.addListener((observedValue, oldValue, newValue) -> fields.get("any").getSecond().setText(String.valueOf(newValue)));

        goldResources.addListener((observedValue, oldValue, newValue) -> fields.get("gold").getSecond().setText(String.valueOf(newValue)));
        servantResources.addListener((observedValue, oldValue, newValue) -> fields.get("servant").getSecond().setText(String.valueOf(newValue)));
        shieldResources.addListener((observedValue, oldValue, newValue) -> fields.get("shield").getSecond().setText(String.valueOf(newValue)));
        stoneResources.addListener((observedValue, oldValue, newValue) -> fields.get("stone").getSecond().setText(String.valueOf(newValue)));

        Label label = new Label(getName());
        label.setPrefWidth(100d);
        label.setAlignment(Pos.CENTER);
        name.addListener((observedValue, oldValue, newValue) -> label.setText(newValue));
        this.getChildren().add(label);

        HBox hBox;

        for(String res : fields.keySet().stream().sorted().collect(Collectors.toList())) {
            hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPrefHeight(40d);
            hBox.setSpacing(20d);

            hBox.getChildren().add(fields.get(res).getFirst());
            hBox.getChildren().add(fields.get(res).getSecond());

            this.getChildren().add(hBox);
        }
    }

    public Map<String, Integer> getResourceMap() {
        Map<String, Integer> map = new HashMap<>();

        if(getAnyResources() != 0)
            map.put("any", getAnyResources());

        if(getGoldResources() != 0)
            map.put("gold", getGoldResources());

        if(getServantResources() != 0)
            map.put("servant", getServantResources());

        if(getShieldResources() != 0)
            map.put("shield", getShieldResources());

        if(getStoneResources() != 0)
            map.put("stone", getStoneResources());

        return map;
    }

    public void setResourceMap(Map<String, Integer> resources) {
        for(String res : resources.keySet())
            fields.get(res).getSecond().setText(String.valueOf(resources.get(res)));
    }

    public int getAnyResources() {
        return anyResources.get();
    }

    public IntegerProperty anyResourcesProperty() {
        return anyResources;
    }

    public int getGoldResources() {
        return goldResources.get();
    }

    public IntegerProperty goldResourcesProperty() {
        return goldResources;
    }

    public int getServantResources() {
        return servantResources.get();
    }

    public IntegerProperty servantResourcesProperty() {
        return servantResources;
    }

    public int getShieldResources() {
        return shieldResources.get();
    }

    public IntegerProperty shieldResourcesProperty() {
        return shieldResources;
    }

    public int getStoneResources() {
        return stoneResources.get();
    }

    public IntegerProperty stoneResourcesProperty() {
        return stoneResources;
    }

    public boolean isAnyEnabled() {
        return anyEnabled.get();
    }

    public BooleanProperty anyEnabledProperty() {
        return anyEnabled;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setAnyResources(int anyResources) {
        this.anyResources.set(anyResources);
    }

    public void setGoldResources(int goldResources) {
        this.goldResources.set(goldResources);
    }

    public void setServantResources(int servantResources) {
        this.servantResources.set(servantResources);
    }

    public void setShieldResources(int shieldResources) {
        this.shieldResources.set(shieldResources);
    }

    public void setStoneResources(int stoneResources) {
        this.stoneResources.set(stoneResources);
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
