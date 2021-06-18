package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.FXMLCachedLoaders;
import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawCraftingCard;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShopBox extends GridPane {

    private final ListProperty<RawCraftingCard> craftingCards;
    private final IntegerProperty rowNum;
    private final IntegerProperty colNum;

    private final List<CraftingCardBox> craftingCardBoxes;


    private GridPane grid;
    

    public ShopBox(){
        grid = this;

        rowNum = new SimpleIntegerProperty(this, "row count", 3);
        colNum = new SimpleIntegerProperty(this, "col count", 4);

        List<RawCraftingCard> defaultCards = new ArrayList<>();
        craftingCardBoxes = new ArrayList<>();
        for(int i = 0; i < rowNum.get(); i++){
            for(int j = 0; j < colNum.get(); j++) {
                try {
                    defaultCards.add(JSONParser.parse("{\"id\":3,\"color\":\"BLUE\",\"level\":1,\"points\":1,\"cost\":{\"gold\":2},\"crafting\":{\"input\":{\"shield\":1},\"output\":{},\"faith_output\":1}}", RawCraftingCard.class).toRaw());
                } catch (ParserException | IllegalRawConversionException e) {
                    e.printStackTrace();
                }
                CraftingCardBox cardBox = new CraftingCardBox();
                cardBox.setRawCraftingCard(defaultCards.get(i* colNum.get() + j));
                craftingCardBoxes.add(new CraftingCardBox());
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
                        CraftingCardBox cardBox = new CraftingCardBox();
                        cardBox.setRawCraftingCard(craftingCards.get(i* colNum.get() + j));
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


    public ObservableList<RawCraftingCard> getCraftingCards() {
        return craftingCards.get();
    }

    public ListProperty<RawCraftingCard> craftingCardsProperty() {
        return craftingCards;
    }

    public void setCraftingCards(List<RawCraftingCard> craftingCards) {
        this.craftingCards.set(FXCollections.observableArrayList(craftingCards));
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
}
