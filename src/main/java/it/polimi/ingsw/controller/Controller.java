package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.ActionQueue;
import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.common.payload_components.PayloadComponent;
import it.polimi.ingsw.model.actions.Action;
import it.polimi.ingsw.model.fsm.StateMachine;
import it.polimi.ingsw.server.clienthandling.ClientHub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The controller of the game. It consumes actions from the ActionQueue (eventually producing action if internal)
 * and sends the messages to the clients
 */
public class Controller extends Thread{
    private StateMachine stateMachine;
    private ActionQueue actionQueue;
    private ClientHub clientHub;

    /**
     * Creates the controller with a state machine, an action queue and a clientHub
     * @param stateMachine the state machine of the game
     * @param actionQueue the action queue
     * @param clientHub the client hub
     */
    public Controller(StateMachine stateMachine, ActionQueue actionQueue, ClientHub clientHub){
        this.stateMachine = stateMachine;
        this.actionQueue = actionQueue;
        this.clientHub = clientHub;
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

    //TODO: we may want to send to the clients that have lost connections, the preciously sent messages
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
        }

        //TODO: for each entry
}



