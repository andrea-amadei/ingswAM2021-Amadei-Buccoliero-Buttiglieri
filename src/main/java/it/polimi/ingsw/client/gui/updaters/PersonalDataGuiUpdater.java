package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.nodes.ScoreboardBox;
import it.polimi.ingsw.client.model.PersonalData;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class PersonalDataGuiUpdater implements Listener<PersonalData> {

    private final TextArea textArea;

    // TODO: add messages and errors
    public PersonalDataGuiUpdater(TextArea textArea, PersonalData personalData) {
        this.textArea = textArea;

        personalData.addListener(this);
        update(personalData);
    }

    @Override
    public void update(PersonalData personalData) {
        Platform.runLater(() -> {
            StringBuilder str = new StringBuilder();

            for (int i = personalData.getServerMessages().size() - 1; i >= 0; i--) {
                str.append("- ").append(personalData.getServerMessages().get(i)).append('\n');
            }

            textArea.setText(str.toString());
        });
    }
}