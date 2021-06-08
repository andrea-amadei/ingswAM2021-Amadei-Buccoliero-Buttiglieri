package it.polimi.ingsw.client.updates;

import com.google.gson.annotations.SerializedName;
import it.polimi.ingsw.client.ClientGameBuilder;
import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.exceptions.ParserException;

import java.util.List;

public class InitialConfigurationUpdate implements Update{

    @SerializedName(value = "config_json", alternate = "configJson")
    private final String configJson;

    @SerializedName(value = "crafting_json", alternate = "craftingJson")
    private final String craftingJson;

    @SerializedName(value = "faith_json", alternate = "faithJson")
    private final String faithJson;

    @SerializedName(value = "leaders_json", alternate = "leadersJson")
    private final String leadersJson;

    private final List<String> usernames;

    public InitialConfigurationUpdate(String configJson, String craftingJson, String faithJson, String leadersJson, List<String> usernames) {
        this.configJson = configJson;
        this.craftingJson = craftingJson;
        this.faithJson = faithJson;
        this.leadersJson = leadersJson;
        this.usernames = usernames;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {

        //TODO: is it ok to throw an exception here?
        try {
            ClientGameBuilder.buildGame(client, usernames, configJson, craftingJson, faithJson, leadersJson);
        } catch (ParserException e) {
            e.printStackTrace();
            throw new RuntimeException("The server sent an invalid json file (could not be parsed)");
        }

        client.getPersonalData().setGameStarted();
    }

    @Override
    public void checkFormat() {
        if(configJson == null || craftingJson == null || faithJson == null || leadersJson == null || usernames == null)
            throw new NullPointerException("One of the parameters of the initial configuration update is not present");
    }

    public String getConfigJson() {
        return configJson;
    }

    public String getCraftingJson() {
        return craftingJson;
    }

    public String getFaithJson() {
        return faithJson;
    }

    public String getLeadersJson() {
        return leadersJson;
    }

    public List<String> getUsernames() {
        return usernames;
    }
}
