package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlagBox extends HBox {
    private final StringProperty flag;
    private final IntegerProperty level;
    private final IntegerProperty amount;
    private final BooleanProperty showAmount;
    private final BooleanProperty showIfZero;
    private final BooleanProperty showX;


    public ImageView imageView;


    public Label levelLabel;


    public Label label;


    public HBox box;

    private final List<String> ACCEPTED_COLORS = new ArrayList<>(Arrays.asList("blue", "green", "purple", "yellow"));
    private final List<Double> COLOR_LABEL_OFFSET_TOP = new ArrayList<>(Arrays.asList(-15d, -15d, -15d, -15d));
    private final List<Double> COLOR_LABEL_OFFSET_LEFT = new ArrayList<>(Arrays.asList(0d, 0d, 0d, 0d));
    private final List<String> FLAG_TEXTURE_PATH = new ArrayList<>(Arrays.asList(
            "assets/flags/blue_flag.png",
            "assets/flags/green_flag.png",
            "assets/flags/purple_flag.png",
            "assets/flags/yellow_flag.png"
    ));

    private final List<String> EXTENDED_LEVELS = new ArrayList<>(Arrays.asList("I", "II", "III"));

    public FlagBox() {

        attachElements();

        this.flag = new SimpleStringProperty(this, "flag", "blue");
        this.level = new SimpleIntegerProperty(this, "level", 0);
        this.amount = new SimpleIntegerProperty(this, "amount", 0);
        this.showAmount = new SimpleBooleanProperty(this, "showAmount", true);
        this.showIfZero = new SimpleBooleanProperty(this, "showIfZero", true);
        this.showX = new SimpleBooleanProperty(this, "showX", true);

        updateLabel();
        updateFlag();
    }

    public FlagBox(String flag, int level, int amount) {

        attachElements();

        this.flag = new SimpleStringProperty(this, "flag", flag);
        this.level = new SimpleIntegerProperty(this, "level", level);
        this.amount = new SimpleIntegerProperty(this, "amount", amount);
        this.showAmount = new SimpleBooleanProperty(this, "showAmount", true);
        this.showIfZero = new SimpleBooleanProperty(this, "showIfZero", true);
        this.showX = new SimpleBooleanProperty(this, "showX", true);

        updateLabel();
        updateFlag();
    }

    public FlagBox(String flag, int level, int amount, boolean showAmount, boolean showIfZero, boolean showX) {
        attachElements();

        this.flag = new SimpleStringProperty(this, "flag", flag);
        this.level = new SimpleIntegerProperty(this, "level", level);
        this.amount = new SimpleIntegerProperty(this, "amount", amount);
        this.showAmount = new SimpleBooleanProperty(this, "showAmount", showAmount);
        this.showIfZero = new SimpleBooleanProperty(this, "showIfZero", showIfZero);
        this.showX = new SimpleBooleanProperty(this, "showX", showX);

        updateLabel();
        updateFlag();
    }

    private void attachElements(){
        StackPane stackPane = new StackPane();

        imageView = new ImageView();
        imageView.setFitHeight(40d);
        imageView.setFitWidth(40d);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        levelLabel = new Label();
        levelLabel.setFont(new Font("Algerian", 24d));

        stackPane.getChildren().addAll(imageView, levelLabel);

        label = new Label();
        label.setMaxHeight(Double.NEGATIVE_INFINITY);
        label.setMinHeight(Double.NEGATIVE_INFINITY);
        label.setMaxWidth(Double.NEGATIVE_INFINITY);
        label.setMinWidth(Double.NEGATIVE_INFINITY);
        label.setPrefHeight(40d);
        label.setFont(new Font(22d));

        this.getChildren().addAll(stackPane, label);
        box = this;
    }

    private void updateLabel() {
        if(isShowX())
            label.setText("X " + getAmount());
        else
            label.setText(String.valueOf(getAmount()));

        label.setVisible(isShowAmount());
        box.setVisible(isShowIfZero() || getAmount() != 0);
    }

    private void updateFlag() {
        int colorId;

        if(!ACCEPTED_COLORS.contains(getFlag()))
            throw new IllegalArgumentException("Invalid resource: " + flag);

        colorId = ACCEPTED_COLORS.indexOf(getFlag());

        imageView.setImage(ResourceLoader.loadImage(FLAG_TEXTURE_PATH.get(colorId)));

        if(getLevel() > 0) {
            levelLabel.setVisible(true);
            StackPane.setMargin(levelLabel, new Insets(COLOR_LABEL_OFFSET_TOP.get(colorId), 0d, 0d, COLOR_LABEL_OFFSET_LEFT.get(colorId)));

            if(getLevel() > EXTENDED_LEVELS.size())
                levelLabel.setText(String.valueOf(getLevel()));
            else
                levelLabel.setText(EXTENDED_LEVELS.get(getLevel() - 1));
        }
        else {
            levelLabel.setVisible(false);
        }
    }

    /* PROPERTIES */
    public StringProperty flagProperty() {
        return flag;
    }

    public IntegerProperty levelProperty() {
        return level;
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
    public String getFlag() {
        return flagProperty().get();
    }

    public int getLevel() {
        return levelProperty().get();
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
    public void setFlag(String flag) {
        if(!flagProperty().get().equals(flag)) {
            flagProperty().set(flag);
            updateFlag();
        }
    }

    public void setLevel(int level) {
        if(levelProperty().get() != level) {
            this.level.set(level);
            updateFlag();
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
