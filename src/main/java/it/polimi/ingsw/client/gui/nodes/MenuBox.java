package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.FXMLCachedLoaders;
import it.polimi.ingsw.client.gui.events.*;
import it.polimi.ingsw.common.payload_components.groups.PossibleActions;
import it.polimi.ingsw.model.actions.SelectPlayAction;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

public class MenuBox extends VBox {

    @FXML
    private Button backBtn;

    @FXML
    private Button confirmButton;

    @FXML
    private Button selectPlayCraftingBtn;

    @FXML
    private Button selectPlayMarketBtn;

    @FXML
    private Button selectPlayShopBtn;

    @FXML
    private Button preliminaryPickBtn;

    @FXML
    private GridPane grid;

    private final SetProperty<PossibleActions> possibleActions;
    private final BooleanProperty areControlsDisabled;
    private final List<Button> btns;


    public MenuBox(){
        FXMLLoader fxmlLoader;
        String fileName = "jfx/custom/MenuBox.fxml";
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

        possibleActions = new SimpleSetProperty<>(this, "possibleActions", FXCollections.observableSet());
        btns = new ArrayList<>(Arrays.asList(backBtn, confirmButton, selectPlayCraftingBtn, selectPlayMarketBtn, selectPlayShopBtn, preliminaryPickBtn));
        areControlsDisabled = new SimpleBooleanProperty(this, "AreControlsDisabled", true);
        //setting up events and bindings

        backBtn.setOnAction(e -> fireEvent(new BackEvent()));
        //backBtn.visibleProperty().bind(Bindings.createBooleanBinding(()-> possibleActions.contains(PossibleActions.BACK) && !areControlsDisabled.get(), possibleActions));
        backBtn.disableProperty().bind(Bindings.createBooleanBinding(()->!(possibleActions.contains(PossibleActions.BACK) && !areControlsDisabled.get()), possibleActions));


        confirmButton.setOnAction(e -> fireEvent(new ConfirmEvent()));
        //confirmButton.visibleProperty().bind(Bindings.createBooleanBinding(()-> (possibleActions.contains(PossibleActions.CONFIRM) || possibleActions.contains(PossibleActions.CONFIRM_TIDY)) && !areControlsDisabled.get(), possibleActions));
        confirmButton.disableProperty().bind(Bindings.createBooleanBinding(()->!(possibleActions.contains(PossibleActions.CONFIRM) || possibleActions.contains(PossibleActions.CONFIRM_TIDY)) || areControlsDisabled.get(), possibleActions));

        selectPlayCraftingBtn.setOnAction(e -> fireEvent(new SelectPlayEvent(SelectPlayAction.Play.CRAFTING)));
        //selectPlayCraftingBtn.visibleProperty().bind(Bindings.createBooleanBinding(()-> possibleActions.contains(PossibleActions.SELECT_PLAY) && !areControlsDisabled.get(), possibleActions));
        selectPlayCraftingBtn.disableProperty().bind(Bindings.createBooleanBinding(()->!possibleActions.contains(PossibleActions.SELECT_PLAY) || areControlsDisabled.get(), possibleActions));

        selectPlayMarketBtn.setOnAction(e -> fireEvent(new SelectPlayEvent(SelectPlayAction.Play.MARKET)));
        //selectPlayMarketBtn.visibleProperty().bind(Bindings.createBooleanBinding(()-> possibleActions.contains(PossibleActions.SELECT_PLAY) && !areControlsDisabled.get(), possibleActions));
        selectPlayMarketBtn.disableProperty().bind(Bindings.createBooleanBinding(()->!possibleActions.contains(PossibleActions.SELECT_PLAY) || areControlsDisabled.get(), possibleActions));

        selectPlayShopBtn.setOnAction(e -> fireEvent(new SelectPlayEvent(SelectPlayAction.Play.SHOP)));
        //selectPlayShopBtn.visibleProperty().bind(Bindings.createBooleanBinding(()-> possibleActions.contains(PossibleActions.SELECT_PLAY) && !areControlsDisabled.get(), possibleActions));
        selectPlayShopBtn.disableProperty().bind(Bindings.createBooleanBinding(()->!possibleActions.contains(PossibleActions.SELECT_PLAY) || areControlsDisabled.get(), possibleActions));

        preliminaryPickBtn.setOnAction(e -> fireEvent(new PreliminaryPickEvent()));
        //preliminaryPickBtn.visibleProperty().bind(Bindings.createBooleanBinding(()-> possibleActions.contains(PossibleActions.PRELIMINARY_PICK) && !areControlsDisabled.get(), possibleActions));
        preliminaryPickBtn.disableProperty().bind(Bindings.createBooleanBinding(()->!possibleActions.contains(PossibleActions.PRELIMINARY_PICK) || areControlsDisabled.get(), possibleActions));

    }

    public Button getBackBtn() {
        return backBtn;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public Button getSelectPlayCraftingBtn() {
        return selectPlayCraftingBtn;
    }

    public Button getSelectPlayMarketBtn() {
        return selectPlayMarketBtn;
    }

    public Button getSelectPlayShopBtn() {
        return selectPlayShopBtn;
    }

    public Button getPreliminaryPickBtn() {
        return preliminaryPickBtn;
    }

    public void setPossibleActions(Set<PossibleActions> possibleActions){
        this.possibleActions.clear();
        this.possibleActions.addAll(possibleActions);
    }

    public void setAreControlsDisabled(boolean disable){
        areControlsDisabled.set(disable);
        Set<PossibleActions> previous = new HashSet<>(possibleActions.get());
        possibleActions.clear();
        possibleActions.addAll(previous);
    }

}
