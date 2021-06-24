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

    public PlayerGuiUpdater(ScoreboardBox scoreboardBox, CupboardBox cupboardBox, ClientPlayer clientPlayer, int playerNumber) {
        this.scoreboardBox = scoreboardBox;
        this.cupboardBox = cupboardBox;
        this.playerNumber = playerNumber;

        clientPlayer.addListener(this);
        update(clientPlayer);
    }

    @Override
    public void update(ClientPlayer clientPlayer) {
        int i;

        for(i = cupboardBox.getNBase(); i < clientPlayer.getCupboard().size(); i++) {
            new ShelfGuiUpdater(cupboardBox.getBaseShelves().get(i), clientPlayer.getCupboard().get(i));

            int fi = i;
            Platform.runLater(() -> cupboardBox.addBaseShelf(clientPlayer.getCupboard().get(fi).getAcceptedType()));
        }

        for(i = cupboardBox.getNLeader(); i < clientPlayer.getLeaderShelves().size(); i++) {
            new ShelfGuiUpdater(cupboardBox.getLeaderShelves().get(i), clientPlayer.getLeaderShelves().get(i));

            int fi = i;
            Platform.runLater(() -> cupboardBox.addLeaderShelf(clientPlayer.getLeaderShelves().get(fi).getAcceptedType()));
        }

        scoreboardBox.setPlayerPointsProperty(playerNumber - 1, clientPlayer.getVictoryPoints());
    }
}
