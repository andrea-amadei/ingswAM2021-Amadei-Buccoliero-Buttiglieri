package it.polimi.ingsw.clientproto.updates;

import it.polimi.ingsw.clientproto.model.ClientModel;

public class UnselectUpdate implements Update{

    private final String section;
    private final String player;

    public UnselectUpdate(String section, String player){
        this.section = section;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {

    }

    @Override
    public void checkFormat() {
        if(section == null)
            throw new NullPointerException("Pointer to section is null");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public String getSection() {
        return section;
    }

    public String getPlayer() {
        return player;
    }
}
