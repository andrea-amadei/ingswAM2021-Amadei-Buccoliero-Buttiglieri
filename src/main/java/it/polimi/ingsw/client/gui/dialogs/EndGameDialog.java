package it.polimi.ingsw.client.gui.dialogs;

import it.polimi.ingsw.client.gui.beans.EndGameResultsBean;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class EndGameDialog extends CustomDialog{
    private EndGameResultsBean bean;

    public EndGameDialog(Stage primaryStage, EndGameResultsBean bean, Runnable callback){
        super(primaryStage);
        this.bean = bean;

        Label title = new Label("Game Ended!");
        title.setFont(new Font(20));
        AnchorPane.setLeftAnchor(title, 240d);
        AnchorPane.setTopAnchor(title, 50d);

        Label winnerLabel = new Label("The winner is: " + ( (bean.isHasLorenzoWon()) ? "Lorenzo" : (bean.getUsernames().get(0) + " ("  + bean.getPoints().get(0) + " points)") ));
        winnerLabel.setFont(new Font(16));
        AnchorPane.setLeftAnchor(winnerLabel, 100d);
        AnchorPane.setTopAnchor(winnerLabel, 115d);

        List<Label> playerLabels = new ArrayList<>();
        if(bean.isHasLorenzoWon()){
            Label singlePlayerLostLabel = new Label("2) " + bean.getUsernames().get(0) + " (" + bean.getPoints().get(0) + " points)");
            AnchorPane.setLeftAnchor(singlePlayerLostLabel, 100d);
            AnchorPane.setTopAnchor(singlePlayerLostLabel, 155d);
            playerLabels.add(singlePlayerLostLabel);
        }else {
            for(int i = 1; i < bean.getUsernames().size(); i++){
                Label playerScoreLabel = new Label("" + (i+1) + ") " + bean.getUsernames().get(i) + " (" + bean.getPoints().get(i) + " points)");
                AnchorPane.setLeftAnchor(playerScoreLabel, 100d);
                AnchorPane.setTopAnchor(playerScoreLabel, 155d + (i-1) * 30d);
                playerLabels.add(playerScoreLabel);
            }
        }

        Button quitBtn = new Button("Quit");
        AnchorPane.setRightAnchor(quitBtn, 50d);
        AnchorPane.setBottomAnchor(quitBtn, 30d);
        quitBtn.setOnAction(e -> {
            closeDialog();
            callback.run();
        });


        getRoot().setPrefSize(600, 350);
        getRoot().getChildren().setAll(title, winnerLabel, quitBtn);
        getRoot().getChildren().addAll(playerLabels);
    }
}
