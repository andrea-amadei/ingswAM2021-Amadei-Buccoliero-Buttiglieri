package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.parser.raw.RawLeaderCard;
import it.polimi.ingsw.parser.raw.RawRequirement;
import it.polimi.ingsw.parser.raw.RawSpecialAbility;
import it.polimi.ingsw.parser.raw.RawStorage;
import it.polimi.ingsw.server.Logger;
import it.polimi.ingsw.utils.Pair;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LeaderCardBox extends VBox {

    private final BooleanProperty isCovered;
    private final ObjectProperty<RawLeaderCard> rawLeaderCard;

    public LeaderCardBox(){
        this(null, true);
    }

    public LeaderCardBox(RawLeaderCard rawLeaderCard, boolean isCovered){
        this.isCovered = new SimpleBooleanProperty(this, "isCovered", isCovered);
        this.rawLeaderCard = new SimpleObjectProperty<>(this, "rawLeaderCard", rawLeaderCard);

        //setting up  the VBox
        this.setAlignment(Pos.TOP_CENTER);
        this.setPrefHeight(550d);
        this.setPrefWidth(200d);
        this.setStyle("-fx-background-color: #adadad; -fx-border-color: black; -fx-border-insets: 3; -fx-border-width: 3");
        update();
    }

    private void update(){
        if(!isCovered.get() && rawLeaderCard.get() != null){
            this.getChildren().clear();
            System.out.println("Update!");
            RawLeaderCard currentCard = rawLeaderCard.get();
            Map<String, Integer> resourceRequirements = new HashMap<>();
            List<RawRequirement> levelFlagRequirements = new ArrayList<>();
            List<RawRequirement> flagRequirements = new ArrayList<>();

            for(RawRequirement requirement : currentCard.getRequirements()){
                switch(requirement.getType()){
                    case "resource":
                        String resource = requirement.getResource().toLowerCase();
                        resourceRequirements.putIfAbsent(resource, 0);
                        resourceRequirements.put(resource, resourceRequirements.get(resource) + requirement.getAmount());
                        break;
                    case "level_flag":
                        levelFlagRequirements.add(requirement);
                        break;
                    case "flag":
                        flagRequirements.add(requirement);
                        break;
                    default:
                        Logger.log("Wrong requirement type '" + requirement.getType() + "'");
                }
            }

            //name setup
            Label nameLabel = new Label(currentCard.getName() + " (id: " + currentCard.getId() + ")");
            nameLabel.setFont(new Font("Arial", 22));
            this.getChildren().add(nameLabel);

            //create a HBox for the requirements
            HBox hBox = new HBox();

            //add all the level flag requirements
            for(RawRequirement levelFlagRequirement : levelFlagRequirements){
                String flag = levelFlagRequirement.getFlag().name().toLowerCase();
                int level = levelFlagRequirement.getLevel();
                int amount = levelFlagRequirement.getAmount();
                FlagBox flagBox = new FlagBox(flag, level, amount, true, false, true);
                hBox.getChildren().add(flagBox);
            }

            //add all the level requirements
            for(RawRequirement flagRequirement : flagRequirements){
                String flag = flagRequirement.getFlag().name().toLowerCase();
                int amount = flagRequirement.getAmount();
                FlagBox flagBox = new FlagBox(flag, 0, amount, true, false, true);
                hBox.getChildren().add(flagBox);
            }

            //set the resource container with the required resources
            RawStorage requirementStorage = new RawStorage("requirements", resourceRequirements);
            HResourceContainer resourceRequirementsBox = new HResourceContainer(requirementStorage, true, false, true, false);
            hBox.getChildren().add(resourceRequirementsBox);

            //add the requirement HBox to the vbox
            this.getChildren().add(hBox);

            //add the first region to center elements vertically
            Region topRegion = new Region();
            VBox.setVgrow(topRegion, Priority.ALWAYS);
            this.getChildren().add(topRegion);

            //add all the special abilities
            for(RawSpecialAbility specialAbility : currentCard.getAbilities()){
                //add the container for the ability
                HBox abilityContainer = new HBox();
                VBox.setMargin(abilityContainer, new Insets(20, 0, 0, 0));
                this.getChildren().add(abilityContainer);

                //add the left region to the container
                Region leftRegion = new Region();
                HBox.setHgrow(leftRegion, Priority.ALWAYS);
                abilityContainer.getChildren().add(leftRegion);

                //add the ability box
                switch(specialAbility.getType()){
                    case "crafting":
                        CraftingAbilityBox craftingAbilityBox = new CraftingAbilityBox(specialAbility);
                        abilityContainer.getChildren().add(craftingAbilityBox);
                        break;
                    case "conversion":
                        ConversionAbilityBox conversionAbilityBox = new ConversionAbilityBox(specialAbility);
                        abilityContainer.getChildren().add(conversionAbilityBox);
                        break;
                    case "storage":
                        StorageAbilityBox storageAbilityBox = new StorageAbilityBox(specialAbility);
                        abilityContainer.getChildren().add(storageAbilityBox);
                        break;
                    case "discount":
                        DiscountAbilityBox discountAbilityBox = new DiscountAbilityBox(specialAbility);
                        abilityContainer.getChildren().add(discountAbilityBox);
                        break;
                    default:
                        Logger.log("Wrong special ability type: '" + specialAbility.getType() + "'");
                }

                //add right region to the container
                Region rightRegion = new Region();
                HBox.setHgrow(rightRegion, Priority.ALWAYS);
                abilityContainer.getChildren().add(rightRegion);
            }

            //add the second region to vertically center the abilities
            Region bottomRegion = new Region();
            VBox.setVgrow(bottomRegion, Priority.ALWAYS);
            this.getChildren().add(bottomRegion);

            //add the points
            PointsBox pointsBox = new PointsBox(currentCard.getPoints());
            this.getChildren().add(pointsBox);
        }
    }

    public boolean isIsCovered() {
        return isCovered.get();
    }

    public BooleanProperty isCoveredProperty() {
        return isCovered;
    }

    public RawLeaderCard getRawLeaderCard() {
        return rawLeaderCard.get();
    }

    public ObjectProperty<RawLeaderCard> rawLeaderCardProperty() {
        return rawLeaderCard;
    }

    public void setIsCovered(boolean isCovered) {
        if(isIsCovered() != isCovered) {
            this.isCovered.set(isCovered);
            if(isCovered){
                this.getChildren().clear();
            }else{
                update();
            }
        }
    }

    public void setRawLeaderCard(RawLeaderCard rawLeaderCard) {
        if(getRawLeaderCard() == null || getRawLeaderCard().getId() != rawLeaderCard.getId()) {
            this.rawLeaderCard.set(rawLeaderCard);
            update();
        }
    }
}
