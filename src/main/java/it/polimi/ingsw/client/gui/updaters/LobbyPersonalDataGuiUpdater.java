package it.polimi.ingsw.client.gui.updaters;

import it.polimi.ingsw.client.cli.framework.elements.Frame;
import it.polimi.ingsw.client.model.PersonalData;
import it.polimi.ingsw.client.observables.Listener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class LobbyPersonalDataGuiUpdater implements Listener<PersonalData> {

    private Scene scene;
    private PersonalData personalData;

    public LobbyPersonalDataGuiUpdater(PersonalData personalData, Scene scene) {
        if(scene == null || personalData == null)
            throw new NullPointerException();

        this.scene = scene;
        this.personalData = personalData;

        personalData.addListener(this);

        setup(personalData);
        update(personalData);
    }

    public void setup(PersonalData personalData) {
        ObservableList<String> data = FXCollections.observableArrayList("Singleplayer", "2", "3", "4");

        @SuppressWarnings("unchecked")
        ComboBox<String> comboBox = (ComboBox<String>) scene.lookup("#n_players");
        comboBox.setItems(data);
    }

    @Override
    public void update(PersonalData personalData) {
        TextField username = (TextField) scene.lookup("#username");
        TextField match = (TextField) scene.lookup("#match");

        Button setUsername = (Button) scene.lookup("#set_username");
        Button createMatch = (Button) scene.lookup("#create_match");
        Button joinMatch = (Button) scene.lookup("#join_match");

        Button start = (Button) scene.lookup("#start");
        Label startLabel = (Label) scene.lookup("#start_label");

        @SuppressWarnings("unchecked")
        ComboBox<String> nPlayers = (ComboBox<String>) scene.lookup("#n_players");
        Label createMatchLabel = (Label) scene.lookup("#create_match_label");
        Button createMatchButton = (Button) scene.lookup("#create_match_button");

        Platform.runLater(() -> {
            if(!personalData.getUsername().equals("Unknown")) {
                username.setText(personalData.getUsername());
                username.setDisable(true);
                setUsername.setDisable(true);

                createMatch.setDisable(false);
                joinMatch.setDisable(false);
                match.setDisable(false);
            }

            if(!personalData.getGameName().equals("Unknown"))
                match.setText(personalData.getGameName());

            if(personalData.isGameStarted()) {
                start.setDisable(false);
                startLabel.setVisible(false);

                match.setDisable(true);
                createMatch.setDisable(true);
                joinMatch.setDisable(true);

                nPlayers.setDisable(true);
                nPlayers.setVisible(false);
                createMatchButton.setDisable(true);
                createMatchButton.setVisible(false);
                createMatchLabel.setVisible(false);
            }

            StringBuilder str = new StringBuilder();
            for (int i = personalData.getServerMessages().size() - 1; i >= 0; i--) {
                str.append("- ").append(personalData.getServerMessages().get(i)).append('\n');
            }

            ((TextArea) scene.lookup("#messages")).setText(str.toString());
        });
    }
}
