package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.FXMLCachedLoaders;
import it.polimi.ingsw.client.gui.beans.ConversionSelectionBean;
import it.polimi.ingsw.client.gui.beans.MarketPickBean;
import it.polimi.ingsw.client.gui.events.ConversionSelectionEvent;
import it.polimi.ingsw.client.gui.events.MarketPickEvent;
import it.polimi.ingsw.client.clientmodel.ConversionOption;
import it.polimi.ingsw.server.model.basetypes.MarbleColor;
import it.polimi.ingsw.common.parser.raw.RawMarket;
import it.polimi.ingsw.common.utils.ResourceLoader;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MarketBox extends VBox {

    @FXML
    private GridPane marketGrid;

    @FXML
    private GridPane optionsGrid;

    private final IntegerProperty rowNum;
    private final IntegerProperty colNum;
    private final ObjectProperty<RawMarket> rawMarket;
    private final ObjectProperty<List<MarbleColor>> selectedMarbleColors;
    private final ObjectProperty<List<List<ConversionOption>>> conversionOptions;
    private final BooleanProperty areControlsDisabled;

    private MarbleBox[][] graphicGrid;
    private MarbleBox oddMarbleBox;
    private final List<List<HBox>> selectableOptionBoxes;
    private final List<Integer> selectedOptions;
    private final List<Button> buttons;

    public MarketBox(){
        FXMLLoader fxmlLoader;
        String fileName = "jfx/custom/MarketBox.fxml";
        if(FXMLCachedLoaders.getInstance().isLoaderContained(fileName)){
            fxmlLoader = FXMLCachedLoaders.getInstance().getLoader(fileName);
        }else{
            fxmlLoader = new FXMLLoader(ResourceLoader.getResource(fileName));
            FXMLCachedLoaders.getInstance().addLoader(fileName, fxmlLoader);
        }
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Unable to load custom element '" + getClass().getSimpleName() + "': " + exception);
        }

        rowNum = new SimpleIntegerProperty(this, "rows", -1);
        colNum = new SimpleIntegerProperty(this, "columns", -1);
        rawMarket = new SimpleObjectProperty<>(this, "rawMarket", null);
        selectedMarbleColors = new SimpleObjectProperty<>(this, "selectedMarbles", new ArrayList<>());
        conversionOptions = new SimpleObjectProperty<>(this, "conversionOptions", new ArrayList<>());
        buttons = new ArrayList<>();

        areControlsDisabled = new SimpleBooleanProperty(this, "areControlsDisabled", true);
        areControlsDisabled.addListener((b, oldValue, newValue) -> {
            for(Button btn : buttons){
                btn.setDisable(newValue);
                btn.setVisible(!newValue);
            }
        });
        selectableOptionBoxes = new ArrayList<>();
        selectedOptions = new ArrayList<>();

        setup(3, 4);
    }

    private void updateGrid(){
        //draw the odd marble
        oddMarbleBox.setColor(rawMarket.get().getOdd().name().toLowerCase());
        //draw the grid
        for(int i = 0; i < rowNum.get(); i++){
            for(int j = 0; j < colNum.get(); j++){
                graphicGrid[i][j].setColor(rawMarket.get().getMarbles().get(i * colNum.get() + j).name().toLowerCase());
            }
        }
    }

    //called only if the options where empty and after an update they were updated.
    //the grid pane is empty when this method is called
    private void updateOptions(){
        //add all the labels
        int maxOptionLength = conversionOptions.get().stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

        for(int i = 0; i < maxOptionLength; i++){
            Label label = new Label(String.valueOf(i + 1));
            label.setFont(new Font("Algerian", 22d));
            optionsGrid.add(label, 0, i+1);
        }

        //add all the selected marbles
        for(int i = 0; i < selectedMarbleColorsProperty().get().size(); i++){
            optionsGrid.add(new MarbleBox(selectedMarbleColorsProperty().get().get(i).name().toLowerCase()), i + 1, 0);
        }

        //add all the options and store the selectable HBoxes
        for(int i = 0; i < conversionOptions.get().size(); i++){
            selectableOptionBoxes.add(new ArrayList<>());
            selectedOptions.add(0);

            for(int j = 0; j < conversionOptions.get().get(i).size(); j++) {
                //add the selectable HBox with the associated listener
                HBox cell = new HBox();
                optionsGrid.add(cell, i + 1, j +1);
                selectableOptionBoxes.get(i).add(cell);

                cell.setOnMousePressed(this::handleOptionSelection);

                if(j == 0){
                    cell.setStyle("-fx-background-color: red");
                }

                //add each resource in the option
                ConversionOption currentOption = conversionOptions.get().get(i).get(j);
                for(int k = 0; k < currentOption.getResources().size(); k++){
                    cell.getChildren().add(new ResourceBox(currentOption.getResources().get(k).toLowerCase(), 0, false, true, false));
                }

                //add the faith
                cell.getChildren().add(new ResourceBox("faith", currentOption.getFaithPoints(), true, false, false));
            }
        }

        //add the button to confirm
        Button confirmButton = new Button("OK");
        confirmButton.setOnAction(this::handleOptionConfirm);
        optionsGrid.add(confirmButton, 0, 0);
    }

    public void setup(int rowNum, int colNum){
        this.rowNum.set(rowNum);
        this.colNum.set(colNum);

        //setting up the graphical elements (default value)
        oddMarbleBox = new MarbleBox();
        marketGrid.add(oddMarbleBox, 0, 0);
        graphicGrid = new MarbleBox[rowNum][colNum];
        for(int i = 0; i < rowNum; i++){
            for(int j = 0; j < colNum; j++){
                graphicGrid[i][j] = new MarbleBox("yellow");
                marketGrid.add(graphicGrid[i][j], j, i + 1);
            }
        }

        //adding horizontal buttons
        for(int i = 0; i < colNum; i++){
            Button selectButton = new Button();
            selectButton.setOnMousePressed(this::handleRowOrColPick);
            buttons.add(selectButton);
            marketGrid.add(selectButton, i, rowNum + 1);
            selectButton.setDisable(areControlsDisabled.get());
            selectButton.setVisible(!areControlsDisabled.get());
            GridPane.setHalignment(selectButton, HPos.CENTER);
        }

        //adding vertical buttons
        for(int i = 0; i < rowNum; i++){
            Button selectButton = new Button();
            selectButton.setOnMousePressed(this::handleRowOrColPick);
            buttons.add(selectButton);
            marketGrid.add(selectButton, colNum, i + 1);
            selectButton.setDisable(areControlsDisabled.get());
            selectButton.setVisible(!areControlsDisabled.get());
            GridPane.setHalignment(selectButton, HPos.CENTER);
        }

        // this.setStyle("-fx-background-color: red");
    }

    //SETTERS
    public void setRawMarket(RawMarket rawMarket) {
        this.rawMarket.set(rawMarket);
        updateGrid();
    }

    public void updateConversions(List<List<ConversionOption>> conversionOptions, List<MarbleColor> selectedMarbleColors){
        //if the updated value is empty and the old value is not, we remove everything from the grid and set it not visible
        if(conversionOptions.isEmpty() && !conversionOptionsProperty().get().isEmpty()){
            optionsGrid.getChildren().clear();
            optionsGrid.setVisible(false);
            optionsGrid.setDisable(true);
            conversionOptionsProperty().set(conversionOptions);
            selectedMarbleColorsProperty().set(selectedMarbleColors);
            selectedOptions.clear();
            selectableOptionBoxes.clear();
        }
        //if the updated value is not empty and the old value is empty, update
        else if(!conversionOptions.isEmpty() && conversionOptionsProperty().get().isEmpty()){
            conversionOptionsProperty().set(conversionOptions);
            selectedMarbleColorsProperty().set(selectedMarbleColors);
            optionsGrid.setVisible(true);
            optionsGrid.setDisable(false);
            updateOptions();
        }
    }

    private void handleOptionSelection(MouseEvent event){
        HBox sourceOption = (HBox) event.getSource();
        int optionRow = GridPane.getRowIndex(sourceOption) - 1;
        int optionCol = GridPane.getColumnIndex(sourceOption) - 1;

        for(HBox hBox : selectableOptionBoxes.get(optionCol)){
            if(hBox != sourceOption){
                hBox.setStyle("");
            }
        }

        sourceOption.setStyle("-fx-background-color: red");

        selectedOptions.set(optionCol, optionRow);
    }

    private void handleOptionConfirm(ActionEvent event){
        ConversionSelectionBean bean = new ConversionSelectionBean();
        bean.setSelectedConversions(selectedOptions);
        fireEvent(new ConversionSelectionEvent(bean));
    }
    
    private void handleRowOrColPick(MouseEvent event){
        Button button = (Button) event.getSource();
        int buttonRow = GridPane.getRowIndex(button);
        int buttonCol = GridPane.getColumnIndex(button);

        boolean isRaw = !(buttonRow == rowNum.get() + 1);
        int index;
        if(isRaw)
            index = buttonRow - 1;
        else
            index = buttonCol;

        MarketPickBean bean = new MarketPickBean();
        bean.setIsRow(isRaw);
        bean.setIndex(index);
        fireEvent(new MarketPickEvent(bean));
    }

    //GETTERS
    public int getRowNum() {
        return rowNum.get();
    }

    public IntegerProperty rowNumProperty() {
        return rowNum;
    }

    public int getColNum() {
        return colNum.get();
    }

    public IntegerProperty colNumProperty() {
        return colNum;
    }

    public RawMarket getRawMarket() {
        return rawMarket.get();
    }

    public ObjectProperty<RawMarket> rawMarketProperty() {
        return rawMarket;
    }

    public List<MarbleColor> getSelectedMarbleColors() {
        return selectedMarbleColors.get();
    }

    public ObjectProperty<List<MarbleColor>> selectedMarbleColorsProperty() {
        return selectedMarbleColors;
    }

    public List<List<ConversionOption>> getConversionOptions() {
        return conversionOptions.get();
    }

    public ObjectProperty<List<List<ConversionOption>>> conversionOptionsProperty() {
        return conversionOptions;
    }

    public MarbleBox getOddMarbleBox() {
        return oddMarbleBox;
    }

    public boolean isAreControlsDisabled() {
        return areControlsDisabled.get();
    }

    public BooleanProperty areControlsDisabledProperty() {
        return areControlsDisabled;
    }

    public void setAreControlsDisabled(boolean areControlsDisabled) {
        this.areControlsDisabled.set(areControlsDisabled);
    }
}
