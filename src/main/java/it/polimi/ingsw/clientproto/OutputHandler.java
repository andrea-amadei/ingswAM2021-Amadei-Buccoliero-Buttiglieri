package it.polimi.ingsw.clientproto;

import it.polimi.ingsw.clientproto.cliproto.PersonalDataCLI;
import it.polimi.ingsw.clientproto.model.ClientModel;

import java.util.Optional;

public class OutputHandler {
    private ClientModel clientModel;
    private PersonalDataCLI personalDataCLI;

    public void setModel(ClientModel clientModel){
        this.clientModel = clientModel;
        this.personalDataCLI = new PersonalDataCLI(clientModel.getPersonalData());
    }


    public void update(){
        for(int i = 0; i < 15; i++)
            System.out.println();

        personalDataCLI.draw();

        for(int i = 0; i < 15; i++)
            System.out.println();
    }
}
