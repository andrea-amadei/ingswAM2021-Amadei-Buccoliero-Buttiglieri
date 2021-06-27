package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.dialogs.CustomDialog;
import it.polimi.ingsw.client.gui.dialogs.ErrorDialog;
import it.polimi.ingsw.client.gui.nodes.ScoreboardBox;
import it.polimi.ingsw.client.model.PersonalData;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PersonalDataGuiUpdater implements Listener<PersonalData> {

    private final TextArea textArea;
    private int lastError;
    private final Stage primaryStage;

    public PersonalDataGuiUpdater(TextArea textArea, Stage primaryStage, PersonalData personalData) {
        this.textArea = textArea;
        lastError = 0;
        this.primaryStage = primaryStage;
        personalData.addListener(this);
        update(personalData);
    }

    @Override
    public void update(PersonalData personalData) {

        //Update textArea
        StringBuilder str = new StringBuilder();

        for (int i = personalData.getServerMessages().size() - 1; i >= 0; i--) {
            str.append("- ").append(personalData.getServerMessages().get(i)).append('\n');
        }

        Platform.runLater(() -> {
            textArea.setText(str.toString());
        });

        while(lastError < personalData.getServerErrors().size()){
            String errorMessage = personalData.getServerErrors().get(lastError);
            Platform.runLater(()->{
                CustomDialog dialog = new ErrorDialog(primaryStage, errorMessage);
                dialog.openDialog();
            });
            lastError++;
        }


    }
}