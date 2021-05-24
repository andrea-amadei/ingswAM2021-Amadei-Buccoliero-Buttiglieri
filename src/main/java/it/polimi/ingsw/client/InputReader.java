package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CliBuilder;
import it.polimi.ingsw.client.cli.framework.CliFramework;
import it.polimi.ingsw.common.payload_components.groups.actions.*;
import it.polimi.ingsw.common.payload_components.groups.setup.CreateMatchSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.JoinMatchSetupPayloadComponent;
import it.polimi.ingsw.common.payload_components.groups.setup.SetUsernameSetupPayloadComponent;
import it.polimi.ingsw.exceptions.UnableToDrawElementException;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.actions.SelectPlayAction;
import it.polimi.ingsw.model.production.Production;

import java.util.*;

public class InputReader extends Thread{

    private String inputString;
    private final ServerHandler serverHandler;
    private final Scanner scanner;
    private final CliFramework framework;

    public InputReader(ServerHandler serverHandler, CliFramework framework) {
        if(serverHandler == null || framework == null)
            throw new NullPointerException();

        this.serverHandler = serverHandler;
        this.framework = framework;

        scanner = new Scanner(System.in);
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
                case "activate_leader":
                    parseActivateLeaderCommand(logicalInput);
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
                case "discard_leader":
                    parseDiscardLeaderCommand(logicalInput);
                    break;
                case "join_match":
                    parseJoinMatchCommand(logicalInput);
                    break;
                case "resources_move":
                    parseResourcesMoveCommand(logicalInput);
                    break;
                case "select_card_from_shop":
                    parseSelectCardFromShopCommand(logicalInput);
                    break;
                case "select_conversions":
                    parseSelectConversionsCommand(logicalInput);
                    break;
                case "select_crafting":
                    parseSelectCraftingCommand(logicalInput);
                    break;
                case "select_output":
                    parseSelectOutputCommand(logicalInput);
                    break;
                case "select_play":
                    parseSelectPlayCommand(logicalInput);
                    break;
                case "select_resources":
                    parseSelectResourcesCommand(logicalInput);
                    break;
                case "start":
                    parseStartCommand(logicalInput);
                    break;
                case "switch":
                    parseSwitchCommand(logicalInput);
                    break;

                default:
                    break;
            }

        }
    }

    private void parseSelectPlayCommand(List<String> logicalInput) {
        try {
            SelectPlayAction.Play play = null;
            String playString = logicalInput.get(1).toUpperCase();
            try {
                play = SelectPlayAction.Play.valueOf(playString);
            } catch (IllegalArgumentException e) {
                System.out.println("There is no \"" + playString + "\" option");
            }
            serverHandler.sendPayload(new SelectPlayActionPayloadComponent(serverHandler.getUsername(), play));
        }catch(RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseUsernameCommand(List<String> logicalInput){
        //TODO: how do we handle invalid command?
        try {
            serverHandler.sendPayload(new SetUsernameSetupPayloadComponent(logicalInput.get(1)));
        }catch(RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseDiscardLeaderCommand(List<String> logicalInput){
        try {
            int id = Integer.parseInt(logicalInput.get(1));
            serverHandler.sendPayload(new DiscardLeaderActionPayloadComponent(serverHandler.getUsername(), id));
        }catch (RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseActivateLeaderCommand(List<String> logicalInput){
        try {
            int id = Integer.parseInt(logicalInput.get(1));
            serverHandler.sendPayload(new ActivateLeaderActionPayloadComponent(serverHandler.getUsername(), id));
        }catch (RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseSelectResourcesCommand(List<String> logicalInput){
        try {
            String containerID = logicalInput.get(1);
            String resource = ResourceTypeSingleton.getInstance().getResourceSingleByName(logicalInput.get(2).toLowerCase()).getId();
            int amount = Integer.parseInt(logicalInput.get(3));
            serverHandler.sendPayload(new SelectResourcesActionPayloadComponent(serverHandler.getUsername(), containerID,
                    resource, amount));
        }catch(RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseSelectOutputCommand(List<String> logicalInput){
        try {
            Map<String, Integer> conversion = new HashMap<>();
            Integer amount;
            String resource;
            for(int i = 1; i < logicalInput.size(); i += 2){
                resource = ResourceTypeSingleton.getInstance().getResourceSingleByName(logicalInput.get(i).toLowerCase())
                .getId();
                amount = Integer.parseInt(logicalInput.get(i + 1));
                conversion.putIfAbsent(resource, 0);
                conversion.put(resource, conversion.get(resource) + amount);
            }
            serverHandler.sendPayload(new SelectCraftingOutputActionPayloadComponent(serverHandler.getUsername(), conversion));
        }catch (RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseSelectCraftingCommand(List<String> logicalInput){
        try {
            Production.CraftingType craftingType = null;
            String craftingTypeString = logicalInput.get(1).toUpperCase();
            int index = Integer.parseInt(logicalInput.get(2));
            try {
                craftingType = Production.CraftingType.valueOf(craftingTypeString);
            } catch (IllegalArgumentException e) {
                System.out.println("There is no \"" + craftingTypeString + "\" option");
            }
            serverHandler.sendPayload(new SelectCraftingActionPayloadComponent(serverHandler.getUsername(), craftingType,
                    index));
        }catch (RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseSelectCardFromShopCommand(List<String> logicalInput){
        try {
            int row = Integer.parseInt(logicalInput.get(1));
            int col = Integer.parseInt(logicalInput.get(2));
            int upgradableCraftingIndex = Integer.parseInt(logicalInput.get(3));
            serverHandler.sendPayload(new SelectCardFromShopActionPayloadComponent(serverHandler.getUsername(), row, col,
                    upgradableCraftingIndex));
        }catch (RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseConfirmTidyCommand(List<String> logicalInput){
        try {
            serverHandler.sendPayload(new ConfirmTidyActionPayloadComponent(serverHandler.getUsername()));
        }catch(RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseConfirmCommand(List<String> logicalInput){
        try {
            serverHandler.sendPayload(new ConfirmActionPayloadComponent(serverHandler.getUsername()));
        }catch(RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseResourcesMoveCommand(List<String> logicalInput){

        try {
            String origin = logicalInput.get(1);
            String destination = logicalInput.get(2);
            int amount = Integer.parseInt(logicalInput.get(4));
            try {
                String resourcesToMove = ResourceTypeSingleton.getInstance().getResourceSingleByName(logicalInput.get(3)
                        .toLowerCase()).getId();
                serverHandler.sendPayload(new ResourcesMoveActionPayloadComponent(serverHandler.getUsername(), origin, destination,
                        resourcesToMove, amount));
            } catch (NoSuchElementException e) {
                System.out.println(e.getMessage());
            }
        }catch(RuntimeException e){
            System.out.println("Command not valid");
        }

    }

    public void parseCollectFromBasketCommand(List<String> logicalInput){
        try {
            int amount = Integer.parseInt(logicalInput.get(2));
            String shelfID = logicalInput.get(3);
            try {
                String resourceToMove = ResourceTypeSingleton.getInstance().getResourceSingleByName(logicalInput.get(1)
                        .toLowerCase()).getId();
                serverHandler.sendPayload(new MoveFromBasketToShelfActionPayloadComponent(serverHandler.getUsername(),
                        resourceToMove, amount, shelfID));
            } catch (NoSuchElementException e) {
                System.out.println(e.getMessage());
            }
        }catch(RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseSelectConversionsCommand(List<String> logicalInput){
        try {
            List<Integer> actuatorsChoice = new ArrayList<>();
            for (int i = 1; i < logicalInput.size(); i++) {
                actuatorsChoice.add(Integer.parseInt(logicalInput.get(i)));
            }
            serverHandler.sendPayload(new SelectConversionsActionPayloadComponent(serverHandler.getUsername(), actuatorsChoice));
        }catch(RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseBuyFromMarketCommand(List<String> logicalInput){
        try {
            boolean isRow = Boolean.parseBoolean(logicalInput.get(1));
            int index = Integer.parseInt(logicalInput.get(2));
            serverHandler.sendPayload(new BuyFromMarketActionPayloadComponent(serverHandler.getUsername(), isRow, index));
        }catch(RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseBackCommand(List<String> logicalInput){
        try {
            serverHandler.sendPayload(new BackActionPayloadComponent(serverHandler.getUsername()));
        }catch(RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseCreateMatchCommand(List<String> logicalInput){
        try {
            String gameName = logicalInput.get(1);
            Integer playerCount = Integer.parseInt(logicalInput.get(2));
            Boolean isSinglePlayer = Boolean.parseBoolean(logicalInput.get(3));

            serverHandler.sendPayload(new CreateMatchSetupPayloadComponent(gameName, playerCount, isSinglePlayer));
        }catch(RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseJoinMatchCommand(List<String> logicalInput){
        try {
            String matchName = logicalInput.get(1);

            serverHandler.sendPayload(new JoinMatchSetupPayloadComponent(matchName));
        }catch(RuntimeException e){
            System.out.println("Command not valid");
        }
    }

    public void parseStartCommand(List<String> logicalInput) {
        try {
            CliBuilder.createGameFrames(framework, serverHandler.getClient());

            framework.setActiveFrame("player_1");
            framework.renderActiveFrame();
        }catch(RuntimeException | UnableToDrawElementException e){
            System.out.println("Command not valid " + e.getMessage());
        }
    }

    public void parseSwitchCommand(List<String> logicalInput) {
        try {
            if(logicalInput.get(1).equalsIgnoreCase("global")) {
                framework.setActiveFrame("global");
                framework.renderActiveFrame();
                return;
            }

            framework.setActiveFrame("player_" + Integer.parseInt(logicalInput.get(1)));
            framework.renderActiveFrame();

        }catch(RuntimeException | UnableToDrawElementException e){
            System.out.println("Command not valid");
        }
    }
}
