package it.polimi.ingsw.client;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.common.payload_components.groups.actions.*;
import it.polimi.ingsw.common.payload_components.groups.setup.CreateMatchSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.JoinMatchSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.SetUsernameSetupPayloadComponent;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.actions.SelectPlayAction;

import java.util.*;

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
                case "back":
                    parseBackCommand(logicalInput);
                    break;
                case "buy_from_market":
                    parseBuyFromMarketCommand(logicalInput);
                    break;
                case "collect_from_basket":
                    parseCollectFromBasketCommand(logicalInput);
                    break;
                case "confirm":
                    parseConfirmCommand(logicalInput);
                    break;
                case "confirm_tidy":
                    parseConfirmTidyCommand(logicalInput);
                    break;
                case "create_match":
                    parseCreateMatchCommand(logicalInput);
                    break;
                case "join_match":
                    parseJoinMatchCommand(logicalInput);
                    break;
                case "resources_move":
                    parseResourcesMoveCommand(logicalInput);
                    break;
                case "select_conversions":
                    parseSelectConversionsCommand(logicalInput);
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

    public void parseConfirmTidyCommand(List<String> logicalInput){
        serverHandler.sendPayload(new ConfirmTidyActionPayloadComponent(serverHandler.getUsername()));
    }

    public void parseConfirmCommand(List<String> logicalInput){
        serverHandler.sendPayload(new ConfirmActionPayloadComponent(serverHandler.getUsername()));
    }

    public void parseResourcesMoveCommand(List<String> logicalInput){
        String origin = logicalInput.get(1);
        String destination = logicalInput.get(2);
        int amount = Integer.parseInt(logicalInput.get(4));
        try {
            String resourcesToMove = ResourceTypeSingleton.getInstance().getResourceSingleByName(logicalInput.get(3)
                    .toLowerCase()).getId();
            serverHandler.sendPayload(new ResourcesMoveActionPayloadComponent(serverHandler.getUsername(), origin, destination,
                    resourcesToMove, amount));
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }

    }

    public void parseCollectFromBasketCommand(List<String> logicalInput){
        int amount = Integer.parseInt(logicalInput.get(2));
        String shelfID = logicalInput.get(3);
        try {
            String resourceToMove = ResourceTypeSingleton.getInstance().getResourceSingleByName(logicalInput.get(1)
                    .toLowerCase()).getId();
            serverHandler.sendPayload(new MoveFromBasketToShelfActionPayloadComponent(serverHandler.getUsername(),
                    resourceToMove, amount, shelfID));
        }catch (NoSuchElementException e){
            System.out.println(e.getMessage());
        }
    }

    public void parseSelectConversionsCommand(List<String> logicalInput){
        List<Integer> actuatorsChoice = new ArrayList<>();
        for(int i = 1; i < logicalInput.size(); i++){
            actuatorsChoice.add(Integer.parseInt(logicalInput.get(i)));
        }
        serverHandler.sendPayload(new SelectConversionsActionPayloadComponent(serverHandler.getUsername(), actuatorsChoice));
    }

    public void parseBuyFromMarketCommand(List<String> logicalInput){
        boolean isRow = Boolean.parseBoolean(logicalInput.get(1));
        int index = Integer.parseInt(logicalInput.get(2));
        serverHandler.sendPayload(new BuyFromMarketActionPayloadComponent(serverHandler.getUsername(), isRow, index));
    }

    public void parseBackCommand(List<String> logicalInput){
        serverHandler.sendPayload(new BackActionPayloadComponent(serverHandler.getUsername()));
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
