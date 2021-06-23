package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.FXMLCachedLoaders;
import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
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
    private final ListProperty<Integer> baseAmounts;

    private final ListProperty<String> leaderResources;
    private final ListProperty<String> leaderAccepted;
    private final ListProperty<Integer> leaderAmounts;

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

        baseResources = new SimpleListProperty<>(this, "baseResources", FXCollections.observableArrayList());
        baseAccepted = new SimpleListProperty<>(this, "baseAccepted", FXCollections.observableArrayList());
        baseAmounts = new SimpleListProperty<>(this, "baseAmounts", FXCollections.observableArrayList());

        leaderResources = new SimpleListProperty<>(this, "leaderResources", FXCollections.observableArrayList());
        leaderAccepted = new SimpleListProperty<>(this, "leaderResources", FXCollections.observableArrayList());
        leaderAmounts = new SimpleListProperty<>(this, "leaderResources", FXCollections.observableArrayList());

        setup();
        update();
    }

    private void setup() {
        int i;

        base = new ArrayList<>();
        for(i = 0; i < baseResources.size(); i++) {
            base.add(new ShelfBox("any", i + 1, BASE_NAMES.get(i)));
            super.add(base.get(i), 0, i);
        }

        leader = new ArrayList<>();
        for(i = 0; i < leaderResources.size(); i++) {
            leader.add(new ShelfBox("any", 2, "Leader " + (i + 1)));
            super.add(leader.get(i), 1, i);
        }
    }

    private void update() {
        int i;

        for(i = 0; i < base.size(); i++) {
            if(baseAmounts.get(i) >= 1)
                base.get(i).setResource1(baseResources.get(i));
            else
                base.get(i).setResource1("none");

            if(baseAmounts.get(i) >= 2)
                base.get(i).setResource2(baseResources.get(i));
            else
                base.get(i).setResource2("none");

            if(baseAmounts.get(i) >= 3)
                base.get(i).setResource3(baseResources.get(i));
            else
                base.get(i).setResource3("none");
        }

        for(i = 0; i < leader.size(); i++) {
            if(leaderAmounts.get(i) >= 1)
                leader.get(i).setResource1(leaderResources.get(i));
            else
                leader.get(i).setResource1("none");

            if(leaderAmounts.get(i) >= 2)
                leader.get(i).setResource2(leaderResources.get(i));
            else
                leader.get(i).setResource2("none");
        }
    }

    public ObservableList<String> getBaseResources() {
        return baseResources.get();
    }

    public ListProperty<String> baseResourcesProperty() {
        return baseResources;
    }

    public ObservableList<Integer> getBaseAmounts() {
        return baseAmounts.get();
    }

    public ListProperty<Integer> baseAmountsProperty() {
        return baseAmounts;
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

    public ObservableList<Integer> getLeaderAmounts() {
        return leaderAmounts.get();
    }

    public ListProperty<Integer> leaderAmountsProperty() {
        return leaderAmounts;
    }

    public void setBaseResources(ObservableList<String> baseResources) {
        this.baseResources.set(baseResources);
        update();
    }

    public void setBaseAmounts(ObservableList<Integer> baseAmounts) {
        this.baseAmounts.set(baseAmounts);
        update();
    }

    public void addLeaderShelf(String acceptedResource) {
        leader.add(new ShelfBox(acceptedResource, 2, "Leader " + (leader.size() + 1)));
        super.add(leader.get(leader.size() - 1), 1, leader.size() - 1);
        leaderAccepted.add(acceptedResource);
        leaderAmounts.add(0);
        leaderResources.add("gold");
        update();
    }

    public void addBaseShelf(String acceptedResource) {
        base.add(new ShelfBox(acceptedResource, BASE_SIZES.get(base.size()), BASE_NAMES.get(base.size())));
        super.add(base.get(base.size() - 1), 0, base.size() - 1);
        baseAccepted.add(acceptedResource);
        baseAmounts.add(0);
        baseResources.add("gold");
        update();
    }

    public void setLeaderShelf(int index, String resource, int amount) {
        this.leaderResources.set(index, resource);
        this.leaderAmounts.set(index, amount);
        update();
    }
}
