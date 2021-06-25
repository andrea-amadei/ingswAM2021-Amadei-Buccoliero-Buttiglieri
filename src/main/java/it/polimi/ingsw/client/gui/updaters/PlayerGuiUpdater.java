package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.nodes.CupboardBox;
import it.polimi.ingsw.client.gui.nodes.ScoreboardBox;
import it.polimi.ingsw.client.model.ClientFlagHolder;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;

import java.util.List;

public class PlayerGuiUpdater implements Listener<ClientPlayer> {

    private final ScoreboardBox scoreboardBox;
    private final CupboardBox cupboardBox;
    private final int playerNumber;

    private int nBase;
    private int nLeader;

    public PlayerGuiUpdater(ScoreboardBox scoreboardBox, CupboardBox cupboardBox, ClientPlayer clientPlayer, int playerNumber) {
        this.scoreboardBox = scoreboardBox;
        this.cupboardBox = cupboardBox;
        this.playerNumber = playerNumber;

        nBase = 0;
        nLeader = 0;

        clientPlayer.addListener(this);
        update(clientPlayer);
    }

    @Override
    public void update(ClientPlayer clientPlayer) {
        int i;

        for(i = nBase; i < clientPlayer.getCupboard().size(); i++) {
            new ShelfGuiUpdater(cupboardBox.getBaseShelves().get(i), clientPlayer.getCupboard().get(i));

            int fi = i;
            int fnBase = nBase;
            Platform.runLater(() -> cupboardBox.addBaseShelf(clientPlayer.getCupboard().get(fi).getAcceptedType(), fnBase));
            nBase++;
        }

        for(i = nLeader; i < clientPlayer.getLeaderShelves().size(); i++) {
            new ShelfGuiUpdater(cupboardBox.getLeaderShelves().get(i), clientPlayer.getLeaderShelves().get(i));

            int fi = i;
            int fnLeader = nLeader;
            Platform.runLater(() -> cupboardBox.addLeaderShelf(clientPlayer.getLeaderShelves().get(fi).getAcceptedType(), fnLeader));
            nLeader++;
        }

        Platform.runLater(()->scoreboardBox.setPlayerPointsProperty(playerNumber - 1, clientPlayer.getVictoryPoints()));
    }
}
