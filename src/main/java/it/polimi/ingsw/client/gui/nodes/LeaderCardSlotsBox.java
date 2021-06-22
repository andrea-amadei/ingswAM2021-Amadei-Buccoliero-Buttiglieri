package it.polimi.ingsw.client.gui.nodes;

import it.polimi.ingsw.client.model.ClientLeaderCards;
import it.polimi.ingsw.parser.raw.RawLeaderCard;
import javafx.beans.property.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class LeaderCardSlotsBox extends HBox {

    private final ObjectProperty<List<RawLeaderCard>> rawLeaderCards;
    private final IntegerProperty coveredCards;
    private final ObjectProperty<List<Boolean>> isActiveList;

    private List<LeaderCardBox> leaderBoxes;

    public LeaderCardSlotsBox(){
        rawLeaderCards = new SimpleObjectProperty<>(this, "rawLeaderCards", new ArrayList<>());
        coveredCards = new SimpleIntegerProperty(this, "coveredCard", 0);
        isActiveList = new SimpleObjectProperty<>(this, "isActiveList", new ArrayList<>());

        leaderBoxes = new ArrayList<>();
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
                newBox.setOnMouseClicked(this::handleCardClick);
                updatedLeaderCardsBox.add(newBox);
            }
        }

        this.leaderBoxes = updatedLeaderCardsBox;
        this.coveredCards.set(clientLeaderCards.getCoveredCards());
        this.rawLeaderCards.set(clientLeaderCards.getLeaderCards());
        this.getChildren().setAll(updatedLeaderCardsBox);

        update();
    }

    private void handleCardClick(MouseEvent event){
        LeaderCardBox selectedCard = (LeaderCardBox)event.getSource();
        if(!selectedCard.isIsCovered()){
            int index = leaderBoxes.indexOf(selectedCard);

            if(event.getButton() == MouseButton.PRIMARY){
                System.out.println("Activating card with index " + index);
            }else if(event.getButton() == MouseButton.SECONDARY){
                System.out.println("Discarding card with index " + index);
            }
        }
    }
}
