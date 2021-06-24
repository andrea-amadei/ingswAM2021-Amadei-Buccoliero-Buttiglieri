package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.utils.ResourceLoader;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShelfBox extends GridPane {

    private StringProperty acceptedResource;
    private IntegerProperty size;
    private StringProperty resource1;
    private StringProperty resource2;
    private StringProperty resource3;

    private StringProperty name;

    private Label label;
    private List<ResourceBox> resources;

    public ShelfBox() {
        this("any", 3, "Shelf");
    }

    public ShelfBox(String acceptedResource, int size, String name) {
        this.acceptedResource = new SimpleStringProperty(this, "acceptedResourceProperty", acceptedResource);
        this.size = new SimpleIntegerProperty(this, "sizeProperty", size);
        this.resource1 = new SimpleStringProperty(this, "resource1Property", "none");
        this.resource2 = new SimpleStringProperty(this, "resource2Property", "none");
        this.resource3 = new SimpleStringProperty(this, "resource3Property", "none");
        this.name = new SimpleStringProperty(this, "name", name);

        setup();
        update();
    }

    private boolean resourceExists(String resource, boolean anyAccepted) {
        List<String> resources = new ArrayList<>(Arrays.asList("none", "gold", "servant", "shield", "stone"));

        if(anyAccepted)
            resources.add("any");

        return resources.contains(resource.toLowerCase());
    }

    public void setup() {
        int i;

        super.getRowConstraints().add(new RowConstraints(Double.NEGATIVE_INFINITY, 40d, Double.NEGATIVE_INFINITY));
        super.getColumnConstraints().add(new ColumnConstraints(Double.NEGATIVE_INFINITY, 100d, Double.NEGATIVE_INFINITY));
        for(i = 0; i < 5; i++) {
            super.getColumnConstraints().add(new ColumnConstraints(Double.NEGATIVE_INFINITY, 40d, Double.NEGATIVE_INFINITY));
        }

        resources = new ArrayList<>();

        for(i = 0; i < 3; i++) {
            resources.add(new ResourceBox("any", 1, false, true, false));
            super.getChildren().add(resources.get(i));
        }

        label = new Label(getName());
        label.setFont(new Font("Times New Roman bold", 22));
        GridPane.setColumnIndex(label, 0);
        super.getChildren().add(label);
    }

    public void update() {
        if(getSize() < 1 || getSize() > 3)
            throw new IllegalArgumentException("Size must be between 1 and 3");

        if(!resourceExists(getAcceptedResource(), true) || !resourceExists(getResource1(), false) || !resourceExists(getResource2(), false) || !resourceExists(getResource3(), false))
            throw new IllegalArgumentException("Illegal resource");

        label.setText(getName());

        switch(getSize()) {
            case 1:
                resources.get(0).setVisible(true);
                resources.get(1).setVisible(false);
                resources.get(2).setVisible(false);

                GridPane.setColumnIndex(resources.get(0), 3);

                if(getResource1().equals("none")) {
                    resources.get(0).setResource(getAcceptedResource());
                    resources.get(0).setOpacity(0.3d);
                }
                else {
                    resources.get(0).setResource(getResource1());
                    resources.get(0).setOpacity(1d);
                }

                break;

            case 2:
                resources.get(0).setVisible(true);
                resources.get(1).setVisible(true);
                resources.get(2).setVisible(false);

                GridPane.setColumnIndex(resources.get(0), 2);
                GridPane.setColumnIndex(resources.get(1), 4);

                if(getResource1().equals("none")) {
                    resources.get(0).setResource(getAcceptedResource());
                    resources.get(0).setOpacity(0.3d);
                }
                else {
                    resources.get(0).setResource(getResource1());
                    resources.get(0).setOpacity(1d);
                }

                if(getResource2().equals("none")) {
                    resources.get(1).setResource(getAcceptedResource());
                    resources.get(1).setOpacity(0.3d);
                }
                else {
                    resources.get(1).setResource(getResource1());
                    resources.get(1).setOpacity(1d);
                }

                break;

            case 3:
                resources.get(0).setVisible(true);
                resources.get(1).setVisible(true);
                resources.get(2).setVisible(true);

                GridPane.setColumnIndex(resources.get(0), 1);
                GridPane.setColumnIndex(resources.get(1), 3);
                GridPane.setColumnIndex(resources.get(2), 5);

                if(getResource1().equals("none")) {
                    resources.get(0).setResource(getAcceptedResource());
                    resources.get(0).setOpacity(0.3d);
                }
                else {
                    resources.get(0).setResource(getResource1());
                    resources.get(0).setOpacity(1d);
                }

                if(getResource2().equals("none")) {
                    resources.get(1).setResource(getAcceptedResource());
                    resources.get(1).setOpacity(0.3d);
                }
                else {
                    resources.get(1).setResource(getResource1());
                    resources.get(1).setOpacity(1d);
                }

                if(getResource3().equals("none")) {
                    resources.get(2).setResource(getAcceptedResource());
                    resources.get(2).setOpacity(0.3d);
                }
                else {
                    resources.get(2).setResource(getResource1());
                    resources.get(2).setOpacity(1d);
                }

                break;
        }
    }

    public String getAcceptedResource() {
        return acceptedResource.get();
    }

    public StringProperty acceptedResourceProperty() {
        return acceptedResource;
    }

    public int getSize() {
        return size.get();
    }

    public IntegerProperty sizeProperty() {
        return size;
    }

    public String getResource1() {
        return resource1.get();
    }

    public StringProperty resource1Property() {
        return resource1;
    }

    public String getResource2() {
        return resource2.get();
    }

    public StringProperty resource2Property() {
        return resource2;
    }

    public String getResource3() {
        return resource3.get();
    }

    public StringProperty resource3Property() {
        return resource3;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setAcceptedResource(String acceptedResource) {
        this.acceptedResource.set(acceptedResource);
        update();
    }

    public void setSize(int size) {
        this.size.set(size);
        update();
    }

    public void setResource1(String resource1) {
        this.resource1.set(resource1);
        update();
    }

    public void setResource2(String resource2) {
        this.resource2.set(resource2);
        update();
    }

    public void setResource3(String resource3) {
        this.resource3.set(resource3);
        update();
    }

    public void setName(String name) {
        this.name.set(name);
        update();
    }
}
