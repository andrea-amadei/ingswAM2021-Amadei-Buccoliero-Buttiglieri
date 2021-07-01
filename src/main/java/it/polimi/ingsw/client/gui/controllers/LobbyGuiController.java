package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GuiBuilder;
import it.polimi.ingsw.common.payload_components.groups.setup.*;
import it.polimi.ingsw.server.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LobbyGuiController extends BaseController {

    @FXML
    public TextField username;

    @FXML
    public Button reconnectBtn;

    public CheckBox customCheck;

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
        customCheck.setVisible(true);
    }

    @FXML
    private void startGameButtonAction() {
        GuiBuilder.createGameScene(getSceneManager(), getModel(), getServerHandler());
    }

    @FXML
    private void selectedItemAction() {
        create_match_button.setDisable(false);
        customCheck.setDisable(false);
    }

    @FXML
    private void createMatchButtonAction(){
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

        if(customCheck.isSelected()){
            try {
                //reading files from disk
                List<String> fileNames = Arrays.asList("config.json", "crafting.json", "faith.json", "leaders.json");
                List<String> fileContents = new ArrayList<>();
                for (String fileName : fileNames) {
                    File parentDirectory = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
                    Path jsonFilePath = new File(parentDirectory, fileName).toPath();
                    String json = Files.readString(jsonFilePath);
                    fileContents.add(json);
                }
                getServerHandler().sendPayload(new CreateCustomMatchSetupPayloadComponent(match.getText(), n, single,
                        fileContents.get(0), fileContents.get(1), fileContents.get(2), fileContents.get(3)));
            } catch (URISyntaxException | IOException e) {
                Logger.log("Can't read the files");
            }
        }else {
            getServerHandler().sendPayload(new CreateMatchSetupPayloadComponent(match.getText(), n, single));
        }
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
