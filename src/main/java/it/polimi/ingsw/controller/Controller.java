package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.fsm.StateMachine;
import it.polimi.ingsw.parser.JSONSerializer;
import it.polimi.ingsw.server.clienthandling.ClientHandler;
import it.polimi.ingsw.server.clienthandling.ClientHub;
import it.polimi.ingsw.server.clienthandling.Match;
import it.polimi.ingsw.utils.Pair;

import java.util.*;

/**
 * The controller of the game. It consumes actions from the ActionQueue (eventually producing action if internal)
 * and sends the messages to the clients
 */
public class Controller extends Thread{
    private final StateMachine stateMachine;
    private final ActionQueue actionQueue;
    private final ClientHub clientHub;
    private final Match playingMatch;
    private final Map<String, Deque<PayloadComponent>> backup;

    /**
     * Creates the controller with a state machine, an action queue and a clientHub
     * @param stateMachine the state machine of the game
     * @param actionQueue the action queue
     * @param clientHub the client hub
     */
    public Controller(StateMachine stateMachine, ActionQueue actionQueue, ClientHub clientHub, Match playingMatch){
        this.stateMachine = stateMachine;
        this.actionQueue = actionQueue;
        this.clientHub = clientHub;
        this.playingMatch = playingMatch;
        backup = new HashMap<>();
        List<String> playerNames = clientHub.getUsernames();
        for(String username : playerNames){
            backup.put(username, new ArrayDeque<>());
        }
    }

    /**
     * Pops an action from the queue, feeds it in the state machine and returns the list of received messages
     * @return the list of received messages
     * @throws InterruptedException if the thread is terminated when occupied
     */
    public List<Message> consumeMove() throws InterruptedException {
        Action action = actionQueue.pop();
        return stateMachine.executeAction(action);
    }

    //TODO: we may want to send the previously sent messages to the clients that have lost connections (store them in queues?)
    /**
     * Sends all messages to the correct clients (if connected)
     * @param messages the messages to be sent
     */
    public void sendMessages(List<Message> messages){
        Map<String, List<PayloadComponent>> messageDictionary = new HashMap<>();

        for(String user : clientHub.getUsernames())
            messageDictionary.put(user, new ArrayList<>());

        for(Message m : messages){
            for(String target : m.getTargets()){
                    messageDictionary.get(target).addAll(m.getPayloadComponents());
                }
        }
        for(Map.Entry<String, List<PayloadComponent>> entry : messageDictionary.entrySet()){
            if (entry.getValue().size() > 0) {
                System.out.println("-----------------------------------------------------------");
                System.out.println("payloads for: " + entry.getKey());
                for (PayloadComponent component : entry.getValue()) {
                    System.out.println(JSONSerializer.toJson(component));
                }

                //retrieve the clientHandler of the player that need to receive the payload components
                ClientHandler clientHandler = clientHub.getClientByName(entry.getKey()).getSecond();

                //if the client is connected, proceed to send the payloads
                if(clientHandler != null) {
                    if(clientHandler.isFreshClient()){
                        List<PayloadComponent> earlierNecessaryPayloads = new ArrayList<>();
                        for(PayloadComponent earlierComponent : backup.get(entry.getKey())){
                            if(earlierComponent.isNecessary()){
                                earlierNecessaryPayloads.add(earlierComponent);
                            }
                        }
                        clientHandler.sendPayload(earlierNecessaryPayloads);
                        clientHandler.setNotFreshClient();
                    }
                    clientHandler.sendPayload(entry.getValue());
                }

                for(PayloadComponent component : entry.getValue())
                    backup.get(entry.getKey()).addLast(component);
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run(){
        //send the initial messages to the clients
        sendMessages(stateMachine.getCurrentState().onEntry());
        while(true) {
            List<Message> messages = new ArrayList<>();
            try {
                messages.addAll(consumeMove());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            sendMessages(messages);
        }
    }


}



