package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.exceptions.IllegalRawConversionException;
import it.polimi.ingsw.exceptions.ParserException;
import it.polimi.ingsw.parser.JSONParser;
import it.polimi.ingsw.parser.raw.RawSpecialAbility;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.HBox;

public class CraftingAbilityBox extends HBox {

    private final StringProperty craftingAbilityJSON;
    private final ObjectProperty<RawSpecialAbility> craftingAbility;

    private CraftingBox crafting;

    public CraftingAbilityBox(){
        String json = "{\"type\":\"crafting\",\"crafting\":{\"input\":{\"shield\":1},\"output\":{\"any\":1},\"faith_output\":1}}";
        try {
            craftingAbility = new SimpleObjectProperty<>(this, "craftingAbility", JSONParser.parseToRaw(json, RawSpecialAbility.class));
        } catch (ParserException | IllegalRawConversionException e) {
            throw new IllegalArgumentException("Conversion from JSON failed.");
        }
        craftingAbilityJSON = new SimpleStringProperty(this, "craftingAbilityJSON", json);
        attachElements();
    }

    public CraftingAbilityBox(RawSpecialAbility rawSpecialAbility){
        craftingAbility = new SimpleObjectProperty<>(this, "craftingAbility", rawSpecialAbility);
        craftingAbilityJSON = new SimpleStringProperty(this, "craftingAbilityJSON", rawSpecialAbility.toString());

        attachElements();
    }

    private void attachElements(){
        crafting = new CraftingBox(craftingAbility.get().getCrafting());

        this.getChildren().addAll(crafting);
    }

    private void update(){
        crafting.setRawCrafting(craftingAbility.get().getCrafting());
    }

    public String getCraftingAbilityJSON() {
        return craftingAbilityJSON.get();
    }

    public StringProperty craftingAbilityJSONProperty() {
        return craftingAbilityJSON;
    }

    public RawSpecialAbility getCraftingAbility() {
        return craftingAbility.get();
    }

    public ObjectProperty<RawSpecialAbility> craftingAbilityProperty() {
        return craftingAbility;
    }

    public CraftingBox getCrafting() {
        return crafting;
    }

    public void setCraftingAbility(RawSpecialAbility craftingAbility) {
        this.craftingAbility.set(craftingAbility);
        if(!craftingAbility.getCrafting().equals(this.craftingAbility.get().getCrafting()))
            update();
    }
}
