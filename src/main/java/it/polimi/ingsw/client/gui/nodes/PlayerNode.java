package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.FXMLCachedLoaders;
import it.polimi.ingsw.client.gui.beans.ResourceContainerBean;
import it.polimi.ingsw.client.gui.beans.ResourceSelectionBean;
import it.polimi.ingsw.client.gui.events.ResourceSelectionEvent;
import it.polimi.ingsw.client.gui.events.ResourceTransferEvent;
import it.polimi.ingsw.common.utils.Pair;
import it.polimi.ingsw.common.utils.ResourceLoader;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.*;

public class PlayerNode extends AnchorPane {

    public enum ContainerType{BASE, LEADER, HAND, CHEST, MARKET}
    @FXML
    private CupboardBox cupboard;
    @FXML
    private HResourceContainer chest;
    @FXML
    private VResourceContainer hand;
    @FXML
    private VResourceContainer basket;
    @FXML
    private ProductionBox production;
    @FXML
    private LeaderCardSlotsBox leaders;

    private final BooleanProperty areControlsDisable;

    public PlayerNode() {
        FXMLLoader fxmlLoader;
        String fileName = "jfx/custom/PlayerNode.fxml";
        if(FXMLCachedLoaders.getInstance().isLoaderContained(fileName)) {
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

        areControlsDisable = new SimpleBooleanProperty(this, "areControlsDisable", true);
        areControlsDisable.addListener((b, oldValue, newValue) -> setControlsStatus(newValue));

        setupListeners();
    }

    public void setControlsStatus(boolean disable){
        cupboard.setAreControlsDisabled(disable);
        chest.setAreControlsDisabled(disable);
        hand.setAreControlsDisabled(disable);
        basket.setAreControlsDisabled(disable);
        production.setAreControlsDisabled(disable);
        leaders.setAreControlsDisabled(disable);
    }

    public boolean isAreControlsDisable() {
        return areControlsDisable.get();
    }

    public BooleanProperty areControlsDisableProperty() {
        return areControlsDisable;
    }

    public void setAreControlsDisable(boolean areControlsDisable) {
        this.areControlsDisable.set(areControlsDisable);
    }

    public CupboardBox getCupboard() {
        return cupboard;
    }

    public HResourceContainer getChest() {
        return chest;
    }

    public VResourceContainer getHand() {
        return hand;
    }

    public VResourceContainer getBasket() {
        return basket;
    }

    public ProductionBox getProduction() {
        return production;
    }

    public LeaderCardSlotsBox getLeaders() {
        return leaders;
    }

    private void setupListeners() {
        for(ShelfBox s : cupboard.getBaseShelves()){
            s.setResourceSelectionCallback(this::handleResourceSelection);
            s.setStartResourceTransferCallback(this::handleResourceTransfer);
        }
        for(ShelfBox s : cupboard.getLeaderShelves()){
            s.setResourceSelectionCallback(this::handleResourceSelection);
            s.setStartResourceTransferCallback(this::handleResourceTransfer);
        }

        chest.setResourceSelectionCallback(this::handleResourceSelection);
        hand.setStartResourceTransferCallback(this::handleResourceTransfer);

        basket.setStartResourceTransferCallback(this::handleResourceTransfer);
    }

    private void handleResourceSelection(ResourceSelectionBean bean){
        Pair<ContainerType, Integer> containerInfo = getContainerInfo(bean.getSource());
        Set<ContainerType> acceptedTypes = Set.of(ContainerType.BASE, ContainerType.CHEST, ContainerType.LEADER);
        if(containerInfo.getFirst() != null && acceptedTypes.contains(containerInfo.getFirst())){
            bean.setSourceType(containerInfo.getFirst());
            bean.setIndex(containerInfo.getSecond());

            fireEvent(new ResourceSelectionEvent(bean));
            //System.out.println("Fired resource selection: " + containerInfo.getFirst() + " " + containerInfo.getSecond());
        }
    }

    private void handleResourceTransfer(ResourceContainerBean bean){
        Pair<ContainerType, Integer> containerInfo = getContainerInfo(bean.getSource());
        Set<ContainerType> acceptedTypes = Set.of(ContainerType.BASE, ContainerType.HAND, ContainerType.LEADER, ContainerType.MARKET);
        if(containerInfo.getFirst() != null && acceptedTypes.contains(containerInfo.getFirst())){
            bean.setSourceType(containerInfo.getFirst());
            bean.setIndex(containerInfo.getSecond());

            fireEvent(new ResourceTransferEvent(bean));
            //System.out.println("Fired start resource transfer: " + containerInfo.getFirst() + " " + containerInfo.getSecond());
        }
    }

    private Pair<ContainerType, Integer> getContainerInfo(Object source){
        int index = -1;
        ContainerType containerType = null;
        for(int i = 0; i < cupboard.getBaseShelves().size(); i++){
            if(cupboard.getBaseShelves().get(i).equals(source)){
                index = i;
                containerType = ContainerType.BASE;
            }
        }
        for(int i = 0; i < cupboard.getLeaderShelves().size(); i++){
            if(cupboard.getLeaderShelves().get(i).equals(source)) {
                index = i;
                containerType = ContainerType.LEADER;
            }
        }
        if(chest.equals(source)){
            containerType = ContainerType.CHEST;
        }

        if(hand.equals(source)){
            containerType = ContainerType.HAND;
        }
        if(basket.equals(source)){
            containerType = ContainerType.MARKET;
        }

        return new Pair<>(containerType, index);
    }
}
