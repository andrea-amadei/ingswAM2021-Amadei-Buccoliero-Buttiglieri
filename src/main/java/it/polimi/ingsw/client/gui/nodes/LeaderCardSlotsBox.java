package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.gui.beans.LeaderInteractionBean;
import it.polimi.ingsw.client.gui.events.LeaderInteractionEvent;
import it.polimi.ingsw.client.model.ClientLeaderCards;
import it.polimi.ingsw.gamematerials.*;
import it.polimi.ingsw.model.leader.*;
import it.polimi.ingsw.model.market.ConversionActuator;
import it.polimi.ingsw.model.production.Crafting;
import it.polimi.ingsw.model.storage.Shelf;
import it.polimi.ingsw.parser.raw.RawLeaderCard;
import javafx.beans.property.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Scale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LeaderCardSlotsBox extends HBox {

    private final ObjectProperty<List<RawLeaderCard>> rawLeaderCards;
    private final IntegerProperty coveredCards;
    private final ObjectProperty<List<Boolean>> isActiveList;
    private final BooleanProperty areControlsDisabled;
    private final Scale scale;

    private List<LeaderCardBox> leaderBoxes;

    //TODO: remove dumb stuff
    public LeaderCardSlotsBox(){
        rawLeaderCards = new SimpleObjectProperty<>(this, "rawLeaderCards", new ArrayList<>());
        coveredCards = new SimpleIntegerProperty(this, "coveredCard", 0);
        isActiveList = new SimpleObjectProperty<>(this, "isActiveList", new ArrayList<>());

        leaderBoxes = new ArrayList<>();
        areControlsDisabled = new SimpleBooleanProperty(this, "areControlsDisabled", true);

        //set initial scale
        scale = new Scale();
        this.getTransforms().add(scale);

        scale.setX(0.75);
        scale.setY(0.75);

        ClientLeaderCards clientLeaderCards = new ClientLeaderCards();
        clientLeaderCards.changeCoveredCardsNumber(2);
        clientLeaderCards.addLeaderCard(
                new RawLeaderCard(
                        new LeaderCard(1, "Lorenzo", 12,
                                new ArrayList<>() {{
                                    add(new ConversionAbility(MarbleColor.WHITE, new ConversionActuator(Arrays.asList(
                                            ResourceTypeSingleton.getInstance().getServantResource(),
                                            ResourceTypeSingleton.getInstance().getShieldResource()), 1)));
                                    add(new DiscountAbility(2, ResourceTypeSingleton.getInstance().getGoldResource()));
                                    add(new StorageAbility(new Shelf("leader", ResourceTypeSingleton.getInstance().getStoneResource(), 2)));
                                }},
                                new ArrayList<>() {{
                                    add(new FlagRequirement(new BaseFlag(FlagColor.BLUE), 1));
                                    add(new FlagRequirement(new BaseFlag(FlagColor.GREEN), 1));
                                    add(new LevelFlagRequirement(new LevelFlag(FlagColor.YELLOW, 1), 1));
                                    add(new ResourceRequirement(ResourceTypeSingleton.getInstance().getServantResource(), 2));
                                }})
                )
        );
        clientLeaderCards.addLeaderCard(
                new RawLeaderCard(
                        new LeaderCard(2, "Paolo", 9,
                                new ArrayList<>() {{
                                    add(new CraftingAbility(new Crafting(
                                            new HashMap<>() {{
                                                put(ResourceTypeSingleton.getInstance().getServantResource(), 2);
                                                put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                                                put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
                                                put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
                                            }},
                                            new HashMap<>() {{
                                                put(ResourceTypeSingleton.getInstance().getServantResource(), 2);
                                                put(ResourceTypeSingleton.getInstance().getGoldResource(), 2);
                                                put(ResourceTypeSingleton.getInstance().getShieldResource(), 2);
                                                put(ResourceTypeSingleton.getInstance().getStoneResource(), 2);
                                            }},
                                            2
                                    )));
                                    add(new DiscountAbility(2, ResourceTypeSingleton.getInstance().getGoldResource()));
                                }},
                                new ArrayList<>() {{
                                    add(new FlagRequirement(new BaseFlag(FlagColor.BLUE), 1));
                                    add(new FlagRequirement(new BaseFlag(FlagColor.GREEN), 1));
                                }})
                )
        );

        updateLeaderSlots(clientLeaderCards);

    }

    private void update(){
        //update the discovered boxes(it will be effective only if the new leader card is different from the old one)
        for(int i = 0; i < rawLeaderCards.get().size(); i++){
            leaderBoxes.get(i).setRawLeaderCard(rawLeaderCards.get().get(i));
            leaderBoxes.get(i).setIsCovered(false);
        }

        //set the correct amount of leaderBoxes as covered
        for(int i = rawLeaderCards.get().size(); i < coveredCards.get(); i++){
            leaderBoxes.get(i).setIsCovered(true);
        }

    }

    public void updateLeaderSlots(ClientLeaderCards clientLeaderCards){
        List<LeaderCardBox> updatedLeaderCardsBox = new ArrayList<>();

        //add new boxes if necessary
        int newCoveredCardAmount = clientLeaderCards.getCoveredCards() - coveredCards.get();
        int newDiscoveredCardAmount = clientLeaderCards.getLeaderCards().size() - rawLeaderCards.get().size();

        int deltaBoxes = newCoveredCardAmount + newDiscoveredCardAmount;

        for(int i = 0; i < leaderBoxes.size() + deltaBoxes; i++){
            if(i < leaderBoxes.size()){
                updatedLeaderCardsBox.add(leaderBoxes.get(i));
            }else{
                LeaderCardBox newBox = new LeaderCardBox();
                newBox.setOnMousePressed(this::handleCardClick);
                updatedLeaderCardsBox.add(newBox);
            }
        }

        this.leaderBoxes = updatedLeaderCardsBox;
        this.coveredCards.set(clientLeaderCards.getCoveredCards());
        this.rawLeaderCards.set(clientLeaderCards.getLeaderCards());
        this.getChildren().setAll(updatedLeaderCardsBox);

        update();
    }

    private void handleCardClick(MouseEvent event) {
        if (!areControlsDisabled.get()) {
            LeaderCardBox selectedCard = (LeaderCardBox) event.getSource();
            if (!selectedCard.isIsCovered()) {
                int index = leaderBoxes.indexOf(selectedCard);
                boolean isActivate;
                if (event.getButton() == MouseButton.PRIMARY) {
                    isActivate = true;
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    isActivate = false;
                }
                else
                    return;

                LeaderInteractionBean bean = new LeaderInteractionBean();
                bean.setActivate(isActivate);
                bean.setIndex(index);
                fireEvent(new LeaderInteractionEvent(bean));
            }

        }
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


