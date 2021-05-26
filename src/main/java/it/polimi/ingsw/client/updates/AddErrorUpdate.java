package it.polimi.ingsw.client.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.model.ClientModel;

public class AddErrorUpdate implements Update {
    @SerializedName(value = "message", alternate = "error")
    private final String error;

    public AddErrorUpdate(String error) {
        this.error = error;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        client.getPersonalData().addError(error);
    }

    @Override
    public void checkFormat() {
        if(error == null)
            throw new NullPointerException("Error message not inserted");
    }
}
