package it.polimi.ingsw.clientproto.cliproto;

import it.polimi.ingsw.clientproto.model.PersonalData;
import it.polimi.ingsw.clientproto.observables.Listener;

import java.util.List;

public class PersonalDataCLI implements Listener<PersonalData> {

    //THESE WILL BE FRAMEWORK ELEMENTS
    private String userNameTextbox;
    private List<String> serverMessages;

    public PersonalDataCLI(PersonalData personalData){
        this.userNameTextbox = personalData.getUsername();
        this.serverMessages = personalData.getServerMessages();
        personalData.addListener(this);
    }

    @Override
    public void update(PersonalData personalData) {
        this.userNameTextbox = personalData.getUsername();
        this.serverMessages = personalData.getServerMessages();
    }

    public void draw(){
        System.out.println("-------------------------------");
        System.out.println("Username: " + userNameTextbox);
        System.out.println("-------------------------------");
        System.out.println("-------------------------------");
        System.out.println("Server messages:");
        for(String s : serverMessages)
            System.out.println(s);

        System.out.println("-------------------------------");
    }
}
