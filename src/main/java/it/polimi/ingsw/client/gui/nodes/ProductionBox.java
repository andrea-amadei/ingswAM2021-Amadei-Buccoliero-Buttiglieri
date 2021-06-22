package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.FXMLCachedLoaders;
import it.polimi.ingsw.client.model.ClientProduction;
import it.polimi.ingsw.model.production.Production;
import it.polimi.ingsw.parser.raw.RawCrafting;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class ProductionBox extends GridPane {

    private final ObjectProperty<List<RawCrafting>> upgradableRawCraftings;
    private final ObjectProperty<List<Integer>> upgradableLevels;

    private final ObjectProperty<List<RawCrafting>> baseRawCraftings;

    private final ObjectProperty<List<RawCrafting>> leaderRawCraftings;

    private final ObjectProperty<Pair<Integer, Integer>> selectedBox;


    private final List<StackPane> slotBoxes;
    private final List<CraftingBox> upgradableCraftingBoxes;
    private final List<CraftingBox> baseCraftingBoxes;
    private final List<CraftingBox> leaderCraftingBoxes;

    private final int ROW_NUM = 2;
    private final int COL_NUM = 3;

    public ProductionBox(){
        upgradableRawCraftings = new SimpleObjectProperty<>(this, "upgradableRawCraftings", new ArrayList<>(Arrays.asList(null, null, null)));
        upgradableLevels = new SimpleObjectProperty<>(this, "upgradableLevels", new ArrayList<>(Arrays.asList(0, 0, 0)));
        baseRawCraftings = new SimpleObjectProperty<>(this, "baseRawCraftings", new ArrayList<>());
        leaderRawCraftings = new SimpleObjectProperty<>(this, "leaderRawCraftings", new ArrayList<>());
        selectedBox = new SimpleObjectProperty<>(this, "selectedBox", null);

        slotBoxes = new ArrayList<>();
        upgradableCraftingBoxes = new ArrayList<>(Arrays.asList(null, null, null));
        baseCraftingBoxes = new ArrayList<>();
        leaderCraftingBoxes = new ArrayList<>();
        setup();
    }

    private void setup() {

        //setting up the grid that will contain the craftings
        for(int i = 0; i < ROW_NUM; i++){
            for(int j = 0; j < COL_NUM; j++){
                StackPane stackPane = new StackPane();
                stackPane.setPrefHeight(220);
                stackPane.setPrefWidth(204);
                Region region = new Region();
                stackPane.setStyle("-fx-border-color: black; -fx-border-insets: 10;");
                stackPane.getChildren().add(region);
                slotBoxes.add(stackPane);
                this.add(stackPane, j, i);
            }
        }

        System.out.println(slotBoxes.get(0).backgroundProperty());

    }

    private void update(){

        //update all the upgradable crafting slots
        for(int i = 0; i < upgradableRawCraftings.get().size(); i++){
            if(upgradableRawCraftings.get().get(i) != null)
            upgradableCraftingBoxes.get(i).setUpgradableRawCrafting(upgradableRawCraftings.get().get(i), upgradableLevels.get().get(i));
        }

        //update all the base crafting slots
        for(int i = 0; i < baseRawCraftings.get().size(); i++){
            baseCraftingBoxes.get(i).setRawCrafting(baseRawCraftings.get().get(i));
        }

        //update all the leader crafting slots
        for(int i = 0; i < leaderRawCraftings.get().size(); i++){
            leaderCraftingBoxes.get(i).setRawCrafting(leaderRawCraftings.get().get(i));
        }
    }

    public void setProduction(ClientProduction clientProduction){
        this.baseRawCraftings.set(clientProduction.getBaseCraftings());

        //add the new baseCraftingBoxes
        for(int i = baseCraftingBoxes.size(); i < baseRawCraftings.get().size(); i++){
            CraftingBox craftingBox = new CraftingBox(baseRawCraftings.get().get(i));
            baseCraftingBoxes.add(craftingBox);
            slotBoxes.get(COL_NUM).getChildren().add(craftingBox);
        }

        this.upgradableRawCraftings.set(clientProduction.getUpgradableCraftings());
        this.upgradableLevels.set(clientProduction.getUpgradableLevels());

        //add the new upgradableCraftingBoxes
        for(int i = 0; i < upgradableRawCraftings.get().size(); i++){
            if(upgradableCraftingBoxes.get(i) == null && upgradableRawCraftings.get().get(i) != null) {
                CraftingBox craftingBox = new CraftingBox(upgradableRawCraftings.get().get(i));
                craftingBox.setLevel(upgradableLevels.get().get(i));
                upgradableCraftingBoxes.set(i, craftingBox);
                slotBoxes.get(i).getChildren().add(craftingBox);
            }
        }

        this.leaderRawCraftings.set(clientProduction.getLeaderCraftings());

        //add the new leaderCraftingBoxes
        for(int i =  leaderCraftingBoxes.size(); i < leaderRawCraftings.get().size(); i++){
            CraftingBox craftingBox = new CraftingBox(leaderRawCraftings.get().get(i));
            leaderCraftingBoxes.add(craftingBox);
            slotBoxes.get(COL_NUM + i + 1).getChildren().add(craftingBox);
        }

        //update selection
        updateSelection(clientProduction.getSelectedType(), clientProduction.getSelectedCraftingIndex());

        update();
    }

    private void updateSelection(Production.CraftingType selectedType, Integer selectedIndex){
        if(selectedType != null){
            int row, col;
            switch(selectedType){
                case BASE:
                    row = 1;
                    col = 0;
                    break;
                case UPGRADABLE:
                    row = 0;
                    col = selectedIndex;
                    break;
                default:
                    row = 1;
                    col = 1 + selectedIndex;
            }
            //if there is an already selected box we need to deselect it
            if(selectedBox.get() != null){
                if((!selectedBox.get().getFirst().equals(row) || !selectedBox.get().getSecond().equals(col))) {
                    StackPane selectedStackPane = slotBoxes.get(selectedBox.get().getFirst() * COL_NUM + selectedBox.get().getSecond());
                    selectedStackPane.getChildren().get(0).setStyle("");
                }
            }


            if(selectedBox.get() == null || (!selectedBox.get().getFirst().equals(row) || !selectedBox.get().getSecond().equals(col))) {
                StackPane selectedStackPane = slotBoxes.get(row * COL_NUM + col);
                selectedStackPane.getChildren().get(0).setStyle("-fx-background-color: red");
                System.out.println();
            }
            selectedBox.set(new Pair<>(row, col));
        }
        else if(selectedBox.get() != null){
            //we need to deselect the selected slot
            slotBoxes.get(selectedBox.get().getFirst() * COL_NUM + selectedBox.get().getSecond()).getChildren().get(0).setStyle("");
            selectedBox.set(null);
        }
    }

    public List<RawCrafting> getUpgradableRawCraftings() {
        return upgradableRawCraftings.get();
    }

    public ObjectProperty<List<RawCrafting>> upgradableRawCraftingsProperty() {
        return upgradableRawCraftings;
    }

    public List<Integer> getUpgradableLevels() {
        return upgradableLevels.get();
    }

    public ObjectProperty<List<Integer>> upgradableLevelsProperty() {
        return upgradableLevels;
    }

    public List<RawCrafting> getBaseRawCraftings() {
        return baseRawCraftings.get();
    }

    public ObjectProperty<List<RawCrafting>> baseRawCraftingsProperty() {
        return baseRawCraftings;
    }

    public List<RawCrafting> getLeaderRawCraftings() {
        return leaderRawCraftings.get();
    }

    public ObjectProperty<List<RawCrafting>> leaderRawCraftingsProperty() {
        return leaderRawCraftings;
    }
}
