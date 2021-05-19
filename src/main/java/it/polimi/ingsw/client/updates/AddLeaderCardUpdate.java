package it.polimi.ingsw.client.updates;

import it.polimi.ingsw.client.model.ClientModel;
import it.polimi.ingsw.client.model.ClientPlayer;
import it.polimi.ingsw.parser.raw.RawLeaderCard;

public class AddLeaderCardUpdate implements Update{

    private final int id;
    private final String player;

    public AddLeaderCardUpdate(int id, String player){
        this.id = id;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {
        ClientPlayer clientPlayer = client.getPlayerByName(player);
        RawLeaderCard toBeAdded = client.getLeaderCardById(id);
        clientPlayer.getLeaderCards().addLeaderCard(toBeAdded);
    }

    @Override
    public void checkFormat() {
        if(id < 0)
            throw new IllegalArgumentException("leader ID cannot be negative");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public int getId() {
        return id;
    }

    public String getPlayer() {
        return player;
    }
}
