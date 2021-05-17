package it.polimi.ingsw.client;

import it.polimi.ingsw.common.payload_components.groups.setup.SetUsernameSetupPayloadComponent;

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
                default:
                    break;
            }

        }
    }

    public void parseUsernameCommand(List<String> logicalInput){
        //TODO: how do we handle invalid command?
        serverHandler.sendPayload(new SetUsernameSetupPayloadComponent(logicalInput.get(1)));
    }
}
