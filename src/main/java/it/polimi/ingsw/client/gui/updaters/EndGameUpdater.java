package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.beans.EndGameResultsBean;
import it.polimi.ingsw.client.gui.events.EndGameEvent;
import it.polimi.ingsw.client.gui.nodes.MarketBox;
import it.polimi.ingsw.client.clientmodel.ClientEndGameResults;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;

public class EndGameUpdater implements Listener<ClientEndGameResults> {
    private final MarketBox marketBox;

    public EndGameUpdater(MarketBox marketBox, ClientEndGameResults clientEndGameResults){
        this.marketBox = marketBox;
        clientEndGameResults.addListener(this);
    }
    @Override
    public void update(ClientEndGameResults clientEndGameResults) {
        Platform.runLater(() ->{
            if(clientEndGameResults.isGameEnded()){
                EndGameResultsBean bean = new EndGameResultsBean();
                bean.setHasLorenzoWon(clientEndGameResults.isHasLorenzoWon());
                bean.setUsernames(clientEndGameResults.getUsernames());
                bean.setPoints(clientEndGameResults.getPoints());

                marketBox.fireEvent(new EndGameEvent(bean));
            }else if(clientEndGameResults.isGameCrashed()){
                EndGameResultsBean bean = new EndGameResultsBean();
                bean.setGameCrashed(true);

                marketBox.fireEvent(new EndGameEvent(bean));
            }
        });
    }
}
