package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.events.ChangedCurrentPlayerEvent;
import it.polimi.ingsw.client.gui.nodes.MenuBox;
import it.polimi.ingsw.client.gui.nodes.ScoreboardBox;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;

public class TurnGuiUpdater implements Listener<ClientModel> {
    private final MenuBox menuBox;
    private final ScoreboardBox scoreboardBox;

    public TurnGuiUpdater(MenuBox menuBox, ScoreboardBox scoreboardBox, ClientModel clientModel){
        this.menuBox = menuBox;
        this.scoreboardBox = scoreboardBox;
        clientModel.addListener(this);
        if(clientModel.getCurrentPlayer() != null){
            update(clientModel);
        }
    }
    @Override
    public void update(ClientModel clientModel) {
        Platform.runLater(() -> {
            menuBox.fireEvent(new ChangedCurrentPlayerEvent(clientModel.getCurrentPlayer().getUsername()));
            scoreboardBox.setCurrentPlayer(clientModel.getPlayers().indexOf(clientModel.getCurrentPlayer()) + 1);
        });
    }
}
