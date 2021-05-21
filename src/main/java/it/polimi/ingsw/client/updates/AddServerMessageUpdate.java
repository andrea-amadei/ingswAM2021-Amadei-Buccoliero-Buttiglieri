package it.polimi.ingsw.client.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.model.ClientModel;

public class AddServerMessageUpdate implements Update{

    @SerializedName(value = "text", alternate = "message")
    private final String text;

    public AddServerMessageUpdate(String text){
        this.text = text;
        checkFormat();
    }
    @Override
    public void apply(ClientModel client) {
        client.getPersonalData().addServerMessage(text);
    }

    @Override
    public void checkFormat() {
        if(text == null)
            throw new NullPointerException("Null message form server");
    }
}
