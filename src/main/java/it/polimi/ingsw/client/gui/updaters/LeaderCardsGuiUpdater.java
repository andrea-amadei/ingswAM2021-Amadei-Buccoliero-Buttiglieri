package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.nodes.LeaderCardSlotsBox;
import it.polimi.ingsw.client.clientmodel.ClientLeaderCards;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;

public class LeaderCardsGuiUpdater implements Listener<ClientLeaderCards> {

    LeaderCardSlotsBox leaderCardSlotsBox;

    public LeaderCardsGuiUpdater(LeaderCardSlotsBox leaderCardSlotsBox, ClientLeaderCards clientLeaderCards){
        this.leaderCardSlotsBox = leaderCardSlotsBox;
        update(clientLeaderCards);
        clientLeaderCards.addListener(this);
    }

    @Override
    public void update(ClientLeaderCards clientLeaderCards) {
        Platform.runLater(()->leaderCardSlotsBox.updateLeaderSlots(clientLeaderCards));
    }
}
