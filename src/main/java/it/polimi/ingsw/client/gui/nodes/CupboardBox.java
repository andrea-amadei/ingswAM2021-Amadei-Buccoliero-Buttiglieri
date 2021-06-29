package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.FXMLCachedLoaders;
import it.polimi.ingsw.common.utils.ResourceLoader;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CupboardBox extends GridPane {
    private final ListProperty<String> baseResources;
    private final ListProperty<String> baseAccepted;

    private final ListProperty<String> leaderResources;
    private final ListProperty<String> leaderAccepted;

    private final BooleanProperty areControlsDisabled;

    private List<ShelfBox> base;
    private List<ShelfBox> leader;

    private final List<Integer> BASE_SIZES = Arrays.asList(1, 2, 3);
    private final List<String> BASE_NAMES = Arrays.asList("Top", "Middle", "Bottom");

    public CupboardBox() {
        FXMLLoader fxmlLoader;
        String fileName = "jfx/custom/CupboardBox.fxml";
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

        baseResources = new SimpleListProperty<>(this, "baseResources", FXCollections.observableArrayList("none", "none", "none"));
        baseAccepted = new SimpleListProperty<>(this, "baseAccepted", FXCollections.observableArrayList());

        leaderResources = new SimpleListProperty<>(this, "leaderResources", FXCollections.observableArrayList("none", "none", "none"));
        leaderAccepted = new SimpleListProperty<>(this, "leaderResources", FXCollections.observableArrayList());

        areControlsDisabled = new SimpleBooleanProperty(this, "areControlsDisabled", true);

        setup();
    }

    private void setup() {
        int i;

        base = new ArrayList<>();
        for(i = 0; i < baseResources.size(); i++) {
            base.add(new ShelfBox("any", BASE_SIZES.get(base.size()), BASE_NAMES.get(base.size())));
            baseAccepted.add("any");
        }

        leader = new ArrayList<>();
        for(i = 0; i < leaderResources.size(); i++) {
            leader.add(new ShelfBox("any", 2, "Leader " + (leader.size() + 1)));
            leaderAccepted.add("any");
        }
    }

    public ObservableList<String> getBaseResources() {
        return baseResources.get();
    }

    public ListProperty<String> baseResourcesProperty() {
        return baseResources;
    }

    public ObservableList<String> getLeaderResources() {
        return leaderResources.get();
    }

    public ListProperty<String> leaderResourcesProperty() {
        return leaderResources;
    }

    public ObservableList<String> getLeaderAccepted() {
        return leaderAccepted.get();
    }

    public ListProperty<String> leaderAcceptedProperty() {
        return leaderAccepted;
    }

    public void setBaseResources(ObservableList<String> baseResources) {
        this.baseResources.set(baseResources);
    }

    public void addBaseShelf(String acceptedResource, int nBase) {
        base.get(nBase).setAcceptedResource(acceptedResource);
        super.add(base.get(nBase), 0, nBase);
        baseAccepted.set(nBase, acceptedResource);
    }

    public void addLeaderShelf(String acceptedResource, int nLeader) {
        leader.get(nLeader).setAcceptedResource(acceptedResource);
        super.add(leader.get(nLeader), 1, nLeader);
        leaderAccepted.set(nLeader, acceptedResource);
    }

    public List<ShelfBox> getBaseShelves() {
        return base;
    }

    public List<ShelfBox> getLeaderShelves() {
        return leader;
    }

    public void setAreControlsDisabled(boolean disabled){
        for(ShelfBox s : base)
            s.setAreControlsDisabled(disabled);
        for(ShelfBox s : leader)
            s.setAreControlsDisabled(disabled);
    }
}
