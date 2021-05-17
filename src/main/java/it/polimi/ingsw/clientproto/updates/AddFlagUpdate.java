package it.polimi.ingsw.clientproto.updates;

import it.polimi.ingsw.clientproto.model.ClientModel;
import it.polimi.ingsw.parser.raw.RawLevelFlag;

public class AddFlagUpdate implements Update{

    private final RawLevelFlag flag;
    private final String player;

    public AddFlagUpdate(RawLevelFlag flag, String player){
        this.flag = flag;
        this.player = player;
        checkFormat();
    }

    @Override
    public void apply(ClientModel client) {

    }

    @Override
    public void checkFormat() {
        if(flag == null)
            throw new NullPointerException("pointer to flag is null");
        if(player == null)
            throw new NullPointerException("Pointer to player is null");
    }

    public RawLevelFlag getFlag() {
        return flag;
    }

    public String getPlayer() {
        return player;
    }
}
