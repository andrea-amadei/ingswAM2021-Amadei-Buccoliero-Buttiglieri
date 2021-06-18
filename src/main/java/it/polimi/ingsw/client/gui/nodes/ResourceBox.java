package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.FXMLCachedLoaders;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceBox extends HBox {
    private final StringProperty resource;
    private final IntegerProperty amount;
    private final BooleanProperty showAmount;
    private final BooleanProperty showIfZero;
    private final BooleanProperty showX;

    public ImageView imageView;

    public Label label;

    public HBox box;

    private final List<String> ACCEPTED_RESOURCES = new ArrayList<>(Arrays.asList("any", "faith", "gold", "servant", "shield", "stone"));
    private final List<String> RESOURCES_TEXTURE_PATH = new ArrayList<>(Arrays.asList(
            "assets/resources/any.png",
            "assets/resources/faith.png",
            "assets/resources/gold.png",
            "assets/resources/servant.png",
            "assets/resources/shield.png",
            "assets/resources/stone.png"
    ));

    public ResourceBox() {
        this.resource = new SimpleStringProperty(this, "resource", "any");
        this.amount = new SimpleIntegerProperty(this, "amount", 0);
        this.showAmount = new SimpleBooleanProperty(this, "showAmount", true);
        this.showIfZero = new SimpleBooleanProperty(this, "showIfZero", true);
        this.showX = new SimpleBooleanProperty(this, "showX", true);

        attachElements();
    }

    public ResourceBox(String resource, int amount) {
        this.resource = new SimpleStringProperty(this, "resource", resource);
        this.amount = new SimpleIntegerProperty(this, "amount", amount);

        this.showAmount = new SimpleBooleanProperty(this, "showAmount", true);
        this.showIfZero = new SimpleBooleanProperty(this, "showIfZero", true);
        this.showX = new SimpleBooleanProperty(this, "showX", true);

        attachElements();
    }

    public ResourceBox(String resource, int amount, boolean showAmount, boolean showIfZero, boolean showX) {
        this.resource = new SimpleStringProperty(this, "resource", resource);
        this.amount = new SimpleIntegerProperty(this, "amount", amount);

        this.showAmount = new SimpleBooleanProperty(this, "showAmount", showAmount);
        this.showIfZero = new SimpleBooleanProperty(this, "showIfZero", showIfZero);
        this.showX = new SimpleBooleanProperty(this, "showX", showX);

        attachElements();
    }

    private void attachElements(){
        box = this;
        if(!ACCEPTED_RESOURCES.contains(getResource()))
            throw new IllegalArgumentException("Invalid resource: " + resource);
        imageView = new ImageView(ResourceLoader.loadImage(RESOURCES_TEXTURE_PATH.get(ACCEPTED_RESOURCES.indexOf(getResource()))));

        imageView.setFitHeight(40d);
        imageView.setFitWidth(40d);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        if(isShowX())
            label = new Label("X " + getAmount());
        else
            label = new Label(String.valueOf(getAmount()));
        label.setMinHeight(Double.NEGATIVE_INFINITY);
        label.setMinWidth(Double.NEGATIVE_INFINITY);
        label.setMaxHeight(Double.NEGATIVE_INFINITY);
        label.setMaxWidth(Double.NEGATIVE_INFINITY);
        label.setPrefHeight(40d);
        label.setFont(new Font(22d));

        box.setVisible(isShowIfZero() || getAmount() != 0);

        this.getChildren().addAll(imageView, label);
    }

    private void updateLabel() {
        if(isShowX())
            label.setText("X " + getAmount());
        else
            label.setText(String.valueOf(getAmount()));

        label.setVisible(isShowAmount());
        box.setVisible(isShowIfZero() || getAmount() != 0);
    }

    private void updateResource() {
        if(!ACCEPTED_RESOURCES.contains(getResource()))
            throw new IllegalArgumentException("Invalid resource: " + resource);

        imageView.setImage(ResourceLoader.loadImage(RESOURCES_TEXTURE_PATH.get(ACCEPTED_RESOURCES.indexOf(getResource()))));
    }

    /* PROPERTIES */
    public StringProperty resourceProperty() {
        return resource;
    }

    public IntegerProperty amountProperty() {
        return amount;
    }

    public BooleanProperty showAmountProperty() {
        return showAmount;
    }

    public BooleanProperty showIfZeroProperty() {
        return showIfZero;
    }

    public BooleanProperty showXProperty() {
        return showX;
    }


    /* GETTERS */
    public String getResource() {
        return resourceProperty().get();
    }

    public int getAmount() {
        return amountProperty().get();
    }

    public boolean isShowAmount() {
        return showAmountProperty().get();
    }

    public boolean isShowIfZero() {
        return showIfZeroProperty().get();
    }

    public boolean isShowX() {
        return showXProperty().get();
    }


    /* SETTERS */
    public void setResource(String resource) {
        if(!resourceProperty().get().equals(resource)) {
            resourceProperty().set(resource);
            updateResource();
        }
    }

    public void setAmount(int amount) {
        if(amountProperty().get() != amount) {
            amountProperty().set(amount);
            updateLabel();
        }
    }

    public void setShowAmount(boolean showAmount) {
        if(showAmountProperty().get() != showAmount) {
            showAmountProperty().set(showAmount);
            updateLabel();
        }
    }

    public void setShowIfZero(boolean showIfZero) {
        if(showIfZeroProperty().get() != showIfZero) {
            showIfZeroProperty().set(showIfZero);
            updateLabel();
        }
    }

    public void setShowX(boolean showX) {
        if(showXProperty().get() != showX) {
            showXProperty().set(showX);
            updateLabel();
        }
    }
}
