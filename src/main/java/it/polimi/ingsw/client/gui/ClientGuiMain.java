package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientDisconnectionManager;
import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.clientmodel.ClientModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ClientGuiMain extends Application {

    public static void main(String[] args) {
        /*try {
            if (args.length == 2){
                Integer.parseInt(args[1]);
            }else if(args.length != 0){
                System.out.println("Wrong parameters format");
                return;
            }
        }catch(RuntimeException e){
            System.out.println("Wrong parameters format");
            return;
        }*/

        launch(args);
    }

    public void start(Stage stage) throws Exception {
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("ESC"));

        stage.setOnCloseRequest(ignored -> {
            Platform.exit();
            System.exit(0);
        });

        GuiSceneManager sceneManager = new GuiSceneManager(stage);

        String host;
        int port;
        List<String> arguments = getParameters().getRaw();
        try {
            if (arguments.size() == 0) {
                host = "localhost";
                port = 6789;
            } else if (arguments.size() == 2){
                host = arguments.get(0);
                port = Integer.parseInt(arguments.get(1));
            }else{
                System.out.println("Wrong parameters format");
                Platform.exit();
                return;
            }
        }catch(RuntimeException e){
            System.out.println("Wrong parameters format");
            Platform.exit();
            return;
        }

        ClientModel model = new ClientModel();

        ServerHandler serverHandler;
        try{
            serverHandler = new ServerHandler(host, port, model, null);
        }catch(IOException e){
            System.out.println("Cannot connect to the server");
            Platform.exit();
            return;
        }
        serverHandler.start();

        GuiBuilder.createStartScene(sceneManager, model, serverHandler);

        ClientDisconnectionManager disconnectionManager = new ClientDisconnectionManager(5000, serverHandler);
        disconnectionManager.start();

        stage.show();
    }
}
