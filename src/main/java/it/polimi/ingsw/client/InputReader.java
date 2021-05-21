package it.polimi.ingsw.client;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.common.payload_components.groups.actions.SelectPlayActionPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.CreateMatchSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.JoinMatchSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.SetUsernameSetupPayloadComponent;
import it.polimi.ingsw.model.actions.SelectPlayAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputReader extends Thread{

    private String inputString;
    private final CommandExecutor commandExecutor;
    private final ServerHandler serverHandler;
    private final Scanner scanner;

    public InputReader(CommandExecutor commandExecutor, ServerHandler serverHandler){
        inputString = null;
        this.commandExecutor = commandExecutor;
        scanner = new Scanner(System.in);
        this.serverHandler = serverHandler;
    }

    @Override
    public void run(){
        while(true){
            inputString = scanner.nextLine();
            List<String> logicalInput = new ArrayList<>(Arrays.asList(inputString.split(" ")));
            switch(logicalInput.get(0)){
                case "set_username" :
                    parseUsernameCommand(logicalInput);
                    break;
                case "create_match":
                    parseCreateMatchCommand(logicalInput);
                    break;
                case "join_match":
                    parseJoinMatchCommand(logicalInput);
                    break;
                case "select_play":
                    parseSelectPlayCommand(logicalInput);

                default:
                    break;
            }

        }
    }

    private void parseSelectPlayCommand(List<String> logicalInput) {
        SelectPlayAction.Play play;
        String playString = logicalInput.get(1).toUpperCase();
        try {
            play = SelectPlayAction.Play.valueOf(playString);
            serverHandler.sendPayload(new SelectPlayActionPayloadComponent(serverHandler.getUsername(), play));
        }catch(IllegalArgumentException e){
            System.out.println("There is no \"" + playString + "\" option");
        }
    }

    public void parseUsernameCommand(List<String> logicalInput){
        //TODO: how do we handle invalid command?
        serverHandler.sendPayload(new SetUsernameSetupPayloadComponent(logicalInput.get(1)));
    }

    public void parseCreateMatchCommand(List<String> logicalInput){
        String gameName = logicalInput.get(1);
        Integer playerCount = Integer.parseInt(logicalInput.get(2));
        Boolean isSinglePlayer = Boolean.parseBoolean(logicalInput.get(3));

        serverHandler.sendPayload(new CreateMatchSetupPayloadComponent(gameName, playerCount, isSinglePlayer));
    }

    public void parseJoinMatchCommand(List<String> logicalInput){
        String matchName = logicalInput.get(1);

        serverHandler.sendPayload(new JoinMatchSetupPayloadComponent(matchName));
    }
}
