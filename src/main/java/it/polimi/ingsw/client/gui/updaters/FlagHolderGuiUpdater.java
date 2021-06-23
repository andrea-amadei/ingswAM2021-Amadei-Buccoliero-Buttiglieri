package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.nodes.FaithPath;
import it.polimi.ingsw.client.gui.nodes.ScoreboardBox;
import it.polimi.ingsw.client.model.ClientFaithPath;
import it.polimi.ingsw.client.model.ClientFlagHolder;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;
import javafx.collections.FXCollections;

import java.util.List;

public class FlagHolderGuiUpdater implements Listener<ClientFlagHolder> {
    private final ScoreboardBox scoreboardBox;
    private final int playerNumber;

    public FlagHolderGuiUpdater(ScoreboardBox scoreboardBox, ClientFlagHolder flagHolder, int playerNumber) {
        this.scoreboardBox = scoreboardBox;
        this.playerNumber = playerNumber;

        flagHolder.addListener(this);
        update(flagHolder);
    }

    @Override
    public void update(ClientFlagHolder clientFlagHolder) {
        Platform.runLater(() -> scoreboardBox.setPlayerFlagsProperty(playerNumber - 1, List.copyOf(clientFlagHolder.getFlags())));
    }
}
