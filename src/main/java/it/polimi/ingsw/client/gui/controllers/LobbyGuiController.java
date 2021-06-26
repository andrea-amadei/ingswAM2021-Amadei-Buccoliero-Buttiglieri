package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.ClientGameBuilder;
import it.polimi.ingsw.client.gui.GuiBuilder;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.common.payload_components.groups.setup.CreateMatchSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.JoinMatchSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.ReconnectSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.SetUsernameSetupPayloadComponent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.stream.Collectors;

public class LobbyGuiController extends BaseController {

    @FXML
    public TextField username;

    @FXML
    public Button reconnectBtn;

    @FXML
    private Button set_username;

    @FXML
    public TextField match;
    @FXML
    public Button create_match;
    @FXML
    public Button join_match;

    @FXML
    public Label create_match_label;
    @FXML
    public ComboBox<String> n_players;
    @FXML
    public Button create_match_button;

    @FXML
    public Button start;

    @FXML
    public TextArea messages;

    @FXML
    private void changeUsernameButtonAction() {
        getServerHandler().sendPayload(new SetUsernameSetupPayloadComponent(username.getText()));
    }

    @FXML
    private void openMatchButtonAction() {
        create_match_label.setVisible(true);
        create_match_label.setDisable(false);

        n_players.setVisible(true);
        n_players.setDisable(false);

        create_match_button.setVisible(true);
    }

    @FXML
    private void startGameButtonAction() {
        GuiBuilder.createGameScene(getSceneManager(), getModel(), getServerHandler());
    }

    @FXML
    private void selectedItemAction() {
        create_match_button.setDisable(false);
    }

    @FXML
    private void createMatchButtonAction() {
        boolean single;
        int n;

        if(n_players.getValue().equalsIgnoreCase("Singleplayer")) {
            n = 1;
            single = true;
        }
        else {
            n = Integer.parseInt(n_players.getValue());
            single = false;
        }


        getServerHandler().sendPayload(new CreateMatchSetupPayloadComponent(match.getText(), n, single));
    }

    @FXML
    private void joinMatchActionButton() {
        getServerHandler().sendPayload(new JoinMatchSetupPayloadComponent(match.getText()));
    }

    @FXML
    public void reconnectButtonAction(ActionEvent actionEvent) {
        if(!username.getText().equals("")){
            getServerHandler().sendPayload(new ReconnectSetupPayloadComponent(username.getText()));
        }
    }
}
