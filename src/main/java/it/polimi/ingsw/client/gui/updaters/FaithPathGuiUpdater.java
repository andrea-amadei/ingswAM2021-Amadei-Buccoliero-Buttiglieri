package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.nodes.FaithPath;
import it.polimi.ingsw.client.model.ClientFaithPath;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.Map;

public class FaithPathGuiUpdater implements Listener<ClientFaithPath> {

    private final FaithPath faithPathElement;
    private final int playerNumber;

    public FaithPathGuiUpdater(FaithPath faithPathElement, ClientFaithPath faithPath, int playerNumber) {
        this.faithPathElement = faithPathElement;
        this.playerNumber = playerNumber;

        faithPath.addListener(this);
        update(faithPath);
    }

    @Override
    public void update(ClientFaithPath clientFaithPath) {
        switch(playerNumber) {
            case 0:
                Platform.runLater(() -> faithPathElement.setLorenzoPositionProperty(clientFaithPath.getLorenzoFaith()));
                break;

            case 1:
                Platform.runLater(() -> {
                    faithPathElement.setPlayer1PositionsProperty(clientFaithPath.getFaithPoints());
                    faithPathElement.setPlayer1CheckpointsStatusProperty(FXCollections.observableArrayList(clientFaithPath.getCheckpointStatus()));
                });
                break;

            case 2:
                Platform.runLater(() -> {
                    faithPathElement.setPlayer2PositionsProperty(clientFaithPath.getFaithPoints());
                    faithPathElement.setPlayer2CheckpointsStatusProperty(FXCollections.observableArrayList(clientFaithPath.getCheckpointStatus()));
                });
                break;

            case 3:
                Platform.runLater(() -> {
                    faithPathElement.setPlayer3PositionsProperty(clientFaithPath.getFaithPoints());
                    faithPathElement.setPlayer3CheckpointsStatusProperty(FXCollections.observableArrayList(clientFaithPath.getCheckpointStatus()));
                });
                break;

            case 4:
                Platform.runLater(() -> {
                    faithPathElement.setPlayer4PositionsProperty(clientFaithPath.getFaithPoints());
                    faithPathElement.setPlayer4CheckpointsStatusProperty(FXCollections.observableArrayList(clientFaithPath.getCheckpointStatus()));
                });
                break;
        }
    }
}
