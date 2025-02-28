package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.beans.ShopSelectionBean;
import it.polimi.ingsw.client.gui.events.ShopCardSelectionEvent;
import it.polimi.ingsw.common.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.common.exceptions.ParserException;
import it.polimi.ingsw.common.parser.JSONParser;
import it.polimi.ingsw.common.parser.raw.RawCraftingCard;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class ShopBox extends GridPane {

    private final ListProperty<RawCraftingCard> craftingCards;
    private final IntegerProperty rowNum;
    private final IntegerProperty colNum;
    private final BooleanProperty areControlsDisabled;

    private final List<CraftingCardBox> craftingCardBoxes;
    private final String defaultStyle;
    private Integer selectedRow = null;
    private Integer selectedCol = null;


    private GridPane grid;
    

    public ShopBox(){
        grid = this;

        rowNum = new SimpleIntegerProperty(this, "row count", 3);
        colNum = new SimpleIntegerProperty(this, "col count", 4);
        areControlsDisabled = new SimpleBooleanProperty(this, "areControlsDisabled", true);

        defaultStyle = new CraftingCardBox().getStyle();

        List<RawCraftingCard> defaultCards = new ArrayList<>();
        craftingCardBoxes = new ArrayList<>();
        for(int i = 0; i < rowNum.get(); i++){
            for(int j = 0; j < colNum.get(); j++) {
                try {
                    defaultCards.add(JSONParser.parse("{\"id\":3,\"color\":\"BLUE\",\"level\":1,\"points\":1,\"cost\":{\"gold\":2, \"shield\":2, \"stone\":2, \"servant\":2},\"crafting\":{\"input\":{\"shield\":1},\"output\":{\"gold\":1, \"shield\":1, \"servant\":1, \"stone\":1},\"faith_output\":1}}", RawCraftingCard.class).toRaw());
                } catch (ParserException | IllegalRawConversionException e) {
                    e.printStackTrace();
                }
                CraftingCardBox cardBox = new CraftingCardBox(defaultCards.get(i * colNum.get() + j));
                cardBox.setOnMousePressed(this::handleCardSelection);
                craftingCardBoxes.add(cardBox);
                grid.add(cardBox, j, i);

            }
        }
        craftingCards = new SimpleListProperty<>(this, "cards", FXCollections.observableArrayList(defaultCards));

        update();
    }

    private void update(){
        for(int i = 0; i < rowNum.get(); i++){
            for(int j = 0; j < colNum.get(); j++){
                //if the rawCraftingCard is null and the cardBox is not null, then we must set the cardBox to null and remove it from the grid
                if(craftingCards.get(i* colNum.get() + j) == null){
                    if(craftingCardBoxes.get(i* colNum.get() + j) != null){
                        grid.getChildren().remove(craftingCardBoxes.get(i* colNum.get() + j));
                        craftingCardBoxes.set(i* colNum.get() + j, null);
                    }
                }
                //if the rawCraftingCard is not null, then:
                //  -if the cardBox is null, we need to add a new CardBox and sync the lists
                //  -if the cardBox is not null, we need to update it
                else{
                    if(craftingCardBoxes.get(i* colNum.get() + j) == null){
                        CraftingCardBox cardBox = new CraftingCardBox(craftingCards.get(i* colNum.get() + j));
                        cardBox.setOnMousePressed(this::handleCardSelection);
                        grid.add(cardBox, j, i);
                        craftingCardBoxes.set(i* colNum.get() + j, cardBox);
                    }else{
                        if(craftingCardBoxes.get(i* colNum.get() + j).getRawCraftingCard().getId() != craftingCards.get(i* colNum.get() + j).getId())
                            craftingCardBoxes.get(i* colNum.get() + j).setRawCraftingCard(craftingCards.get(i* colNum.get() + j));
                    }
                }
            }
        }
    }

    private void handleCardSelection(MouseEvent event){
        if(!areControlsDisabled.get()) {
            CraftingCardBox cardBox = (CraftingCardBox) event.getSource();
            int row = GridPane.getRowIndex(cardBox);
            int col = GridPane.getColumnIndex(cardBox);

            ShopSelectionBean shopSelectionBean = new ShopSelectionBean();
            shopSelectionBean.setCol(col);
            shopSelectionBean.setRow(row);
            this.fireEvent(new ShopCardSelectionEvent(shopSelectionBean));
        }
    }


    public ObservableList<RawCraftingCard> getCraftingCards() {
        return craftingCards.get();
    }

    public ListProperty<RawCraftingCard> craftingCardsProperty() {
        return craftingCards;
    }

    public void setCraftingCards(List<RawCraftingCard> craftingCards) {
        this.craftingCards.set(FXCollections.observableArrayList(craftingCards));
        update();
    }

    public void setSelectedCard(Integer selRow, Integer selCol){
        //get the old selected box and the new selected box
        CraftingCardBox oldSelectedBox = this.selectedRow != null ? craftingCardBoxes.get(colNum.get() * this.selectedRow + this.selectedCol) : null;

        //get the new selected card box
        CraftingCardBox newSelectedBox = selRow != null ? craftingCardBoxes.get(colNum.get() * selRow + selCol) : null;

        //if there was a selected card and now there is not, we just restore the selected card style
        if(selRow == null && this.selectedRow != null){
            if(oldSelectedBox != null)
                oldSelectedBox.setStyle(defaultStyle);
        }

        //if there is a selected card different from the previously selected card, then we update the style
        else if(selRow != null && (!selRow.equals(this.selectedRow) || !selCol.equals(this.selectedCol))){
            //if the previous selected card was not null restore the default style
            if(this.selectedRow != null && this.selectedCol != null) {
                if(oldSelectedBox != null)
                    oldSelectedBox.setStyle(defaultStyle);
            }

            if(newSelectedBox != null) {
                newSelectedBox.setStyle(defaultStyle + "-fx-background-color: red");
            }
        }
        this.selectedRow = selRow;
        this.selectedCol = selCol;
    }

    public int getRowNum() {
        return rowNum.get();
    }

    public IntegerProperty rowNumProperty() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum.set(rowNum);
    }

    public int getColNum() {
        return colNum.get();
    }

    public IntegerProperty colNumProperty() {
        return colNum;
    }

    public void setColNum(int colNum) {
        this.colNum.set(colNum);
    }

    public List<CraftingCardBox> getCraftingCardBoxes() {
        return craftingCardBoxes;
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
