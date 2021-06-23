package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.nodes.ScoreboardBox;
import it.polimi.ingsw.client.model.PersonalData;
import it.polimi.ingsw.client.observables.Listener;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;

public class PersonalDataGuiUpdater implements Listener<PersonalData> {

    private final ScoreboardBox scoreboardBox;

    // TODO: add messages and errors
    public PersonalDataGuiUpdater(ScoreboardBox scoreboardBox, PersonalData personalData) {
        this.scoreboardBox = scoreboardBox;

        personalData.addListener(this);
        update(personalData);
    }

    @Override
    public void update(PersonalData personalData) {
    }
}