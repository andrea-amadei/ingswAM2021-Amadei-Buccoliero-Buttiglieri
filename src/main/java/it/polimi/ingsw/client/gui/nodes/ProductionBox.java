package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.beans.CraftingSelectionBean;
import it.polimi.ingsw.client.gui.beans.OutputSelectionBean;
import it.polimi.ingsw.client.gui.events.CraftingSelectionEvent;
import it.polimi.ingsw.client.gui.events.OutputSelectionEvent;
import it.polimi.ingsw.client.clientmodel.ClientProduction;
import it.polimi.ingsw.server.model.production.Production;
import it.polimi.ingsw.common.parser.raw.RawCrafting;
import it.polimi.ingsw.common.utils.Pair;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductionBox extends GridPane {

    private final ObjectProperty<List<RawCrafting>> upgradableRawCraftings;
    private final ObjectProperty<List<Integer>> upgradableLevels;

    private final ObjectProperty<List<RawCrafting>> baseRawCraftings;

    private final ObjectProperty<List<RawCrafting>> leaderRawCraftings;

    private final ObjectProperty<Pair<Integer, Integer>> selectedBox;

    private final BooleanProperty areControlsDisabled;


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
        areControlsDisabled = new SimpleBooleanProperty(this, "areControlsDisabled", true);

        slotBoxes = new ArrayList<>();
        upgradableCraftingBoxes = new ArrayList<>(Arrays.asList(null, null, null));
        baseCraftingBoxes = new ArrayList<>();
        leaderCraftingBoxes = new ArrayList<>();
        setup();
    }

    private void setup() {

        //setting up the grid that will contain the crafting
        for(int i = 0; i < ROW_NUM; i++){
            for(int j = 0; j < COL_NUM; j++){
                StackPane stackPane = new StackPane();
                stackPane.setPrefHeight(220);
                stackPane.setPrefWidth(220);
                Region region = new Region();
                stackPane.setStyle("-fx-border-color: black; -fx-border-insets: 10;");
                stackPane.getChildren().add(region);
                slotBoxes.add(stackPane);
                this.add(stackPane, j, i);
            }
        }
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
            craftingBox.setOnMouseClicked(this::handleSelectCrafting);
            baseCraftingBoxes.add(craftingBox);
            slotBoxes.get(COL_NUM).getChildren().add(craftingBox);
        }

        this.upgradableRawCraftings.set(clientProduction.getUpgradableCraftings());
        this.upgradableLevels.set(clientProduction.getUpgradableLevels());

        //add the new upgradableCraftingBoxes
        for(int i = 0; i < upgradableRawCraftings.get().size(); i++){
            if(upgradableCraftingBoxes.get(i) == null && upgradableRawCraftings.get().get(i) != null) {
                CraftingBox craftingBox = new CraftingBox(upgradableRawCraftings.get().get(i));
                craftingBox.setOnMouseClicked(this::handleSelectCrafting);
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
            craftingBox.setOnMouseClicked(this::handleSelectCrafting);
            slotBoxes.get(COL_NUM + i + 1).getChildren().add(craftingBox);
        }
        //update readiness
        updateCraftingReady(clientProduction.getBaseCraftingsReady(), clientProduction.getUpgradableCraftingsReady(), clientProduction.getLeaderCraftingsReady());
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
            }
            selectedBox.set(new Pair<>(row, col));
        }
        else if(selectedBox.get() != null){
            //we need to deselect the selected slot
            slotBoxes.get(selectedBox.get().getFirst() * COL_NUM + selectedBox.get().getSecond()).getChildren().get(0).setStyle("");
            selectedBox.set(null);
        }
    }

    private void updateCraftingReady(List<Boolean> baseReady, List<Boolean> upgradableReady, List<Boolean> leaderReady){
        //set the base crafting readiness
        StackPane baseStackPane = slotBoxes.get(COL_NUM);
        baseStackPane.getChildren().get(0).setStyle((baseReady.get(0)) ? "-fx-background-color: #2c2cea" : "");

        //set the upgradable crafting row
        for(int i = 0; i < upgradableReady.size(); i++){
            StackPane upgradableStackPane = slotBoxes.get(i);
            upgradableStackPane.getChildren().get(0).setStyle((upgradableReady.get(i)) ? "-fx-background-color: #2c2cea" : "");
        }

        //set the leader upgradable row
        for(int i = 0; i < leaderReady.size(); i++){
            StackPane leaderStackPane = slotBoxes.get(COL_NUM + i + 1);
            leaderStackPane.getChildren().get(0).setStyle((leaderReady.get(i)) ? "-fx-background-color: #2c2cea" : "");
        }
    }
    private void handleSelectCrafting(MouseEvent event){
        if(!areControlsDisabled.get()) {
            if(event.getButton().equals(MouseButton.PRIMARY)) {
                CraftingBox selectedBox = (CraftingBox) event.getSource();
                Production.CraftingType type;
                int index;

                int row = GridPane.getRowIndex(selectedBox.getParent());
                int col = GridPane.getColumnIndex(selectedBox.getParent());

                if (row == 0) {
                    type = Production.CraftingType.UPGRADABLE;
                    index = col;
                } else if (row == 1 && col == 0) {
                    type = Production.CraftingType.BASE;
                    index = 0;
                } else {
                    type = Production.CraftingType.LEADER;
                    index = col - 1;
                }
                CraftingSelectionBean bean = new CraftingSelectionBean();
                bean.setCraftingType(type);
                bean.setIndex(index);

                fireEvent(new CraftingSelectionEvent(bean));
            }else if(event.getButton().equals(MouseButton.SECONDARY)){
                CraftingBox selectedBox = (CraftingBox) event.getSource();
                RawCrafting rawCrafting = selectedBox.getRawCrafting();
                if(rawCrafting.getOutput().containsKey("any")){
                    OutputSelectionBean bean = new OutputSelectionBean();
                    bean.setAmount(rawCrafting.getOutput().get("any"));
                    fireEvent(new OutputSelectionEvent(bean));
                }
            }
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
