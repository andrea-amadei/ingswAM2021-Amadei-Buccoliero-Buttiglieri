package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.gui.nodes.MenuBox;
import it.polimi.ingsw.client.clientmodel.PersonalData;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;

public class MenuGuiUpdater implements Listener<PersonalData> {

    MenuBox menuBox;

    public MenuGuiUpdater(MenuBox menuBox, PersonalData personalData){
        this.menuBox = menuBox;
        personalData.addListener(this);
        update(personalData);
    }

    @Override
    public void update(PersonalData personalData) {
        Platform.runLater(() -> menuBox.setPossibleActions(personalData.getPossibleActions()));
    }
}
